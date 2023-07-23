package com.ay.system.common;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author cvanlu
 * @version 1.0
 * @description TODO
 * @CreateTime: 2022-10-20  15:36
 */
public class BaseEntity {
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected Integer createBy;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    protected Integer updateBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;
    @TableField(value = "enable_flag", fill = FieldFill.INSERT)
    protected String enableFlag;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    public Integer getCreateBy() {
        return createBy;
    }

    public BaseEntity setCreateBy(Integer createBy) {
        this.createBy = createBy;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public BaseEntity setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public BaseEntity setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public BaseEntity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public BaseEntity setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public BaseEntity setId(Integer id) {
        this.id = id;
        return this;
    }
}
