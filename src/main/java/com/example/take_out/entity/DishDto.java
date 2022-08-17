package com.example.take_out.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// DTO，全称为Data Transfer object，即数据传输对象，一般用于展示层与服务层之间的数据传输


@Data
@EqualsAndHashCode(callSuper = true)
public class DishDto extends Dish implements Serializable {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
