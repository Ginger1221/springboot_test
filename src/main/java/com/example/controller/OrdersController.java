package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dao.OrdersDao;
import com.example.dao.GoodsDao;
import com.example.dao.OrdersDao;
import com.example.domain.Goods;
import com.example.domain.Orders;
import com.example.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersDao ordersDao;
    /**
     * 获取订单表
     * @return
     */
    @GetMapping//访问方式
    public List<Orders> getAll() {
        //System.out.println(ordersService.list());
        //System.out.println("used");
        return ordersService.list();
    }

    /**
     * 根据id获取订单
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Orders getById(@PathVariable int id) {
        return ordersService.getById(id);
    }

    /**
     * 删除订单
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable int id){

        return ordersService.removeById(id);

    }

    /**
     * 保存订单
     * @param orders
     * @return
     */
    @PostMapping
    public boolean saveOrders(@RequestBody Orders orders){
        System.out.println("order save!");
        return ordersService.save(orders);
    }


    /**
     * 自动生成订单
     * @param type 订单类型
     * @return 生成的订单id
     */
    public Integer initOrders(int type) {
        //System.out.println(type);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(dateFormat.format(date));
        Orders orders = new Orders();
        orders.setOrderStartTime(dateFormat.format(date));
        orders.setOrderType(type);
        orders.setOrderStatus(type*10);
        //设置订单发起人ID 默认为11 需要后续实现
        orders.setOrderInit(11);
        ordersService.save(orders);
        return orders.getOrderId();
    }

    /**
     * 订单结束
     * @param orders 订单实体
     * @return if success
     */
    public boolean endOrders(Orders orders){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(dateFormat.format(date));
        orders.setOrderEndTime(dateFormat.format(date));
        orders.setOrderStatus(orders.getOrderStatus()+1);
        return ordersService.updateById(orders);
    }

    /**
     * 更新信息
     * @param orders 实体
     * @return if success
     */
    @PostMapping("/check")
    public boolean checkById(@RequestBody Orders orders){
        orders.setOrderStatus(orders.getOrderStatus()+1);
        System.out.println(orders);
        return ordersService.updateById(orders);
    }

    /**
     * 根据订单类型获取订单 1-入库 2-出库
     * @param orderType
     * @return
     */
    @GetMapping("/getByType")
    public List<Orders> getByType(@RequestParam Integer orderType){
        /*List<Orders> oriOrders = ordersService.list();
        List<Orders> targetOrders = null;
        for (Orders ori:oriOrders
             ) {
            if(ori.getOrderType() == orderType){
                targetOrders.add(ori);
            }
        }
        return targetOrders;*/
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("order_type",orderType);
        List<Orders> orders = ordersDao.selectList(wrapper);
        return orders;
    }

    //模糊查询
    @GetMapping("/like")
    public List<Orders> getAllList(@RequestParam Integer ordersId){
        System.out.println(ordersId);
        return ordersDao.selectOrdersId("%"+ordersId+"%");
    }
}
