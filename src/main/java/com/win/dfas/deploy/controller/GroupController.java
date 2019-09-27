package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.deploy.po.GroupPO;
import com.win.dfas.deploy.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
