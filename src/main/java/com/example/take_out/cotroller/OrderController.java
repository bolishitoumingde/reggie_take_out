package com.example.take_out.cotroller;


import com.example.take_out.cotroller.utils.R;
import com.example.take_out.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submitOrder() {
        return null;
    }
}
