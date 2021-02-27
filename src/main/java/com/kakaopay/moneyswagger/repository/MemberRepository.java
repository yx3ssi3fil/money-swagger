package com.kakaopay.moneyswagger.repository;

import com.kakaopay.moneyswagger.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
