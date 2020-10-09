// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.service;

import io.dolittle.lunch.web.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final Environment env;
    private Set<String> managers;

    @Autowired
    public UserService(Environment environment) {
        this.env = environment;
    }

    @PostConstruct
    public void setManagers() {
        String managersFromEnv = env.getProperty("dolittle.lunch.manager");
        List<String> managerList = Arrays.asList(managersFromEnv.split(","));
        this.managers = new HashSet<>(managerList);

    }

    public UserDTO getUser() {
        DefaultOidcUser oidcUser = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String given_name = oidcUser.getAttribute("given_name");
        String upn = oidcUser.getAttribute("upn");
        String name = oidcUser.getAttribute("name");
        String family_name = oidcUser.getAttribute("family_name");
        String unique_name = oidcUser.getAttribute("unique_name");

        log.info("upn:{}, name:{}, gn:{}, fn:{}, un:{}", upn, name, given_name, family_name, unique_name);
        return new UserDTO(name,
                given_name,
                family_name,
                upn, isManager(upn));

    }

    private Boolean isManager(String givenName) {
        return this.managers.contains(givenName);

    }

}
