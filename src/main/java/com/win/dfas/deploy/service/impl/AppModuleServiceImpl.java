package com.win.dfas.deploy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.common.enumerate.DeployEnum;
import com.win.dfas.deploy.common.exception.BaseException;
import com.win.dfas.deploy.dao.AppModuleDao;
import com.win.dfas.deploy.dto.AppModuleInstanceDTO;
import com.win.dfas.deploy.dto.DeviceModuleRefDTO;
import com.win.dfas.deploy.dto.StatisticsDTO;
import com.win.dfas.deploy.dto.SubInstanceDTO;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.service.AppModuleService;
import com.win.dfas.deploy.service.DeviceModuleService;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.util.DeployUtils;
import com.win.dfas.deploy.util.SpringContextUtils;
import com.win.dfas.deploy.vo.response.AppModuleTreeVO;
import com.win.dfas.deploy.vo.response.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 AppModuleServiceImpl
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:43
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AppModuleServiceImpl extends ServiceImpl<AppModuleDao, AppModulePO> implements AppModuleService {

    @Autowired
    private ScheduleCenterService mScheduleService;
    @Autowired
    private DeviceModuleService deviceModuleService;

    @Autowired
    private Environment env;

    @Override
    public boolean start(String ipAddr, String moduleName) {
        return mScheduleService.moduleStart(ipAddr, moduleName);
    }

    @Override
    public boolean stop(String ipAddr, String moduleName) {
        return mScheduleService.moduleStop(ipAddr, moduleName);
    }

    @Override
    public int status(String ipAddr, String moduleName) {
        return mScheduleService.moduleStatus(ipAddr, moduleName);
    }

    @Override
    public PageVO<AppModuleTreeVO> getAppModuleTreePageInfo(BaseReqVO reqVO) {
        Page<AppModulePO> page = new Page<>(reqVO.getReqPageNum(),reqVO.getReqPageSize());
        //条件构造器对象
        QueryWrapper<AppModulePO> queryWrapper = new QueryWrapper<AppModulePO>();
        queryWrapper.orderByDesc("create_time");
        IPage<AppModulePO> pageList = this.baseMapper.selectPage(page,queryWrapper);

        return new PageVO(page,toTreeList(pageList.getRecords()));
    }

    @Override
    public void uploadFile(MultipartFile file) {
        DeployEnvBean deployEnv = SpringContextUtils.getBean("deploy_env_bean", DeployEnvBean.class);
        File zipTempFile = null;
        try {
            //1,解析部署包，并检查格式
            DeployUtils.parseDeployZipFile(file,deployEnv);
            //2，保存部署包到java默认临时目录
            zipTempFile = DeployUtils.saveZipToTempFile(file,deployEnv);
        } catch (IOException e) {
            FileUtil.del(zipTempFile);
            throw new BaseException("上传异常:"+e.getMessage());
        }
        Boolean upgraded = false;
        try {
            //3.解压部署包到仓库并升级模块和策略数据
            upgraded = this.mScheduleService.upgradAppModule(zipTempFile.getAbsolutePath(),deployEnv.getHomeDir());
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }
        if (!upgraded){
            FileUtil.del(zipTempFile);
            throw new BaseException("压缩包初始化异常");
        }
    }

    @Override
    public List<AppModuleInstanceDTO> appInstanceList() {
        List<AppModulePO> list = this.list();
        List<AppModuleInstanceDTO> instanceDTOS = new ArrayList<>(list.size());
        if (CollectionUtil.isNotEmpty(list)){
            for (AppModulePO appModulePO: list) {
                AppModuleInstanceDTO instanceDTO = new AppModuleInstanceDTO();
                instanceDTO.setMicroServiceName(appModulePO.getName());
                instanceDTO.setMicroServiceAlias(appModulePO.getPackFile());
                List<DeviceModuleRefDTO> refDTOS = this.getInstanceList(appModulePO.getId());
                List<SubInstanceDTO> subInstanceDTOS = new ArrayList<>(refDTOS.size());
                if (CollectionUtil.isNotEmpty(refDTOS)){
                    for (DeviceModuleRefDTO refDTO: refDTOS) {
                        SubInstanceDTO subInstanceDTO = new SubInstanceDTO();
                        subInstanceDTO.setHostName(refDTO.getDeviceName());
                        subInstanceDTO.setIpAddr(refDTO.getIpAddress());
                        subInstanceDTOS.add(subInstanceDTO);
                    }
                    instanceDTO.setInstanceList(subInstanceDTOS);
                }
                instanceDTOS.add(instanceDTO);
            }
        }
        return instanceDTOS;
    }

    @Override
    public List<DeviceModuleRefDTO> getInstanceList(Long id) {
        List<DeviceModuleRefDTO> refDTOS = this.baseMapper.selectDeviceModuleRefList(id);
        if (CollectionUtil.isNotEmpty(refDTOS)){
            for (DeviceModuleRefDTO refDto : refDTOS) {
                int status = this.mScheduleService.moduleStatus(refDto.getIpAddress(),refDto.getModuleName());
                refDto.setStatus(status);
            }
        }
        return refDTOS;
    }

    @Override
    public StatisticsDTO getStatisticsInfo() {
        int total = 0;
        int success = 0;
        int error = 0;
        List<AppModulePO> appList = this.list();
        if (CollectionUtil.isNotEmpty(appList)){
            for (AppModulePO appModulePO : appList) {
                List<DeviceModuleRefDTO> refDTOS = this.getInstanceList(appModulePO.getId());
                if (CollectionUtil.isNotEmpty(refDTOS)){
                    total+=refDTOS.size();
                    for (DeviceModuleRefDTO refDTO: refDTOS) {
                        int status = refDTO.getStatus();
                        if (status>0){
                            success++;
                        }else if (status==-1){
                            error++;
                        }
                    }
                }
            }
        }
        StatisticsDTO dto = new StatisticsDTO();
        dto.setTotal(total);
        dto.setSuccess(success);
        dto.setError(error);
        dto.setWarning(total-success-error);
        return dto;
    }

    @Override
    public Boolean safeRemove(Long id) {
        beforeRemoveSafeValid(id);
        return this.removeById(id);
    }

    @Override
    public Boolean safeRemoveBatch(List<Long> ids) {
        for (Long id : ids) {
            beforeRemoveSafeValid(id);
        }
        return this.removeByIds(ids);
    }

    /**
     * 删除之前必须进行安全验证
     * 1.是否存在。
     * 2.是否已经部署。已经部署的应用需要先卸载再删除。
     * 3.删除应用前先删除调度中心记录
     * @param moduleId
     */
    private void beforeRemoveSafeValid(Long moduleId) {
        AppModulePO appModulePO = this.getById(moduleId);
        if (appModulePO==null){
            throw new BaseException("应用模块["+moduleId+"]已经不存在！");
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("module_id",moduleId);
        int count = this.deviceModuleService.count(queryWrapper);
        if (count>0){
            throw new BaseException("应用模块["+appModulePO.getName()+"]已经部署，无法安全删除！");
        }
        //删除调度中心应用模块内存记录
        Scheduler.get().getAppManager().removeModuleByPath(appModulePO.getPath());
    }

    /**
     * 上传文件到ftp仓库
     * TODO: 暂时将ftp仓库和部署中心放在一台机器，无需进行ftp远程上传存储
     * @param file
     * @return
     */
    public Boolean uploadToRemoteFtpRepo(File file){
        DeployEnvBean deployEnv = SpringContextUtils.getBean("deploy_env_bean", DeployEnvBean.class);
        Ftp ftp = new Ftp(deployEnv.getFtpHost(),deployEnv.getFtpPort(),deployEnv.getFtpUser(),deployEnv.getFtpPswd());
        String ftpRepoTempPath = deployEnv.getFtpTempDir();
        String ftpRepoTempFile = ftpRepoTempPath+File.separator+file.getName();
        Boolean isUpload = ftp.upload(ftpRepoTempPath,file);
        if (!isUpload){
            throw new BaseException("上传Ftp失败");
        }
        Boolean isUpgraded = false;
        try {
            isUpgraded = this.mScheduleService.upgradAppModule(ftpRepoTempFile,deployEnv.getHomeDir());
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }
        if (!isUpgraded){
            throw new BaseException("解压异常");
        }
        return (isUpload && isUpgraded);
    }

    private List<AppModuleTreeVO> toTreeList(List<AppModulePO> records) {
        List<AppModuleTreeVO> treeVOList = new ArrayList<>(records.size());
        for (AppModulePO po : records) {
            AppModuleTreeVO treeVO = new AppModuleTreeVO();
            BeanUtils.copyProperties(po,treeVO);
            treeVO.setHasChildren(true);
            treeVOList.add(treeVO);
        }
        return treeVOList;
    }

}
