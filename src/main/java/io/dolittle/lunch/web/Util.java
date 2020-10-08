// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class Util {

    public static String getDateAsString() {
        String pattern = "ddMMyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Paris")));

        String date = simpleDateFormat.format(new Date());
        log.debug("Today's date: {}", date);
        return date;
    }

}
