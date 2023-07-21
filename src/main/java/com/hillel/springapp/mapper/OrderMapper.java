package com.hillel.springapp.mapper;

import com.hillel.springapp.dto.OrderDTO;
import com.hillel.springapp.entity.Order;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO orderToOrderDTO(Order order);

    Order orderDTOToOrder(OrderDTO orderDTO);

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);
}