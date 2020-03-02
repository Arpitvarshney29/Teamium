package com.teamium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamium.domain.SignatureRecipient;

@Repository
public interface SignatureRecipientRepository extends JpaRepository<SignatureRecipient, Long> {

}
