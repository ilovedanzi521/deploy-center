package com.win.dfas.deploy.vo.response;

import com.win.dfas.deploy.po.AppModulePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.vo.response
 * @类名 AppModuleTreeVO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/18 13:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppModuleTreeVO extends AppModulePO {
    private static final long serialVersionUID = 536618326714404400L;

    /**
     * 是否有子节点
     */
    private Boolean hasChildren;
}
