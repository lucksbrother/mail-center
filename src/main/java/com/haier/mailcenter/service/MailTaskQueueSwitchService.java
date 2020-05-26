package com.haier.mailcenter.service;

public interface MailTaskQueueSwitchService {

    /**
     * 判断队列开关是否打开
     * @return 队列开关状态
     */
    boolean isQueueSwitchOpen();

    /**
     * 打开队列开关
     * @return 操作是否成功
     */
    boolean openQueueSwitch();

    /**
     * 关闭队列开关
     * @return 操作是否成功
     */
    boolean closeQueueSwitch();
}
