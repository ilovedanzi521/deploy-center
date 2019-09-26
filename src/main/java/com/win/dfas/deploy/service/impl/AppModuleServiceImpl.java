package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.deploy.dao.AppModuleDao;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.service.AppModuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 AppModuleServiceImpl
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppModuleServiceImpl extends ServiceImpl<AppModuleDao, AppModulePO> implements AppModuleService {
}
