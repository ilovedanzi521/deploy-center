package com.win.dfas.deploy.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.modules.sys.dao.UserDao;
import com.win.dfas.deploy.modules.sys.entity.SysUser;
import com.win.dfas.deploy.modules.sys.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @包名 com.win.dfas.deploy.modules.sys.service.impl
 * @类名 UserServiceImpl
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/23 10:27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserDao, SysUser> implements UserService {

}
