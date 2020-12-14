package com.javakc.springbootcpims.common.base.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @program: springboot-cpims
 * @Description: 封装实体类公共属性
 * @Author: zhao yun long
 * @date: 2020/10/22 15:57
 */
@Data
public class Base {
    private int id;
    private Integer revision;
    private String createdBy;
    private Date createdTime;
    private String updatedBby;
    private Date updatedTime;
}
