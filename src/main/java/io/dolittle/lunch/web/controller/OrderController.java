// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.controller;

import io.dolittle.lunch.web.Util;
import io.dolittle.lunch.web.dto.OrderDTO;
import io.dolittle.lunch.web.dto.UserDTO;
import io.dolittle.lunch.web.model.Order;
import io.dolittle.lunch.web.model.OrderType;
import io.dolittle.lunch.web.service.OrderService;
import io.dolittle.lunch.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


    @PostMapping(value = "/user")
    public ResponseEntity<String> placeUserOrder(@RequestBody OrderDTO orderDTO) {
        UserDTO userDTO = userService.getUser();
        log.info("Order received: Name: {}, Request{} from User:{}", orderDTO.getUserName(), orderDTO.getAllergy(), userDTO.getName());

        Order order = new Order();
        order.setUser(userDTO.getName());
        order.setRequest(orderDTO.getAllergy());
        order.setOrderType(OrderType.USER);

        orderService.placeOrder(order);

        return ResponseEntity.ok("{\"result\":\"ok\"}");
    }

    @PostMapping(value = "/guest")
    public ResponseEntity<String> placeGuestOrder(@RequestBody OrderDTO orderDTO) {
        UserDTO userDTO = userService.getUser();
        log.info("Order received: Name: {}, Request{} from User:{}", orderDTO.getUserName(), orderDTO.getAllergy(), userDTO.getName());

        Order order = new Order();
        order.setUser(orderDTO.getUserName());
        order.setRequest(orderDTO.getAllergy());
        order.setOrderType(OrderType.GUEST);

        orderService.placeOrder(order);

        return ResponseEntity.ok("{\"result\":\"ok\"}");
    }

    @PostMapping(value = "/cancel")
    public ResponseEntity<String> cancelOrder(@RequestBody OrderDTO orderDTO) {
        UserDTO userDTO = userService.getUser();
        if (!userDTO.getIsManager()) {
            return ResponseEntity.badRequest().body("{\"result\":\"Only managers can cancel a request\"}");
        }
        log.info("Cancel order received: ID: {}, from User:{}", orderDTO.getId(), userDTO.getName());
        orderService.cancelOrder(orderDTO.getId());

        return ResponseEntity.ok("{\"result\":\"ok\"}");
    }
}
