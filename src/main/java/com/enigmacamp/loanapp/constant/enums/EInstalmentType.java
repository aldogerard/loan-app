package com.enigmacamp.loanapp.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EInstalmentType {
    ONE_MONTH(1),
    THREE_MONTHS(3),
    SIXTH_MONTHS(6),
    NINE_MONTHS(9),
    TWELVE_MONTHS(12);

    private int value;
}
