package com.mjpascual.fileguard.model;

import lombok.Data;

@Data
public class SchemaOptions {
    private boolean allowEmptyRows = false;
    private boolean allowDuplicates = false;
}
