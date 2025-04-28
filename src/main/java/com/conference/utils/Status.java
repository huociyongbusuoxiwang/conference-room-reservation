package com.conference.utils;

// 状态类 - 用于维护

public class Status {

    // 账号状态
    public static final String CHECKING = "审核中";
    public static final String ACCOUNT_FROZEN = "已冻结";
    public static final String NORMAL_USING = "正常使用";
    public static final String CHECKING_REJECT = "审核不通过";

    // 订单状态
    public static final String UNPAID = "未支付";
    public static final String PAID = "已支付";
    public static final String CANCEL = "已取消";
    public static final String FINISH = "已完成";
    public static final String REFUND = "已退款";

    // 会议室状态
    public static final String AVAILABLE = "空闲中";
    public static final String UNAVAILABLE = "已锁定";
    public static final String BOOKED = "已被预订";
    public static final String UNDER_USING = "使用中";
    public static final String UNDER_REPAIR = "维护中";

    // 维护状态
    public static final String UNREPAIR = "未处理";
    public static final String REPAIRING = "处理中";
    public static final String REPAIRED = "已处理";
}
