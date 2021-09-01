// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.controller;

import io.dolittle.lunch.web.dto.UserDTO;
import io.dolittle.lunch.web.service.LunchService;
import io.dolittle.lunch.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class HomeController {

    @Value("${dolittle.allergy.choices}")
    private String allergies;

    private final LunchService lunchService;
    private final UserService userService;

    @Autowired
    public HomeController(LunchService lunchService, UserService userService) {
        this.lunchService = lunchService;
        this.userService = userService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("lunch", lunchService.getLunch4Today());
        return "home";
    }

    @ModelAttribute("currentUser")
    public UserDTO getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()){
            log.info(authentication.getDetails().toString());
            return userService.getUser();
        }
        return null;
    }
}
