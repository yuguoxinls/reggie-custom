package com.jack.reggiecustom.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 菜品管理
 * @TableName dish
 */
@TableName(value ="dish")
@Data
public class Dish implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 菜品分类id
     */
    private Long categoryId;

    /**
     * 菜品价格
     */
    private BigDecimal price;

    /**
     * 商品码
     */
    private String code;

    /**
     * 图片
     */
    private String image;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 0 停售 1 起售
     */
    private Integer status;

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

    /**
     * 是否删除
     */
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}