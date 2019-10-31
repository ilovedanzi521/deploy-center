package com.win.dfas.deploy.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.common.exception.BaseException;
import com.win.dfas.deploy.dao.AppModuleDao;
import com.win.dfas.deploy.dto.AppModuleInstanceDTO;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.schedule.bean.DeployEnvBean;
import com.win.dfas.deploy.service.AppModuleService;
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
            DeployUtils.parseDeployZipFile(file,deployEnv);
            zipTempFile = DeployUtils.saveZipToTempFile(file,deployEnv);
        } catch (IOException e) {
            FileUtil.del(zipTempFile);
            throw new BaseException("上传异常:"+e.getMessage());
        }
        Boolean upgraded = false;
        try {
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
    public List<AppModuleInstanceDTO> treeList() {

        return null;
    }

    @Override
    public List<DevicePO> getInstanceList(Long id) {

        return this.baseMapper.getInstanceList(id);
    }

    /**
     * 上传文件到ftp仓库
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
