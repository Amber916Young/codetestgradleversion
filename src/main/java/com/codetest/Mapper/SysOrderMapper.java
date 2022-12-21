package com.codetest.Mapper;

import com.codetest.Entity.Order;
import org.apache.ibatis.annotations.Delete;

import java.util.HashMap;
import java.util.List;

public interface SysOrderMapper {


    void insertOrder_item(HashMap<String,Object> map);

    int insertOrder(Order order);

    void insertOrder_item_list(List<HashMap> list);

    void updateOrder(HashMap<String, Object> map);

    @Delete("delete from codeTest.order_item  where order_id = #{order_id}")
    void deleteOrder_item(int order_id);

}
