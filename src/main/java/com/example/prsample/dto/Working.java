package com.example.prsample.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Working {

    private int empno;
    private String name;
    private String ischul;
    private String isworking;
    private String cometime;
    private String gotime;
    private Date lastday;
}
