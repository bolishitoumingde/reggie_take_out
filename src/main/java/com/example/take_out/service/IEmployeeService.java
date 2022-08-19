package com.example.take_out.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.take_out.controller.utils.R;
import com.example.take_out.entity.Employee;


public interface IEmployeeService extends IService<Employee> {
    R<Employee> login(Employee employee);

    R<String> save(Employee employee, Long id);

    R<Page<Employee>> getPage(int currentPage, int pageSize, String name);

}
