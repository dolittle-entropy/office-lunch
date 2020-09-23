// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web;

import io.dolittle.lunch.web.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class Util {

    public static String getDateAsString() {
        String pattern = "ddMMyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());
        log.debug("Today's date: {}", date);
        return date;
    }

    public static UserDTO getUser() {
        DefaultOidcUser oidcUser = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new UserDTO(oidcUser.getAttribute("name"),
                oidcUser.getAttribute("given_name"),
                oidcUser.getAttribute("family_name"),
                oidcUser.getAttribute("upn"));

    }
}
