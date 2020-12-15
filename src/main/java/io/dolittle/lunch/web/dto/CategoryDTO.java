// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class CategoryDTO implements Comparable<CategoryDTO>{

    private String category;
    private Date sortDate;

    @Override
    public int compareTo(CategoryDTO o) {
        //Sorting in reverse order
        int i = sortDate.compareTo(o.getSortDate());
        if(i != 0) return -i;
        return i;
    }

    public String getHtmlId() {
        return category.replaceAll("\\s", "");
    }
}
