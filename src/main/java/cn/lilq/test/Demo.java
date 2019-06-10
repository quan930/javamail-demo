package cn.lilq.test;

import cn.lilq.JMailUtil;

public class Demo {
    public static void main(String[] args) throws Exception {
        JMailUtil.sendMessage(new String[]{"1583090876@qq.com"},"通知","老师您好，明天上午8：30开会");
    }
}