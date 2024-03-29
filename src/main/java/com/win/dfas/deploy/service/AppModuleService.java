package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.dto.AppModuleInstanceDTO;
import com.win.dfas.deploy.dto.DeviceModuleRefDTO;
import com.win.dfas.deploy.dto.StatisticsDTO;
import com.win.dfas.deploy.po.AppModulePO;
import com.win.dfas.deploy.vo.response.AppModuleTreeVO;
import com.win.dfas.deploy.vo.response.PageVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.service
 * @类名 AppModuleService
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/09/26 14:42
 */
public interface AppModuleService extends IService<AppModulePO> {

    public boolean start(String ipAddr, String moduleName);

    public boolean stop(String ipAddr, String moduleName);

    public int status(String ipAddr, String moduleName);

    /**
     * 获取分页列表
     * @param reqVO
     * @return
     */
    PageVO<AppModuleTreeVO> getAppModuleTreePageInfo(BaseReqVO reqVO);

    /**
     * 上传文件
     * @param file
     * @return
     */
    void uploadFile(MultipartFile file);

    /**
     * 获取所有应用所有服务实例列表
     * @return
     */
    List<AppModuleInstanceDTO> appInstanceList();

    /**
     * 获取应用模块服务器启动实例
     * @param id
     * @return
     */
    List<DeviceModuleRefDTO> getInstanceList(Long id);

    /**
     * 获取应用服务实例统计信息
     * @return
     */
    StatisticsDTO getStatisticsInfo();

    /**
     * 安全删除
     * @param id
     * @return
     */
    Boolean safeRemove(Long id);

    /**
     * 安全批量删除
     * @param ids
     * @return
     */
    Boolean safeRemoveBatch(List<Long> ids);
}
