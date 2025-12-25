package com.mjpascual.fileguard.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationError {
    private int row;
    private String column;
    private String message;
}
