package com.liang.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class WorkApproveVo {


    private Integer id;


    private String approveType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double hours;
}
