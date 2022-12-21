# codetestgradleversion

# yejiaYang-CodingTest

## API screenshots

### Create order  

Two test cases

[<img src="https://s1.ax1x.com/2022/12/21/zXiGKH.png" alt="zXiGKH.png" style="zoom:50%;" />](https://imgse.com/i/zXiGKH)



### Update Order 

[<img src="https://s1.ax1x.com/2022/12/21/zXiJrd.png" alt="zXiJrd.png" style="zoom:50%;" />](https://imgse.com/i/zXiJrd)

## **Database Design**

[<img src="https://s1.ax1x.com/2022/12/21/zXiNVI.png" alt="zXiNVI.png" style="zoom:50%;" />](https://imgse.com/i/zXiNVI)

## My Logic

### ⭐️ Create an order

When somebody creates a new order, the program should calculate the total amount and insert a new recode to the `Order  ` table, when the operation is successful, return the unique key(auto increment). then insert items into the `order_item` table.

I provide two methods to insert multiple records,

-  one by one 

```java
for(HashMap<String,Object> map :batchList ){
    map.put("order_id",order_id);
    orderService.insertOrder_item(map);
}
```

-  batch insert

```java
  orderService.insertOrder_item_list(batchList);
```



### ⭐️ Update an order

When somebody wants to update an order and its related items, my logic is that calculates the new total amount and deletes all the items where order_id (foreign key) is equal to the order's id. And repeat the operation of creation, that is insert one by one or batch insert.

## Common Code Info

Method `getItemsList` is to encapsulate List information and calculates amounts. I need to unify the format of records as a HashMap.

## High precision calculations

```java
BigDecimal totalAmount = new BigDecimal(0);
List<JSONObject> batchList = new ArrayList<>();
for(int i =0;i<jsonArray.length();i++){
    BigDecimal unitPrice = obj.getBigDecimal("unitPrice")
            .setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal quantity = obj.getBigDecimal("quantity");
    totalAmount = totalAmount.add(unitPrice.multiply(quantity));
}
```

` .setScale` :  keep *2 decimal* places

`BigDecimal.ROUND_HALF_UP` : round up 



## GitHub Link

【Maven】https://github.com/Amber916Young/codeTest

【Gradle】https://github.com/Amber916Young/codetestgradleversion

**if Maven version can not run successfully, please try Gradle version and vice versa.**

