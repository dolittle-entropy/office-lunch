// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.controller;

import io.dolittle.lunch.web.Util;
import io.dolittle.lunch.web.component.SendLunchOrder;
import io.dolittle.lunch.web.dto.UserDTO;
import io.dolittle.lunch.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class EmailController {

    private final SendLunchOrder sendLunchOrder;
    private final UserService userService;

    @Autowired
    public EmailController(SendLunchOrder sendLunchOrder, UserService userService) {
        this.sendLunchOrder = sendLunchOrder;
        this.userService = userService;
    }

    @GetMapping(value = "/email")
    public String sendTestEmail() {
        log.info("Sending test email");
        UserDTO userDTO = userService.getUser();

        String name = userDTO.getName();

        if (name != null && name.equals("Retna Ramachandran")) {
            sendLunchOrder.sendLunchOrder();
        }
        return "redirect:/";
    }
}
