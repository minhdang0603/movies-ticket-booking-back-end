package com.dangtm.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangtm.movie.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {}
