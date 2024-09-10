package com.yaroslav.dragontmsbackend.model.testcase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCasePreConditionDTO {

    private int index;
    private String action;
    private String expectedResult;

}
