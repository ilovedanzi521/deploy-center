package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.dao.GroupDao;
import com.win.dfas.deploy.po.GroupDeviceRefPO;
import com.win.dfas.deploy.po.GroupPO;
import com.win.dfas.deploy.service.GroupDeviceRefService;
import com.win.dfas.deploy.service.GroupService;
import com.win.dfas.deploy.vo.request.DeviceParamsVO;
import com.win.dfas.deploy.vo.request.GroupVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 GroupServiceImpl
 * @类描述 组服务实现类
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:44
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupServiceImpl extends ServiceImpl<GroupDao, GroupPO> implements GroupService {

    @Autowired
    private GroupDeviceRefService groupDeviceRefService;

    @Override
    public IPage<GroupPO> getGroupTreePageInfo(DeviceParamsVO deviceParamsVO) {
        Page<GroupPO> page = new Page<>(deviceParamsVO.getReqPageNum(),deviceParamsVO.getReqPageSize());
        IPage<GroupPO> iPage = this.page(page,null);
        return iPage;
    }

    @Override
    public Boolean safeSave(GroupVO groupVO) {
        GroupPO groupPO = new GroupPO();
        BeanUtils.copyProperties(groupVO,groupPO);
        Boolean saveGroup = this.save(groupPO);
        if (saveGroup && groupPO.getId()!=null){
            Boolean saveRef = saveRefDevices(groupPO.getId(), groupVO.getDeviceIds());
            if (saveRef){
                return true;
            }
        }
        return false;
    }

    private Boolean saveRefDevices(Long groupId, List<Long> deviceIds) {
        List<GroupDeviceRefPO> refPOList = new ArrayList<>(deviceIds.size());
        for (Long deviceId: deviceIds){
            GroupDeviceRefPO refPO = new GroupDeviceRefPO();
            refPO.setGroupId(groupId);
            refPO.setDeviceId(deviceId);
            refPOList.add(refPO);
        }
        return this.groupDeviceRefService.saveBatch(refPOList);
    }

    @Override
    public Boolean safeUpdate(GroupVO groupVO) {
        Long groupId = groupVO.getId();
        String desc = groupVO.getDesc();
        List<Long> deviceIds = groupVO.getDeviceIds();
        //更新描述信息。
        if (!StringUtils.isEmpty(desc)){
            GroupPO groupPO = new GroupPO();
            BeanUtils.copyProperties(groupVO,groupPO);
            this.updateById(groupPO);
        }
        if (CollectionUtils.isEmpty(deviceIds)){
            return true;
        }else {
            //更新设备信息
            Map<String,Object> columnMap = new HashMap<>();
            columnMap.put("group_id",groupId);
            Boolean delRefs = this.groupDeviceRefService.removeByMap(columnMap);
            if (delRefs){
                Boolean saveRefs = saveRefDevices(groupId, groupVO.getDeviceIds());
                if (saveRefs){
                    return true;
                }
            }
        }
        return false;
    }
}
