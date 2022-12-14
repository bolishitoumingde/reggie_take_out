package com.example.take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.take_out.controller.utils.R;
import com.example.take_out.entity.Employee;
import com.example.take_out.mapper.EmployeeMapper;
import com.example.take_out.service.IEmployeeService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employee 员工信息
     * @return 员工信息
     */
    @Override
    public R<Employee> login(Employee employee) {
        // 1.将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(password);

        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        // 搜索用户名
        lqw.eq(Employee::getUsername, employee.getUsername());
        // 返回该用户名的对象
        Employee emp = employeeMapper.selectOne(lqw);
        if (emp == null) {
            // 未查询到该用户
            return R.error("用户名不存在");
        } else {
            if (!emp.getPassword().equals(employee.getPassword())) {
                return R.error("账号或密码错误");
            } else if (emp.getStatus() == 0) {
                return R.error("该账号已被禁用");
            } else {
                return R.success(emp);
            }
        }
    }

    /**
     * 新增员工
     *
     * @param employee 员工信息
     * @param id       修改人id
     * @return 成功
     */
    @Override
    public R<String> save(Employee employee, Long id) {
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        // 用于判断该用户名是否存在
        Employee emp = employeeMapper.selectOne(lqw);
        if (emp != null) {
            return R.error("用户名已存在");
        }
        // 设置默认密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeMapper.insert(employee);
        return R.success("添加成功");
    }

    /**
     * 分页
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param name        员工姓名
     */
    @Override
    public R<Page<Employee>> getPage(int currentPage, int pageSize, String name) {
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        Page<Employee> page = new Page<>(currentPage, pageSize);
        lqw.like(Strings.isNotEmpty(name), Employee::getName, name);
        lqw.orderByDesc(Employee::getUpdateTime);
        employeeMapper.selectPage(page, lqw);
        return R.success(page);
    }
}
