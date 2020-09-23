// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.controller;

import io.dolittle.lunch.web.Util;
import io.dolittle.lunch.web.dto.OrderDTO;
import io.dolittle.lunch.web.model.Order;
import io.dolittle.lunch.web.dto.UserDTO;
import io.dolittle.lunch.web.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping(value = "/order")
    public ResponseEntity<String> placeOrder(@RequestBody OrderDTO orderDTO) {
        UserDTO userDTO = Util.getUser();
        log.info("Order received: Name: {}, Request{} from User:{}", orderDTO.getEmployeeName(), orderDTO.getAllergy(), userDTO.getName());

        Order order = new Order();
        order.setEmployee(userDTO.getName());
        order.setRequest(orderDTO.getAllergy());

        orderService.placeOrder(order);

        return ResponseEntity.ok("{\"result\":\"ok\"}");
    }
}
