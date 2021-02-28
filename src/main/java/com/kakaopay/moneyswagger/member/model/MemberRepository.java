package com.kakaopay.moneyswagger.member.model;

import com.kakaopay.moneyswagger.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
