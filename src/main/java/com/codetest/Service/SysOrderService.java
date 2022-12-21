package com.codetest.Service;

import com.codetest.Entity.Order;
import com.codetest.Mapper.SysOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName:SysOrderService
 * @Auther: yyj
 * @Description:
 * @Date: 20/12/2022 18:05
 * @Version: v1.0
 */
@Service
public class SysOrderService {

    @Autowired
    SysOrderMapper orderMapper;


    public int insertOrder(Order order) {
        return orderMapper.insertOrder(order);
    }

    public void insertOrder_item(HashMap<String,Object> map) {
         orderMapper.insertOrder_item(map);
    }

    public void insertOrder_item_list(List<HashMap> list) {
        orderMapper.insertOrder_item_list(list);

    }

    public void deleteOrder_item(int order_id) {
        orderMapper.deleteOrder_item(order_id);

    }

    public void updateOrder(HashMap<String, Object> map) {
        orderMapper.updateOrder(map);
    }
}
