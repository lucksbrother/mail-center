package com.haier.mailcenter.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity基类.
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * 主键ID.
     */
    @ApiModelProperty(name = "主键", hidden = true)
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间.
     */
    private LocalDateTime gmtCreate;

    /**
     * 创建人.
     */
    private String createBy;

    /**
     * 最后修改时间.
     */
    private LocalDateTime gmtModified;

    /**
     * 最后修改人.
     */
    private String modifiedBy;

    /**
     * 是否删除.
     */
    private Integer isDeleted;

}
