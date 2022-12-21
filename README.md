# yejiaYang-CodingTest

## API screenshots

### Create order  

Two test cases

[<img src="https://s1.ax1x.com/2022/12/21/zXiGKH.png" alt="zXiGKH.png" style="zoom:50%;" />](https://imgse.com/i/zXiGKH)



### Update Order 

[<img src="https://s1.ax1x.com/2022/12/21/zXiJrd.png" alt="zXiJrd.png" style="zoom:50%;" />](https://imgse.com/i/zXiJrd)

## **Database Design**

[<img src="https://s1.ax1x.com/2022/12/21/zXiNVI.png" alt="zXiNVI.png" style="zoom:50%;" />](https://imgse.com/i/zXiNVI)

## Connect to DB

```yaml
server:
  port: 8089
  session-timeout: 60 # session
  tomcat:
    uri-encoding: utf-8 # Setting up tomcat encoding
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/codeTest?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&useSSL=false&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true
    username: root
    password: Yyj188706
  mvc:
    static-path-pattern: /static/**
  web:
    resources:
      static-locations: classpath:/templates/,classpath:/static/image/,classpath:/static/,classpath:/mapper/

mybatis:
  mapper-locations: classpath:static/mapper/*.xml # The path where the configuration mapper file is located
  type-aliases-package: com.codetest.Entity # Configure all class aliases under this package
```

The server port is 8089, which can be defined by anyone

I have uploaded the SQL file, here is the content:

```mysql
/*
 Navicat Premium Data Transfer

 Source Server         : yyjblog
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : codeTest

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 21/12/2022 15:10:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `totalAmount` decimal(10,2) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `item_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `unitPrice` decimal(10,2) DEFAULT '0.00',
  `quantity` int DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`item_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;

```

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
