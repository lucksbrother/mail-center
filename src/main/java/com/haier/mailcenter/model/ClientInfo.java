package com.haier.mailcenter.model;

import com.haier.mailcenter.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ClientInfo extends BaseEntity {

    private String clientName;
    private String clientSecret;
    private String fromAddress;
    private String callBackUrl;
}
