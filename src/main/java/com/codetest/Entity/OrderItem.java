package com.codetest.Entity;

import lombok.Data;

/**
 * @author birdyyoung
 * @ClassName:ItemDetail
 * @Description:
 * @Date: 20/12/2022 18:14
 * @Version: v1.0
 */
@Data
public class OrderItem {
    private int item_id;
    private int order_id;
    private double unitPrice;
    private int quantity;
    private String name;
    private String modified_time;
    private String create_time;
}
