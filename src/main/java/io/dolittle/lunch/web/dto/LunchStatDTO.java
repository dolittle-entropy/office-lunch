// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class LunchStatDTO implements Comparable<LunchStatDTO> {
    private String user;
    private Integer lunch = 0;
    private CategoryDTO category;

    public void incrementLunch() {
        lunch ++;
    }

    @Override
    public int compareTo(LunchStatDTO o) {
        return user.compareTo(o.getUser());
    }
}
