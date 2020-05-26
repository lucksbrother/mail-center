package com.haier.mailcenter.model;

import com.haier.mailcenter.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class BlackList extends BaseEntity {

    @NotNull(message = "系统id不能为空")
    private Long clientId;
    @NotNull(message = "黑名单地址不能为空")
    private String mailAddress;
    private String reason;
    private Integer level;
}
