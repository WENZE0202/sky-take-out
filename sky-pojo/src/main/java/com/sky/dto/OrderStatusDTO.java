package com.sky.dto;

import com.sky.dto.OrdersRejectionDTO;
import com.sky.enumeration.OrderStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatusDTO extends OrdersRejectionDTO implements Serializable {
     // Order status
     private OrderStatus orderStatus;
}
