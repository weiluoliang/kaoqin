package com.liang.entities;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * (Headers)实体类
 *
 * @author makejava
 * @since 2024-04-11 11:37:24
 */
@Data
@TableName("t_headers")
public class HeadersPO implements Serializable {
    private static final long serialVersionUID = -75876767117301848L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("header_name")
    private String headerName;
    @TableField("header_value")
    private String headerValue;


}

