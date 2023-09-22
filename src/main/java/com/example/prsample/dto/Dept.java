package com.example.prsample.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dept {
    private int empno;
    private int deptno;
    private String isworking;
    private String location;
    private String deptname;
    private String name;
}
