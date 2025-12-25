package com.mjpascual.fileguard.model;

import lombok.Data;

@Data
public class ColumnRule {
    private boolean required;
    private String type; // string, number, integer, email
    private Integer min;
    private Integer max;
}
