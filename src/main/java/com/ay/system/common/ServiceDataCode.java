package com.ay.system.common;


/**
 * @Description: 接口响应编码枚举类型
 * @Author: yanglihai
 * @CreateTime: 2022-10-14  18:13
 * @Version: 1.0
 */
public enum ServiceDataCode {

    /**成功: 一切ok正确执行后的返回*/
    GY_00000("00000","成功"),

    /**程序出现错误！请联系客服。*/
    GY_ERROR("ERROR", "程序出现错误！请联系客服。"),

    GY_D0001("D0001", "TOKEN授权校验失败"),

    /**用户验证码错误*/
    GY_A0240("A0240","用户验证码错误"),

    /**用户账户不存在*/
    GY_A0201("A0201","用户账户不存在"),
    /**用户登录已过期*/
    GY_A0230("A0230","用户登录已过期"),

    /**数据库服务出错*/
    GY_C0300("C0300","数据库服务出错"),
    /**数据已存在*/
    GY_D9999("D9999", "数据已存在"),

    /**请求必填参数为空*/
    GY_A0410("A0410","请求必填参数为空"),

    GY_A0400("A0400", "用户请求参数错误"),


    GY_C0331("C0331", "数据库死锁"),
    GY_C0341("C0341", "主键冲突");


    /**
     * 编码
     */
    private final String code;

    /**
     * 提示信息
     */
    private final String message;

    ServiceDataCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @description: 根据编码获取提示信息
     * @param code
     * @return String
     * @author: yanglihai
     * @date: 2022/8/22 13:50
     */
    public static String getMessageByCode(String code) {
        for (ServiceDataCode dataCode : ServiceDataCode.values()) {
            if(dataCode.getCode().equals(code)) {
                return dataCode.getMessage();
            }
        }

        return null;
    }
    /**
     * @description: 根据提示信息获取编码
     * @param message
     * @return String
     * @author: yanglihai
     * @date: 2022/8/22 13:49
     */
    public static String getCodeByMessage(String message){
        for(ServiceDataCode dataCode : ServiceDataCode.values()){
            if (dataCode.getMessage().equals(message)) {
                return dataCode.getCode();
            }
        }
        return  null;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}