package com.enigmacamp.loanapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InstalmentTypeRequest {
    private String id;
    private String instalmentType;
    private String role;
}
