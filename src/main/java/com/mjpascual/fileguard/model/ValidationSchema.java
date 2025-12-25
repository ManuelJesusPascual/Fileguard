package com.mjpascual.fileguard.model;

import lombok.Data;

import java.util.Map;

@Data
public class ValidationSchema {
    private Map<String, ColumnRule> columns;
    private SchemaOptions options;
}
