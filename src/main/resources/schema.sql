-- 客户端信息
create table CLIENT_INFO
(
    id            INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
    client_name   VARCHAR(256),
    client_secret VARCHAR(256),
    from_address  VARCHAR(256),
    call_back_url VARCHAR(256),
    gmt_create    DATETIME,
    create_by     VARCHAR(256),
    gmt_modified  DATETIME,
    modified_by   VARCHAR(256),
    is_deleted    tinyint
);

-- 黑名单
create table BLACK_LIST
(
    id           INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
    client_id    INTEGER(20),
    mail_address VARCHAR(256),
    reason       VARCHAR(256),
    level        VARCHAR(256),
    gmt_create   DATETIME,
    create_by    VARCHAR(256),
    gmt_modified DATETIME,
    modified_by  VARCHAR(256),
    is_deleted   tinyint
);

-- 模版表
create table MAIL_TEMPLATE
(
    id                INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
    client_id         INTEGER(20),
    template_name     VARCHAR(256),
    template_content  VARCHAR(256),
    template_variable VARCHAR(256),
    gmt_create        DATETIME,
    create_by         VARCHAR(256),
    gmt_modified      DATETIME,
    modified_by       VARCHAR(256),
    is_deleted        tinyint
);

-- 邮件发送记录表
-- 发送时间,发送方,发送地址,邮件内容,发送状态:1-已发送，2-发送失败,是否已读(扩展)
create table MAIL_SEND_LOG
(
    id           INTEGER(20) PRIMARY KEY AUTO_INCREMENT,
    task_id      VARCHAR(32),
    task_by      VARCHAR(256),
    send_from    VARCHAR(256),
    send_address TEXT,
    send_content VARCHAR(256),
    send_status  INTEGER(2),
    send_desc    TEXT,
    send_time    DATETIME
);