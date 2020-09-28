// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.service;

import io.dolittle.lunch.web.Util;
import io.dolittle.lunch.web.model.Lunch;
import io.dolittle.lunch.web.model.Order;
import io.dolittle.lunch.web.model.OrderType;
import io.dolittle.lunch.web.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class OrderService {


    private final OrderRepository orderRepository;
    private final LunchService lunchService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository, LunchService lunchService, MongoTemplate mongoTemplate) {

        this.orderRepository = orderRepository;
        this.lunchService = lunchService;
        this.mongoTemplate = mongoTemplate;
    }

    public void placeOrder(Order order) {
        Lunch lunch4Today = lunchService.getLunch4Today();
        if (lunch4Today.hasLunchBeenOrdered()) {
            // Throw some shit about 'you're too late' - ref Pedro :)
            return;
        }

        Order userOrder;

        if (order.getOrderType().equals(OrderType.GUEST)) {
            userOrder = null;
        } else {
            userOrder = getUserOrder(order.getUser());
        }


        if (userOrder == null) {
            order.setLunchId(lunch4Today.getId());
            Order savedOrder = saveOrder(order);
            lunch4Today.addOrder(savedOrder);
            lunchService.saveLunch(lunch4Today);
            return;
        }
        userOrder.setRequest( order.getRequest());
        saveOrder(userOrder);

    }

    public void cancelOrder(String orderId) {
        Order guestOrder = getGuestOrder(orderId);
        deleteOrder(guestOrder);
        lunchService.removeOrder(guestOrder);
    }

    private Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    private void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    private Order getUserOrder(String user) {
        return mongoTemplate.findOne(Query.query(Criteria.where("lunchId").is(Util.getDateAsString()).
                andOperator(Criteria.where("user").is(user))), Order.class);
    }

    private Order getGuestOrder(String orderId) {
        return mongoTemplate.findById(orderId, Order.class);
    }
}
