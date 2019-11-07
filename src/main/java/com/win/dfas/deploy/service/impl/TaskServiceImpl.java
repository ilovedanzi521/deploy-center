package com.win.dfas.deploy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.common.enumerate.DeployEnum;
import com.win.dfas.deploy.common.exception.BaseException;
import com.win.dfas.deploy.dao.TaskDao;
import com.win.dfas.deploy.dto.StatisticsDTO;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.StrategyPO;
import com.win.dfas.deploy.po.TaskPO;
import com.win.dfas.deploy.schedule.Scheduler;
import com.win.dfas.deploy.schedule.context.ScheduleContext;
import com.win.dfas.deploy.service.GroupService;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.service.StrategyService;
import com.win.dfas.deploy.service.TaskService;
import com.win.dfas.deploy.vo.response.PageVO;
import com.win.dfas.deploy.dto.TaskDTO;
import com.win.dfas.deploy.vo.response.TaskDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 TaskServiceImpl
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:44
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskPO> implements TaskService {

    @Autowired
    private ScheduleCenterService mScheduleService;
    @Autowired
    private StrategyService strategyService;
    @Autowired
    private GroupService groupService;
    /**
     * 从任务ID获取对应的策略
     * @param taskId - task id
     */
    @Override
    public StrategyPO selectStrategyByTask(long taskId) {
        StrategyPO strategy = (StrategyPO)this.sqlSessionBatch().selectOne("com.win.dfas.deploy.dao.TaskDao.selectStrategyByTask", taskId);
        return strategy;
    }

    /**
     *  从任务ID获取对应的组设备列表
     * @param taskId
     * @return
     */
    @Override
    public List<DevicePO> selectDeviceByTask(long taskId) {
        List<DevicePO> list = (List)this.sqlSessionBatch().selectList("com.win.dfas.deploy.dao.TaskDao.selectDeviceByTask", taskId);
        return list;
    }

    /**
     * 分页列表
     * @param reqVO
     * @return
     */
    @Override
    public PageVO<TaskDTO> getPageInfo(BaseReqVO reqVO) {
        Page<TaskDTO> page = new Page<>(reqVO.getReqPageNum(),reqVO.getReqPageSize());
        //条件构造器对象
        QueryWrapper<TaskPO> queryWrapper = new QueryWrapper<TaskPO>();
        queryWrapper.orderByDesc("create_time");
        IPage<TaskDTO> pageList = this.baseMapper.getPageList(page,queryWrapper);

        return new PageVO(page,pageList.getRecords());
    }

    @Override
    public void deploy(Long id) {
        TaskPO task = this.getById(id);
        if (beforeDeployValid(task)){
            this.mScheduleService.deploy(id);
        }
    }

    /**
     * 部署前检查状态
     * @param task
     * @return
     */
    private boolean beforeDeployValid(TaskPO task) {
        if(task == null) {
            throw new BaseException("部署异常：任务["+task.getId()+"]已经不存在！");
        }else if (DeployEnum.TaskStatus.DEPLOY_UNDERWAY.getValue().equals(task.getStatus())){
            throw new BaseException("部署异常：任务正在部署中！");
        }else if (DeployEnum.TaskStatus.DEPLOY_SUCCESS.getValue().equals(task.getStatus())){
            throw new BaseException("部署异常：任务已经部署成功！");
        }else if (DeployEnum.TaskStatus.UNINSTALL_UNDERWAY.getValue().equals(task.getStatus())){
            throw new BaseException("部署异常：任务正在卸载中！");
        }else if (DeployEnum.TaskStatus.UNINSTALL_FAILURE.getValue().equals(task.getStatus())){
            throw new BaseException("部署异常：任务已经部署！");
        }
        return true;
    }

    @Override
    public void unDeploy(Long id) {
        TaskPO task = this.getById(id);
        if(task == null) {
            throw new BaseException("部署异常：任务["+id+"]已经不存在！");
        }else if (DeployEnum.TaskStatus.DEPLOY_NONE.getValue().equals(task.getStatus())){
            throw new BaseException("部署异常：任务未部署！");
        }else if (DeployEnum.TaskStatus.DEPLOY_UNDERWAY.getValue().equals(task.getStatus())){
            throw new BaseException("部署异常：任务正在部署中！");
        }else if (DeployEnum.TaskStatus.DEPLOY_FAILURE.getValue().equals(task.getStatus())){
            throw new BaseException("部署异常：任务部署失败！");
        }else if (DeployEnum.TaskStatus.UNINSTALL_UNDERWAY.getValue().equals(task.getStatus())){
            throw new BaseException("部署异常：任务正在卸载中！");
        }else if (DeployEnum.TaskStatus.UNINSTALL_SUCCESS.getValue().equals(task.getStatus())){
            throw new BaseException("部署异常：任务已经卸载成功！");
        }
        this.mScheduleService.undeploy(id);
    }

    @Override
    public void appSourceScan() {
        this.mScheduleService.appSourceScan();
    }

    @Override
    public StrategyPO getStrategyByTask(TaskPO taskPO) {
        return this.strategyService.getById(taskPO.getStrategyId());
    }

    @Override
    public List<DevicePO> getDevicesByTask(TaskPO taskPO) {
        return this.groupService.getDevicesByGroupId(taskPO.getGroupId());
    }

    @Override
    public TaskDetailVO getDetailById(Long id) {
        TaskDetailVO detailVO = new TaskDetailVO();
        //1.查任务详情
        QueryWrapper<TaskPO> queryWrapper = new QueryWrapper<TaskPO>();
        queryWrapper.eq("t.id",id);
        IPage<TaskDTO> pageList = this.baseMapper.getPageList(new Page<>(),queryWrapper);
        if (CollectionUtil.isNotEmpty(pageList.getRecords()) && pageList.getRecords().size()==1){
            TaskDTO taskDTO = pageList.getRecords().get(0);
            detailVO.setId(taskDTO.getId());
            detailVO.setStatus(taskDTO.getStatus());
            detailVO.setLogPath(taskDTO.getLogPath());
            detailVO.setStrategyName(taskDTO.getStrategy().getName());
            detailVO.setGroupName(taskDTO.getGroup().getName());
            detailVO.setCreateTime(taskDTO.getCreateTime());

            // 2. 查策略绑定应用模块列表
            detailVO.setAppModules(strategyService.getAppModules(taskDTO.getStrategy()));
            //3.查组设备列表
            detailVO.setDevices(groupService.getDevicesByGroupId(taskDTO.getGroupId()));
            //4.查日志详情
            if (StrUtil.isNotBlank(detailVO.getLogPath())){
                detailVO.setLogInfo(FileUtil.readUtf8Lines(detailVO.getLogPath()));
            }
        }
        return detailVO;
    }

    @Override
    public List<String> getDeployLogInfo(String ipAddress, String strategyName) {
        ScheduleContext context = Scheduler.get().getRemoteContext(ipAddress);
        List<String> lines = new ArrayList<>();
        if (context==null){
            String errLine = "调度中心不存在设备："+ipAddress;
            lines.add(errLine);
            log.error(errLine);
            return lines;
        }
        String filePath = context.getLogFile(strategyName);
        log.info("readUtf8Lines from "+filePath);
        if (!FileUtil.file(filePath).exists()){
            String errLine = filePath+"日志文件不存在！";
            lines.add(errLine);
            log.error(errLine);
            return lines;
        }
        return FileUtil.readUtf8Lines(filePath);
    }

    @Override
    public StatisticsDTO getStatisticsInfo() {

        return this.baseMapper.selectStatisticsInfo();
    }

    @Override
    public TaskPO safeSave(TaskPO taskPO) {
        if (getExistTask(taskPO) != null){
            throw new BaseException("任务已经存在！");
        }
        this.save(taskPO);
        return taskPO;
    }

    private TaskPO getExistTask(TaskPO taskPO) {
        QueryWrapper<TaskPO> queryWrapper = new QueryWrapper<TaskPO>();
        queryWrapper.eq("strategy_id",taskPO.getStrategyId());
        queryWrapper.eq("group_id",taskPO.getGroupId());
        return this.getOne(queryWrapper);
    }

    @Override
    public Boolean oneKeyDeploy(TaskPO taskPO) {
        TaskPO existPo=getExistTask(taskPO);
        if (existPo == null){
            boolean saved =  this.save(taskPO);
            if (saved){
                this.deploy(taskPO.getId());
            }
        }else {
            taskPO=existPo;
            if(beforeDeployValid(existPo)){
                this.deploy(existPo.getId());
            }
        }

        return true;
    }
}
