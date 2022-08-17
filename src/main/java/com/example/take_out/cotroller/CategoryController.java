package com.example.take_out.cotroller;


import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.Category;
import com.example.take_out.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping
    public R<String> add(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("添加成功");
    }


}
