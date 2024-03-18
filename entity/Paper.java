package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

public class Paper implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String birth;
    private String sex;
    private String workdate;
    private String phone;
    private String email;
    private String location;
    private String descr;
    private String expect;
    private String content;
    private Integer userid;
    private String user;
    private String papername;
    private String file;
    private String skills;
    private String education;
    private String years;


}
