package com.codetest.Controller;

import com.codetest.Entity.Order;
import com.codetest.Service.SysOrderService;
import com.codetest.utils.HttpResult;
import com.codetest.utils.JsonUtils;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName:OrderController
 * @author: yejia yang
 * @Description:
 * @Date: 20/12/2022 17:14
 * @Version: v1.0
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private SysOrderService orderService;

    @SneakyThrows
    @ResponseBody
    @PostMapping("/create")
    public HttpResult CreateOrder(@RequestBody String jsonData) {
        jsonData = URLDecoder.decode(jsonData, "utf-8").replaceAll("=", "");
        JSONObject object =  new JSONObject(jsonData);
        if(!object.has("items") ){
            HttpResult.error("Parameter [item] doesn't exist, please check again");
        }

        HashMap<String,Object> reuslt = getItemsList(object,-1);
        BigDecimal totalAmount = new BigDecimal(reuslt.get("totalAmount").toString());
        List<HashMap> batchList = JsonUtils.jsonToList(reuslt.get("batchList").toString(),HashMap.class);

        Order order = new Order();
        order.setTotalAmount(totalAmount.doubleValue());
        int isSuccessful = orderService.insertOrder(order);
        if(isSuccessful == 0){
            HttpResult.error("Insertion failure");
        }
        int order_id = order.getId();

        /**
         * there are two methods to insert order items into DB
         */
        // =========== 1, insert one by one start ===========
        for(HashMap<String,Object> map : batchList ) {
            map.put("order_id", order_id);
            orderService.insertOrder_item(map);
        }
        // =========== end ===========


        // =========== 2, foreach batch insert start ===========
//        for(HashMap map :batchList ){
//            map.put("order_id",order_id);
//        }
//        orderService.insertOrder_item_list(batchList);
        //2=========== end ===========



        for(HashMap<String,Object> map :batchList ){
            map.remove("order_id");
        }

        // return useful message
        HashMap<String , Object> returnMap = new HashMap<>();
        returnMap.put("id",order_id);
        returnMap.put("totalAmount",totalAmount.toString());
        returnMap.put("items",batchList);
        return HttpResult.ok("insert successfully",returnMap);
    }


    @SneakyThrows
    @ResponseBody
    @PostMapping("/update")
    public HttpResult UpdateOrder(@RequestBody String jsonData) {
        jsonData = URLDecoder.decode(jsonData, "utf-8").replaceAll("=", "");
        JSONObject object = new JSONObject(jsonData);
        int order_id = object.getInt("id");
        if(!object.has("items") ){
            HttpResult.error("Parameter [item] doesn't exist, please check again");
        }
        HashMap<String,Object> reuslt = getItemsList(object,order_id);
        BigDecimal totalAmount = new BigDecimal(reuslt.get("totalAmount").toString());
        List<HashMap> batchList = JsonUtils.jsonToList(reuslt.get("batchList").toString(),HashMap.class);

       // update order
        HashMap<String ,Object> updateParam = new HashMap<>();
        updateParam.put("totalAmount",totalAmount);
        updateParam.put("id",order_id);
        orderService.updateOrder(updateParam);

        // delete the original item record(s)
        orderService.deleteOrder_item(order_id);

        // (1) insert new records, is the same logical of the create operation
        for(HashMap<String,Object> map :batchList ){
            map.put("order_id",order_id);
            orderService.insertOrder_item(map);
        }
        // (2) or
//        orderService.insertOrder_item_list(batchList);

        for(HashMap<String,Object> map :batchList ){
            map.remove("order_id");
        }
        // return msg
        HashMap<String , Object> returnMap = new HashMap<>();
        returnMap.put("id",order_id);
        returnMap.put("totalAmount",totalAmount.toString());
        returnMap.put("items",batchList);
        return HttpResult.ok("update successfully",returnMap);
    }


    private HashMap<String,Object> getItemsList( JSONObject object,int order_id){
        JSONArray jsonArray = object.getJSONArray("items");
        BigDecimal totalAmount = new BigDecimal(0);
        List<JSONObject> batchList = new ArrayList<>();
        for(int i =0;i<jsonArray.length();i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            if(!obj.has("name") ||!obj.has("unitPrice") || !obj.has("quantity") ){
                HttpResult.error("Parameter error, please check again");
            }
            String name = obj.getString("name");
            BigDecimal unitPrice = obj.getBigDecimal("unitPrice")
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal quantity = obj.getBigDecimal("quantity");
            totalAmount = totalAmount.add(unitPrice.multiply(quantity));

            HashMap<String ,Object> objectHashMap = new HashMap<>();
            objectHashMap.put("name",name);
            objectHashMap.put("quantity",quantity.intValue());
            objectHashMap.put("unitPrice",unitPrice.toString());
            if (order_id != -1){
                objectHashMap.put("order_id",order_id);
            }
            batchList.add(new JSONObject(objectHashMap));
        }

        HashMap<String,Object>  returnMap = new HashMap<>();
        returnMap.put("batchList",batchList);
        returnMap.put("totalAmount",totalAmount.toString());
        return returnMap;
    }

}
