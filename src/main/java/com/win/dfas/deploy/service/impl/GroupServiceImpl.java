package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.dto.DeviceGroupDTO;
import com.win.dfas.deploy.common.exception.BaseException;
import com.win.dfas.deploy.dao.GroupDao;
import com.win.dfas.deploy.dto.GroupTree;
import com.win.dfas.deploy.po.DevicePO;
import com.win.dfas.deploy.po.GroupDeviceRefPO;
import com.win.dfas.deploy.po.GroupPO;
import com.win.dfas.deploy.service.GroupDeviceRefService;
import com.win.dfas.deploy.service.GroupService;
import com.win.dfas.deploy.vo.request.DeviceParamsVO;
import com.win.dfas.deploy.vo.request.GroupVO;
import com.win.dfas.deploy.vo.response.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    public PageVO<GroupTree> getGroupTreePageInfo(DeviceParamsVO deviceParamsVO) {
        Page<DeviceGroupDTO> groupPage = (Page<DeviceGroupDTO>) this.getPageList(deviceParamsVO);
        //转成树形表格
        return new PageVO(groupPage,toGroupTree(groupPage.getRecords()));
    }

    private List<GroupTree> toGroupTree(List<DeviceGroupDTO> records) {
        List<GroupTree> treeList = new ArrayList<>(records.size());
        for (DeviceGroupDTO deviceGroupDTO : records){
            GroupTree groupTree = new GroupTree();
            BeanUtils.copyProperties(deviceGroupDTO,groupTree);
            List<DevicePO> devices = deviceGroupDTO.getDevices();
            if (!CollectionUtils.isEmpty(devices)){
                List<GroupTree> children = new ArrayList<>(devices.size());
                for (DevicePO devicePO: devices) {
                    GroupTree nodeTree = new GroupTree();
                    BeanUtils.copyProperties(devicePO,nodeTree);
                    children.add(nodeTree);
                }
                groupTree.setChildren(children);
            }
            treeList.add(groupTree);
        }
        return treeList;
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

    /**
     * 保存组关联设备信息
     * @param groupId
     * @param deviceIds
     * @return
     */
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
            Boolean updateGroup = this.updateById(groupPO);
            if (!updateGroup){
                throw new BaseException("组更新失败");
            }
        }
        if (CollectionUtils.isEmpty(deviceIds)){
            return true;
        }else {
            //更新设备信息
            Map<String,Object> columnMap = new HashMap<>(1);
            columnMap.put("group_id",groupId);
            Boolean delRefs = this.groupDeviceRefService.removeByMap(columnMap);
            Boolean saveRefs = saveRefDevices(groupId, groupVO.getDeviceIds());
            if (delRefs && saveRefs){
                return true;
            }else{
                throw new BaseException("组关联信息更新失败");
            }
        }
    }

    @Override
    public DeviceGroupDTO getInfo(Long id) {
        return baseMapper.getOne(id);
    }

    @Override
    public IPage<DeviceGroupDTO> getPageList(DeviceParamsVO deviceParamsVO) {
        Page<DeviceGroupDTO> page = new Page<>(deviceParamsVO.getReqPageNum(),deviceParamsVO.getReqPageSize());
        //条件构造器对象
        QueryWrapper<DeviceGroupDTO> queryWrapper = new QueryWrapper<DeviceGroupDTO>();
        queryWrapper.orderByDesc("create_time");
        IPage<DeviceGroupDTO> pageList;
        try {
             pageList = this.baseMapper.getPageList(page,queryWrapper);
        } catch (Exception e) {
            log.error("查询设置组分页列表失败",e);
            throw new BaseException("查询设置组分页列表失败",e);
        }
        return pageList;
    }

    /**
     * 安全删除设备组（先删除组关联设备表信息，再删除组信息）
     * @param id
     * @return
     */
    @Override
    public Boolean safeRemove(Long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("group_id",id);
        this.groupDeviceRefService.removeByMap(params);

        return this.removeById(id);
    }

    @Override
    public Boolean safeRemoveBatch(List<Long> ids) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("group_id",ids);
        this.groupDeviceRefService.remove(wrapper);
        return this.removeByIds(ids);
    }

    @Override
    public List<DevicePO> getDevicesByGroupId(Long groupId) {

        return this.baseMapper.selectDevicesByGroupId(groupId);
    }
}
