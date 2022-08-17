package com.example.take_out.mapper;

import com.example.take_out.dto.DishDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishDtoMapper {
    List<DishDto> getPage(@Param("currentPage") int currentPage,
                          @Param("pageSize") int pageSize,
                          @Param("name") String name);
}
