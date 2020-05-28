package com.haier.mailcenter.common;

/*
任务类型
 */
public enum JobType {
    /**
     * 周期型cron表达式
     */
    CRON,
    /**
     * 延迟型date执行日期
     */
    DELAY;
}
