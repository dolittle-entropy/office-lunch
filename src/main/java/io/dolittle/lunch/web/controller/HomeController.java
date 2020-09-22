// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.controller;

import io.dolittle.lunch.web.Util;
import io.dolittle.lunch.web.dto.UserDTO;
import io.dolittle.lunch.web.service.LunchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HomeController {


    private final LunchService lunchService;

    @Autowired
    public HomeController(LunchService lunchService) {
        this.lunchService = lunchService;
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
            return Util.getUser((DefaultOidcUser) authentication.getPrincipal());
        }
        return null;
    }

    @ModelAttribute("allergies")
    public List<String> getAllergies() {
        List<String> allergies = new ArrayList<>();
        allergies.add("Vegan");
        allergies.add("Vegetarian");
        allergies.add("Nut allergy");
        allergies.add("Seafood allergy");

        return allergies;
    }



}
