package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.dao.SysUserDao;
import com.win.dfas.deploy.po.SysUserPO;
import com.win.dfas.deploy.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 UserServiceImpl
 * @类描述 用户服务实现类
 * @创建人 heshansen
 * @创建时间 2019/09/23 10:27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserPO> implements SysUserService {

}
