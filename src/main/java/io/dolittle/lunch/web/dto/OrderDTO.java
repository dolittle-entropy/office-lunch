// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private String id;
    private String userName;
    private List<String> allergy;
}
