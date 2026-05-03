package com.application.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class Membership extends BaseEntity<UUID> {

    private String name;
    private String phoneNumber;
    private int memberPoint;
}
