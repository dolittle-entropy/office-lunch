// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.service;

import io.dolittle.lunch.web.Util;
import io.dolittle.lunch.web.model.Lunch;
import io.dolittle.lunch.web.model.Order;
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

        Order employeeOrder = getEmployeeOrder(order.getEmployee());

        if (employeeOrder == null) {
            order.setLunchId(lunch4Today.getId());
            Order savedOrder = saveOrder(order);
            lunch4Today.addOrder(savedOrder);
            lunchService.saveLunch(lunch4Today);
            return;
        }
        employeeOrder.setRequest( order.getRequest());
        saveOrder(employeeOrder);

    }

    private Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    private Order getEmployeeOrder(String employee) {
        return mongoTemplate.findOne(Query.query(Criteria.where("lunchId").is(Util.getDateAsString()).
                andOperator(Criteria.where("employee").is(employee))), Order.class);
    }
}
