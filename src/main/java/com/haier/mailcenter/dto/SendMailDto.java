package com.haier.mailcenter.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class SendMailDto implements Serializable {
    @NotNull(message = "邮件地址列表不能为空")
    @ApiModelProperty(example = "[\"161789415@qq.com\"]")
    private List<String> mailAddresses;
    @NotNull(message = "模版Id不能为空")
    @ApiModelProperty(example = "1")
    private Integer templateId;
    @NotNull(message = "变量Map不能为空")
    @ApiModelProperty(example = "{\"name\":\"王斌\",\n" +
            "\"course\":\"入职课程\",\n" +
            "\"time\":\"2020-02-02\",\n" +
            "\"teacher\":\"王老师\"}")
    private Map<String, String> variableMap;
    @NotNull(message = "客户端密钥不能为空")
    @ApiModelProperty(example = "123")
    private String clientSecret;
    @ApiModelProperty(example = "邮箱主题")
    private String subject;
    @ApiModelProperty(example = "*/30 * * * * ?")
    private String cron;
    @ApiModelProperty(example = "1590660233")
    private Date delay;
}
