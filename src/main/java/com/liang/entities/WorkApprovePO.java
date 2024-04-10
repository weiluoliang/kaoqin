package com.liang.entities;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * (TWorkApprove)实体类
 *
 * @author makejava
 * @since 2024-04-10 18:25:42
 */
@Data
@TableName("t_work_approve")
public class WorkApprovePO implements Serializable {
    private static final long serialVersionUID = -66146078263197850L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("approve_type")
    private String approveType;
    @TableField("start_time")
    private Date startTime;
    @TableField("end_time")
    private Date endTime;
    @TableField("hours")
    private Double hours;


}

