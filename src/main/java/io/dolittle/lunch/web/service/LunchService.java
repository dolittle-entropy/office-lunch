// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.service;

import io.dolittle.lunch.web.Util;
import io.dolittle.lunch.web.model.Lunch;
import io.dolittle.lunch.web.repository.LunchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class LunchService {


    private final LunchRepository lunchRepository;

    @Autowired
    public LunchService(LunchRepository lunchRepository) {
        this.lunchRepository = lunchRepository;
    }

    public Lunch getLunch4Today() {
        String dateAsString = Util.getDateAsString();
        Optional<Lunch> findLunch = lunchRepository.findById(dateAsString);

        if (findLunch.isPresent()) {
            log.info("Found doc with id: {}", dateAsString);
            return findLunch.get();
        }

        Lunch lunch = new Lunch();
        lunch.setId(dateAsString);
        lunch.setOrderDate(LocalDateTime.now());
        lunch.setStatus(false);
        saveLunch(lunch);

        return lunch;
    }

    public void saveLunch(Lunch lunch) {
        lunchRepository.save(lunch);
    }

    public void updateLunchStatus() {
        Lunch lunch4Today = getLunch4Today();
        lunch4Today.setStatus(true);
        saveLunch(lunch4Today);
    }
}
