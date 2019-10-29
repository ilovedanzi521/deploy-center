package com.win.dfas.deploy.dto;

import lombok.Data;

import java.util.List;

/**
 * @包名 com.win.dfas.deploy.dto
 * @类名 AppModuleInstanceDTO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/29 15:52
 */
@Data
public class AppModuleInstanceDTO {
    private  String  microServiceName;
    private  String  microServiceAlias;
    private List<SubInstanceDTO> instanceList;
}
