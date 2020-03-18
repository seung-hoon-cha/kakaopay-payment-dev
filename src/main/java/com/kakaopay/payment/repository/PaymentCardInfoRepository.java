package com.kakaopay.payment.repository;

import com.kakaopay.payment.model.PaymentCardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCardInfoRepository extends JpaRepository<PaymentCardInfo, Long> {
}
