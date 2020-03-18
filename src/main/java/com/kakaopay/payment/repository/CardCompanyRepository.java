package com.kakaopay.payment.repository;

import com.kakaopay.payment.model.CardCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardCompanyRepository extends JpaRepository<CardCompany, String> {
}
