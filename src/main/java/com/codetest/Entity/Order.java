package com.codetest.Entity;

import lombok.Data;

/**
 * @author birdyyoung
 * @ClassName:Order
 * @Description:
 * @Date: 20/12/2022 17:30
 * @Version: v1.0
 */
@Data
public class Order {
    private int id;
    private double totalAmount;
    private String modified_time;
    private String create_time;
}
