// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Slf4j
public class Util {

    public static String dateFormatPattern = "ddMMyy";

    public static String getDateAsString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatPattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Paris")));

        String date = simpleDateFormat.format(new Date());
        log.debug("Today's date: {}", date);
        return date;
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now().atZone(ZoneId.of("Europe/Paris")).toLocalDateTime();
    }

    public static String getDateAsString(LocalDateTime localDateTime) {
        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Paris")));

        return simpleDateFormat.format(java.util.Date.from(localDateTime.toInstant(ZoneOffset.UTC)));
    }

    public static Date getDateFromString(String dateAsString) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatPattern);
        return simpleDateFormat.parse(dateAsString);
    }

    public static String getMonthYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String month = new SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendar.getTime());
        String year = new SimpleDateFormat("yyyy").format(calendar.getTime());
        return month + " - " + year;

    }

    public static Date resetDayToFirst(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
}
