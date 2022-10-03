package com.jack.reggiecustom.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 菜品及套餐分类
 * @TableName category
 */
@TableName(value ="category")
@Data
public class Category implements Serializable {
    /**
     * 主键
     */
    @TableId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 类型   1 菜品分类 2 套餐分类
     */
    private Integer type;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT) //指定什么时候填充字段：插入时填充
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT) //插入时填充
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充
    private Long updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}