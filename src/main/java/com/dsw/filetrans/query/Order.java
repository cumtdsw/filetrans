package com.dsw.filetrans.query;

/**
 * 排序字段
 */
public class Order{

    /** 字段名称 */
    private String name;

    /** 字段排序类型 */
    private OrderType type;
    
    public Order()
    {}
    
    public Order(String name, OrderType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }
}
