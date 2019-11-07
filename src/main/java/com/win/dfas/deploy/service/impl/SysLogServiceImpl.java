package com.win.dfas.deploy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.dao.SysLogDao;
import com.win.dfas.deploy.po.SysLogPO;
import com.win.dfas.deploy.service.SysLogService;
import com.win.dfas.deploy.vo.response.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @包名 com.win.dfas.deploy.service.impl
 * @类名 SysLogServiceImpl
 * @类描述 操作日志实现类
 * @创建人 heshansen
 * @创建时间 2019/09/23 17:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogPO> implements SysLogService {

    @Override
    public PageVO<SysLogPO> getPageInfo(BaseReqVO reqVO) {
        Page<SysLogPO> page = new Page<>(reqVO.getReqPageNum(),reqVO.getReqPageSize());
        //条件构造器对象
        QueryWrapper<SysLogPO> queryWrapper = new QueryWrapper<SysLogPO>();
        queryWrapper.orderByDesc("create_time");
        IPage<SysLogPO> pageList = this.baseMapper.selectPage(page,queryWrapper);

        return new PageVO(page);
    }
}
