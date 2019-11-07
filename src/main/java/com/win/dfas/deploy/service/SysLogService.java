package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.po.SysLogPO;
import com.win.dfas.deploy.vo.response.PageVO;

/**
 * @包名 com.win.dfas.deploy.service
 * @类名 SysLogService
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/23 17:01
 */
public interface SysLogService extends IService<SysLogPO> {

    PageVO<SysLogPO> getPageInfo(BaseReqVO reqVO);
}
