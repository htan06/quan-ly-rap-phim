package com.application.service.impl;

import com.application.dao.MembershipDao;
import com.application.entity.Membership;
import com.application.service.MembershipService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipDao membershipDao;

    @Override
    public Long create(Membership membership) {
        return membershipDao.create(membership);
    }

    @Override
    public List<Membership> findAll() {
        return membershipDao.findAll();
    }

    @Override
    public Membership findById(Long id) {
        return membershipDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
    }

    @Override
    public Membership findByPhone(String phoneNumber) {
        return membershipDao.findByPhone(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
    }

    @Override
    public void updateInfo(Membership membership) {
        findById(membership.getId());

        membershipDao.updateInfo(membership);
    }

    @Override
    public void updatePoint(Long id, int point) {
        findById(id);

        membershipDao.updatePoint(id, point);
    }
}
