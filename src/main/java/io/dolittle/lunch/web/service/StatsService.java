// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.service;

import io.dolittle.lunch.web.dto.CategoryDTO;
import io.dolittle.lunch.web.dto.LunchStatDTO;
import io.dolittle.lunch.web.model.Order;
import io.dolittle.lunch.web.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static io.dolittle.lunch.web.Util.*;
import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
public class StatsService {

    private final OrderRepository orderRepository;

    @Autowired
    public StatsService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    private List<Order> getAllOrdersByUser(String user) {
        return orderRepository.findByUser(user);
    }

    public Map<CategoryDTO, List<LunchStatDTO>> getOrdersByMonth() throws ParseException {
        List<Order> allOrders = getAllOrders();

        return getOrderStats(allOrders);

    }

    private LinkedHashMap<CategoryDTO, List<LunchStatDTO>> getOrderStats(List<Order> allOrders) throws ParseException {
        Map<String, LunchStatDTO> userOrderList = new HashMap<>();

        for (Order order : allOrders) {

            String lunchId = order.getLunchId();
            Date lunchDate = getDateFromString(lunchId);
            String title = getMonthYear(lunchDate);

            String user = order.getUser();
            String searchKey = user + " - " + title;

            if (userOrderList.containsKey(searchKey)) {
                LunchStatDTO lunchStatDTO = userOrderList.get(searchKey);
                lunchStatDTO.incrementLunch();
            } else {
                LunchStatDTO lunchStatDTO = new LunchStatDTO();
                lunchStatDTO.setUser(user);
                lunchStatDTO.incrementLunch();
                Date sortDate = resetDayToFirst(lunchDate);
                lunchStatDTO.setCategory(new CategoryDTO(title, sortDate));
                userOrderList.put(searchKey, lunchStatDTO);
            }

        }

        List<LunchStatDTO> lunchStatDTOList = new ArrayList<>(userOrderList.values());

        Map<CategoryDTO, List<LunchStatDTO>> listMap = lunchStatDTOList.stream().sorted().collect(groupingBy(LunchStatDTO::getCategory));
        return listMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public Map<CategoryDTO, List<LunchStatDTO>> getOrdersByMonth(String user) throws ParseException {
        List<Order> allOrdersByUser = getAllOrdersByUser(user);
        return getOrderStats(allOrdersByUser);

    }
}
