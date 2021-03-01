package com.kakaopay.moneyswagger.account.model;

import com.kakaopay.moneyswagger.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByIdAndMember(Long id, Member member);
}
