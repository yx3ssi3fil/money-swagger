package com.kakaopay.moneyswagger.service;

import com.kakaopay.moneyswagger.dto.CreateMoneySwaggingDto;
import com.kakaopay.moneyswagger.entity.account.MoneyPortion;
import com.kakaopay.moneyswagger.entity.account.MoneySwagging;
import com.kakaopay.moneyswagger.entity.chat.ChatRoom;
import com.kakaopay.moneyswagger.entity.member.Member;
import com.kakaopay.moneyswagger.repository.MoneyPortionRepository;
import com.kakaopay.moneyswagger.repository.MoneySwaggingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MoneySwaggingService {
    private static final Integer ONE = 1;
    private static final Integer THREE_DIGITS = 3;

    private final MemberService memberService;
    private final ChatRoomService chatRoomService;

    private final MoneySwaggingRepository moneySwaggingRepository;
    private final MoneyPortionRepository moneyPortionRepository;

    public MoneySwagging createMoneySwagging(MoneySwagging moneySwagging) {
        String token = RandomStringUtils.randomAlphabetic(THREE_DIGITS);
        moneySwagging.assignToken(token);
        List<MoneyPortion> allMoneyPortion = createAllMoneyPortion(moneySwagging);
        moneySwagging.assignMoneyPortions(allMoneyPortion);

        return moneySwaggingRepository.save(moneySwagging);
    }

    public MoneySwagging buildMakeSwagging(String memberId, String chatRoomId, CreateMoneySwaggingDto.Request request) {
        try {
            Long giverId = Long.valueOf(memberId);
            Member giver = memberService.retrieveMemberById(giverId).get();
            ChatRoom chatRoom = chatRoomService.retrieveById(chatRoomId).get();
            return makeMoneySwagging(request, giver, chatRoom);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private MoneySwagging makeMoneySwagging(CreateMoneySwaggingDto.Request request, Member giver, ChatRoom chatRoom) {
        return MoneySwagging.builder()
                .amount(request.getAmount())
                .member(giver)
                .chatRoom(chatRoom)
                .peopleCount(request.getPeopleCount())
                .build();
    }

    private List<MoneyPortion> createAllMoneyPortion(MoneySwagging moneySwagging) {
        Integer amount = moneySwagging.getAmount();
        Integer peopleCount = moneySwagging.getPeopleCount();
        List<MoneyPortion> moneyPortions = makeMoneyPortionFrom(amount, peopleCount, moneySwagging);

        return moneyPortionRepository.saveAll(moneyPortions);
    }

    private List<MoneyPortion> makeMoneyPortionFrom(Integer amount, Integer peopleCount, MoneySwagging moneySwagging) {
        int base = amount / peopleCount;
        int bonus = amount % peopleCount;

        List<MoneyPortion> moneyPortions = IntStream.range(ONE, peopleCount)
                .mapToObj(num -> new MoneyPortion(moneySwagging, base))
                .collect(Collectors.toList());
        moneyPortions.add(new MoneyPortion(moneySwagging, base + bonus));

        return moneyPortions;
    }
}
