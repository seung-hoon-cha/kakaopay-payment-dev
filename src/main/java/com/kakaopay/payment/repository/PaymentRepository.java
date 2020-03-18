package com.kakaopay.payment.repository;

import com.kakaopay.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    public Payment findByManagementNumber(String managementNumber);

    public List<Payment> findAllByOriginManagementNumber(String managementNumber);

    @Query(value = "SELECT * FROM PAYMENT WHERE management_number = ?1 OR origin_management_number = ?1 ORDER BY create_time_at desc LIMIT 1", nativeQuery = true)
    public Payment findByLatestPayment(String managementNumber);

    @Query(value = "SELECT sum(amount) FROM PAYMENT WHERE origin_management_number = ?1 GROUP BY origin_management_number", nativeQuery = true)
    public Integer findByManagementNumberGroupByAmountSum(String managementNumber);

    @Query(value = "SELECT sum(value_added_tax) FROM PAYMENT WHERE origin_management_number = ?1 GROUP BY origin_management_number", nativeQuery = true)
    public Integer findByManagementNumberGroupByTaxSum(String managementNumber);
}
