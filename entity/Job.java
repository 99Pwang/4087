package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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

public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String experience;
    private String location;
    private String edu;
    private String descr;
    private String price;
    private String company;
    private String skills;
    private String man;
    private String money;
    private String date;
    private String manager;
    private Integer managerid;
    private String status;
    private String time;
    private String category;
    @TableField(exist = false)
    private Boolean hasCollect;
    @TableField(exist = false)
    private Boolean hasApply;

    private String tags;
    private String salary;

    @TableField(exist = false)
    private Integer score;

    private String highlights;
    private String type;
    private String ways;
    private String time2;
    private String welfare;
    private String recommend;
    private String phone;

}
