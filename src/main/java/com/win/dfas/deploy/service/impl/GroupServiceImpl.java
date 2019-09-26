package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.dao.GroupDao;
import com.win.dfas.deploy.po.GroupPO;
import com.win.dfas.deploy.service.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 GroupServiceImpl
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupServiceImpl extends ServiceImpl<GroupDao, GroupPO> implements GroupService {
}
