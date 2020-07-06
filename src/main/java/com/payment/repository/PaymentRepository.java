package com.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
	List<Payment> findAllByOriginalManagementNumber(String originalManagementNumber);
	List<Payment> findAllByEncryptedCardNumberAndCreateDate(String encryptedCardNumber, String createDate);
}
