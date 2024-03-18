package com.example.springboot.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.hutool.core.annotation.Alias;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since
 */
@Getter
@Setter

public class Applys implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer jobid;
    private Integer userid;
    private String time;
    private Integer paperid;
    private String status;
    private Integer managerid;
    @TableField(exist = false)
    private String file;
    private String reason;
    private Double scores;

    @TableField(exist = false)
    private Paper paper;
}
