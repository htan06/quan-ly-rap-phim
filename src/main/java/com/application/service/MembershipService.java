package com.application.service;

import com.application.entity.Membership;

import java.util.List;

public interface MembershipService {

    Long create(Membership membership);

    List<Membership> findAll();

    Membership findById(Long id);

    Membership findByPhone(String phoneNumber);

    void updateInfo(Membership membership);

    void updatePoint(Long id, int point);
}
