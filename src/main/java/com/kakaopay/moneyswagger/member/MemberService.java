package com.kakaopay.moneyswagger.member;

import com.kakaopay.moneyswagger.member.model.Member;
import com.kakaopay.moneyswagger.member.model.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Optional<Member> retrieveMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
