package com.win.dfas.deploy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.win.dfas.common.vo.BaseReqVO;
import com.win.dfas.deploy.dto.AppModuleInstanceDTO;
import com.win.dfas.deploy.dto.DeviceModuleRefDTO;
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

    List<AppModuleInstanceDTO> treeList();

    /**
     * 获取应用模块服务器启动实例
     * @param id
     * @return
     */
    List<DeviceModuleRefDTO> getInstanceList(Long id);
}
