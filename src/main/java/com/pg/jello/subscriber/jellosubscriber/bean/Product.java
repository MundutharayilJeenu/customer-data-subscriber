package com.pg.jello.subscriber.jellosubscriber.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String name;
    private double price;
    private Category category;

}
