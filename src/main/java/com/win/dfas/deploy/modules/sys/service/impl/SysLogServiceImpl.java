package com.win.dfas.deploy.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.modules.sys.dao.SysLogDao;
import com.win.dfas.deploy.modules.sys.po.SysLogPO;
import com.win.dfas.deploy.modules.sys.service.SysLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @包名 com.win.dfas.deploy.modules.sys.service.impl
 * @类名 SysLogServiceImpl
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/23 17:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogPO> implements SysLogService {

}
