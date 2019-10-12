package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.common.annotation.SysLog;
import com.win.dfas.deploy.common.validator.ValidatorUtils;
import com.win.dfas.deploy.common.validator.group.AddGroup;
import com.win.dfas.deploy.common.validator.group.UpdateGroup;
import com.win.dfas.deploy.dto.GroupTree;
import com.win.dfas.deploy.po.GroupPO;
import com.win.dfas.deploy.service.GroupService;
import com.win.dfas.deploy.vo.request.DeviceParamsVO;
import com.win.dfas.deploy.vo.request.GroupVO;
import com.win.dfas.deploy.vo.response.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @包名 com.win.dfas.deploy.controller
 * @类名 GroupController
 * @类描述  组控制器
 * @创建人 heshansen
 * @创建时间 2019/09/27 11:40
 */
@RestController
@RequestMapping("/group")
public class GroupController extends BaseController<GroupPO> {
    @Autowired
    private GroupService groupService;

    @Override
    public IService<GroupPO> getBaseService() {
        return this.groupService;
    }

    @PostMapping("/pageList")
    public WinResponseData pageList(@RequestBody DeviceParamsVO deviceParamsVO){
        PageVO<GroupTree> pageVO = this.groupService.getGroupTreePageInfo(deviceParamsVO);
        return WinResponseData.handleSuccess(pageVO);
    }

    /**
     * 新增设备组及其关联设备信息
     * @param groupVO
     * @return
     */
    @SysLog("新增设备组")
    @PostMapping("/safeSave")
    public WinResponseData safeSave(@Validated @RequestBody GroupVO groupVO){
        ValidatorUtils.validateEntity(groupVO, AddGroup.class);
        Boolean success = this.groupService.safeSave(groupVO);
        if (success){
            return WinResponseData.handleSuccess("新增组成功！");
        }else {
            return WinResponseData.handleError("新增组失败！");
        }
    }
    @SysLog("修改设备组")
    @PostMapping("/safeUpdate")
    public WinResponseData safeUpdate(@RequestBody GroupVO groupVO){
        ValidatorUtils.validateEntity(groupVO, UpdateGroup.class);
        Boolean success = this.groupService.safeUpdate(groupVO);
        if (success){
            return WinResponseData.handleSuccess("更新组成功！");
        }else {
            return WinResponseData.handleError("更新组失败！");
        }
    }

    @GetMapping("/getInfo/{id}")
    public WinResponseData getInfo(@PathVariable Long id){
        return WinResponseData.handleSuccess(this.groupService.getInfo(id));
    }
}
