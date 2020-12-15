// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.controller;

import io.dolittle.lunch.web.dto.CategoryDTO;
import io.dolittle.lunch.web.dto.LunchStatDTO;
import io.dolittle.lunch.web.dto.UserDTO;
import io.dolittle.lunch.web.service.StatsService;
import io.dolittle.lunch.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class StatisticController {

    private final StatsService statsService;
    private final UserService userService;

    @Autowired
    public StatisticController(StatsService statsService, UserService userService) {
        this.statsService = statsService;
        this.userService = userService;
    }

    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    public String stats(Model model) {
        Map<CategoryDTO, List<LunchStatDTO>> ordersByMonth;
        try {
            UserDTO user = userService.getUser();
            if (user.getIsManager()) {
                ordersByMonth = statsService.getOrdersByMonth();
            } else {
                ordersByMonth = statsService.getOrdersByMonth(user.getName());
            }

            model.addAttribute("lunchStats", ordersByMonth);
            log.info("Got model");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "stats";
    }
}
