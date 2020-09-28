// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
@Document
public class Order {

    @Id
    private String id;

    private String lunchId;
    private String user;
    private List<String> request = new ArrayList<>();

    private OrderType orderType;

}
