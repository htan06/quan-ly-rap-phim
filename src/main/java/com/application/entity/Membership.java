package com.application.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Membership extends BaseEntity<Long> {

    private String name;
    private String phoneNumber;
    private int memberPoint;
}
