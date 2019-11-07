package com.win.dfas.deploy.vo.request;

import com.win.dfas.deploy.common.validator.group.AddGroup;
import com.win.dfas.deploy.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @包名 com.win.dfas.deploy.vo.request
 * @类名 GroupVO
 * @类描述 TODO:
 * @创建人 heshansen
 * @创建时间 2019/10/08 13:56
 */
@Data
public class GroupVO {
    /**
     * 组id
     */
    @NotNull(message = "组id不能为空！",groups = UpdateGroup.class)
    private Long id;
    /**
     * 设备组名
     */
    @NotBlank(message = "组名不能为空",groups = AddGroup.class)
    private String name;
    /**
     * 设备组描述
     */
    private String desc;
    /**
     * 设备id列表
     */
    @NotNull(message = "设备不能为空",groups = AddGroup.class)
    @Size(message = "设备至少1个",min = 1,groups = AddGroup.class)
    private List<Long> deviceIds;
}
