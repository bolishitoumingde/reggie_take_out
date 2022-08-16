package com.example.take_out.cotroller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.Employee;
import com.example.take_out.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    /**
     * 用户登录
     *
     * @param employee 员工对象，包含用户名密码
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        R<Employee> res = employeeService.login(employee);
        if (res.getCode() == 1) {
            request.getSession().setAttribute("employee", res.getData().getId());
        }
        return res;
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee 员工信息
     * @return 成功与否
     */
    @PostMapping
    public R<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        Long id = (Long) request.getSession().getAttribute("employee");
        return employeeService.save(employee, id);
    }


    /**
     * 分页
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @param name        员工姓名
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(@RequestParam("page") int currentPage,
                                  @RequestParam("pageSize") int pageSize,
                                  String name) {
        log.info("当前页：{}，页面大小：{}，用户姓名：{}", currentPage, pageSize, name);
        return employeeService.getPage(currentPage, pageSize, name);
    }

    /**
     * 修改员工信息
     */
    @PutMapping
    public R<String> updateEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        Long id = (Long) request.getSession().getAttribute("employee");
        System.out.println(employee.getId());
        employee.setUpdateUser(id);
        if (employeeService.updateById(employee)) {
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }

    /**
     * 根据id获取用户信息
     *
     * @param id 用户id
     * @return R对象
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee emp = employeeService.getById(id);
        return R.success(emp);
    }


}
