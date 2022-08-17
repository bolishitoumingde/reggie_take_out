package com.example.take_out.dto;

import com.example.take_out.entity.Category;
import com.example.take_out.entity.Dish;
import com.example.take_out.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// DTO，全称为Data Transfer object，即数据传输对象，一般用于展示层与服务层之间的数据传输


@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private Category category;

    private Integer copies;
}
