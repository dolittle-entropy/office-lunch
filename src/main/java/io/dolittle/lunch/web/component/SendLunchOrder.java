// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.component;

import io.dolittle.lunch.web.model.Lunch;
import io.dolittle.lunch.web.model.Order;
import io.dolittle.lunch.web.model.OrderItem;
import io.dolittle.lunch.web.service.EmailService;
import io.dolittle.lunch.web.service.LunchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.*;

@Component
@Slf4j
public class SendLunchOrder {

    private final EmailService emailService;
    private final LunchService lunchService;

    @Autowired
    public SendLunchOrder(EmailService emailService, LunchService lunchService) {
        this.emailService = emailService;
        this.lunchService = lunchService;
    }

    @Scheduled(cron = "0 30 17 ? * MON-FRI", zone = "CET")
    public void sendLunchOrder() {
        log.debug("Sending lunch order email.");
        Lunch lunch4Today = lunchService.getLunch4Today();

        if (lunch4Today.hasLunchBeenOrdered()) {
            log.debug("Lunch order email already sent.");
            return;
        }

        Set<Order> orderList = lunch4Today.getOrderList();
        if (orderList.isEmpty()) {
            //No lunch orders today, tag as true and return.
            lunchService.updateLunchStatus();
            return;
        }


        Map<String, OrderItem> orderItems = new HashMap<>();
        for (Order order : orderList) {
            String key = String.join(", ", order.getRequest());
            if (orderItems.containsKey(key)) {
                orderItems.get(key).updateAmount();

            } else {
                OrderItem orderItem = new OrderItem();
                orderItem.updateAmount();
                orderItem.setRequests(key);
                orderItems.put(key, orderItem);
            }
        }

        List<OrderItem> items = new ArrayList<>(orderItems.values());
        Map<String, Object> listMap = new HashMap<>();
        listMap.put("orders", items);
        listMap.put("totalLunch", orderList.size());

        try {
            emailService.sendEmail(listMap);
        } catch (MessagingException e) {
            log.error("Unable to send email!");
            e.printStackTrace();
        }

    }
}
