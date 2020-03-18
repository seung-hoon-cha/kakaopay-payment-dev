package com.kakaopay.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "card_company")
public class CardCompany {
    // 관리번호
    @Id
    @Column(name = "management_number", nullable = false)
    private String managementNumber;
    // 약속된 string
    @Column(name = "telegram", columnDefinition = "TEXT", nullable = false)
    private String telegram;
    // 생성 일시
    @CreationTimestamp
    @Column(name = "create_time_at", nullable = false)
    private LocalDateTime createTimeAt;

    public CardCompany(String managementNumber, String length, String telegram) {
        this.managementNumber = managementNumber;
        this.telegram = String.format("%s%s", length, telegram);
    }
}
