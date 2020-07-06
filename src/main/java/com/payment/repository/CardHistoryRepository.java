package com.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payment.entity.CardHistory;

@Repository
public interface CardHistoryRepository extends JpaRepository<CardHistory, Long> {

}
