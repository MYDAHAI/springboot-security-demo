package com.ay.system.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

/**
 * @Description: 前后端数据交互中封装数据的对象
 * @Author: yanglihai
 * @CreateTime: 2022-10-14  18:09
 * @Version: 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceData {

    /**
     * 编码
     */
    private String code;

    /**
     * 数据
     */
    private Object data;

    /**
     * 信息
     */
    private String message;

    public ServiceData(String message) {
        this.code = ServiceDataCode.GY_ERROR.getCode();
        this.message = message;
    }

    public ServiceData(ServiceDataCode serviceDataCode) {
        this.code = serviceDataCode.getCode();
        this.message = serviceDataCode.getMessage();
    }

    /**
     * 判断是否操作成功
     * @return Boolean
     * @author: yanglihai
     * @date: 2022/11/2 17:10
     */
    public Boolean success() {
        if(ServiceDataCode.GY_00000.getCode().equals(this.code)) {
            return true;
        }
        return false;
    }

    /**
     * @param errorCode 错误码
     * @param errorMsg  自定义异常信息
     * @return ServiceData
     * @description: 获取一个异常结果
     * @author: yanglihai
     * @date: 2022/8/24 13:40
     */
    public static ServiceData getExceptionResult(String errorCode, String errorMsg) {
        ServiceData result = new ServiceData();
        result.setCode(errorCode.isEmpty() ? ServiceDataCode.GY_ERROR.getCode() : errorCode);
        result.setMessage(errorMsg.isEmpty() ? ServiceDataCode.GY_ERROR.getMessage() : errorMsg);
        return result;
    }

    /**
     * @param errorMsg  自定义异常信息
     * @return ServiceData
     * @description: 获取一个异常结果
     * @author: yanglihai
     * @date: 2022/12/10 11:59
     */
    public static ServiceData getExceptionResult(String errorMsg) {
        ServiceData result = new ServiceData(ServiceDataCode.GY_ERROR);
        result.setMessage(errorMsg.isEmpty() ? ServiceDataCode.GY_ERROR.getMessage() : errorMsg);
        return getExceptionResult("", errorMsg);
    }
    /**
     * 根据标识返回对应的结果
     * @param flag
     * @param obj
     * @return ServiceData
     * @author: yanglihai
     * @date: 2022/9/10 18:17
     */
    public static ServiceData getSuccessResult(boolean flag, Object obj) {
        if(flag) {
            return ServiceData.getSuccessResult().setData(obj);
        }
        return ServiceData.getExceptionResult(ServiceDataCode.GY_C0300).setData(obj);
    }
    /**
     * @return ServiceData
     * @description: 获取成功的返回结果
     * @author: yanglihai
     * @date: 2022/8/25 13:48
     */
    public static ServiceData getSuccessResult() {
        return new ServiceData().setDataCode(ServiceDataCode.GY_00000);
    }

    /**
     * 获取成功的返回结果-并设置返回的数据
     * @param obj
     * @return ServiceData
     * @author: yanglihai
     * @date: 2022/10/7 16:25
     */
    public static ServiceData getSuccessResult(Object obj) {
        return new ServiceData().setDataCode(ServiceDataCode.GY_00000).setData(obj);
    }

    /**
     * 根据标识获取执行数据库操作的返回结果
     * @param flag
     * @return ServiceData
     * @author: yanglihai
     * @date: 2022/11/2 14:13
     */
    public static ServiceData getDataBaseResult(Boolean flag) {
        return flag ? getSuccessResult() : ServiceData.getExceptionResult(ServiceDataCode.GY_C0300);
    }

    /**
     * 数据库异常返回
     * @param supplier
     * @param msg
     * @return ServiceData
     * @author cvanlu
     * @date: 2022/9/8 18:29
     */
    public static ServiceData getDataBaseResult(Supplier<Boolean> supplier, String msg) {
        if (supplier.get()) {
            return getSuccessResult().setData(msg);
        }
        return ServiceData.getExceptionResult(ServiceDataCode.GY_C0300);
    }

    /**
     * @param serviceDataCode
     * @return ServiceData
     * @description: 获取一个异常结果
     * @author: yanglihai
     * @date: 2022/8/24 13:56
     */
    public static ServiceData getExceptionResult(ServiceDataCode serviceDataCode) {
        return new ServiceData(serviceDataCode);
    }

    public ServiceData setDataCode(ServiceDataCode serviceDataCode) {
        this.code = serviceDataCode.getCode();
        this.message = serviceDataCode.getMessage();
        return this;
    }

    public String getCode() {
        return code;
    }

    public ServiceData setCode(String code) {
        this.code = code;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ServiceData setData(Object data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ServiceData setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "ServiceData{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}