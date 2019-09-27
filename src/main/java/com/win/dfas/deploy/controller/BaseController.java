package com.win.dfas.deploy.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.WinResponseData;
import com.win.dfas.deploy.common.annotation.SysLog;
import com.win.dfas.deploy.po.BasePO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.controller
 * @类名 BaseController
 * @类描述 通用BaseController,封装单表CRUD操作
 * @创建人 heshansen
 * @创建时间 2019/09/26 15:24
 */
public abstract class BaseController<T extends BasePO> {
    /**
     * 抽象业务服务，必须在控制层注入
     * @return
     */
    public abstract IService<T> getBaseService();

    @SysLog
    @PostMapping("/save")
    public WinResponseData save(@RequestBody T t){
        Boolean flag = this.getBaseService().save(t);
        if (!flag){
            return WinResponseData.handleError("新增异常");
        }
        return WinResponseData.handleSuccess(t);
    }

    @SysLog
    @PostMapping("/update")
    public WinResponseData updateById(@RequestBody T t){
        Boolean flag = this.getBaseService().updateById(t);
        if (!flag){
            return WinResponseData.handleError("修改异常");
        }
        return WinResponseData.handleSuccess(t);
    }

    @SysLog
    @PostMapping("/saveOrUpdate")
    public WinResponseData saveOrUpdate(@RequestBody T t){
        Boolean flag = this.getBaseService().saveOrUpdate(t);
        if (!flag){
            return WinResponseData.handleError("修改异常");
        }
        return WinResponseData.handleSuccess(t);
    }

    @SysLog
    @DeleteMapping("/remove/{id}")
    public WinResponseData removeById(@PathVariable Long id){
        Boolean flag = this.getBaseService().removeById(id);
        if (!flag){
            return WinResponseData.handleError("删除异常");
        }
        return WinResponseData.handleSuccess("删除成功");
    }

    @SysLog
    @DeleteMapping("/remove")
    public WinResponseData removeByIds(@RequestBody List<Long> ids){
        Boolean flag = this.getBaseService().removeByIds(ids);
        if (!flag){
            return WinResponseData.handleError("删除异常");
        }
        return WinResponseData.handleSuccess("删除成功");
    }

    @GetMapping("/info/{id}")
    public WinResponseData getById(@PathVariable Long id){
        return WinResponseData.handleSuccess(this.getBaseService().getById(id));
    }

}
