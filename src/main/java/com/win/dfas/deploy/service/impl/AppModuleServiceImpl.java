package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.bo.AppModuleBO;
import com.win.dfas.deploy.bo.DeviceGroupBO;
import com.win.dfas.deploy.common.exception.BaseException;
import com.win.dfas.deploy.dao.AppModuleDao;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.schedule.context.ScheduleContext;
import com.win.dfas.deploy.service.AppModuleService;
import com.win.dfas.deploy.service.ScheduleCenterService;
import com.win.dfas.deploy.vo.request.DeviceParamsVO;
import com.win.dfas.deploy.vo.response.AppModuleTreeVO;
import com.win.dfas.deploy.vo.response.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
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
