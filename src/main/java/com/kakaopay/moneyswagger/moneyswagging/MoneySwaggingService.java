package com.kakaopay.moneyswagger.moneyswagging;

import com.kakaopay.moneyswagger.account.AccountService;
import com.kakaopay.moneyswagger.chatroom.ChatRoomService;
import com.kakaopay.moneyswagger.moneyswagging.dto.CreateMoneySwaggingDto;
import com.kakaopay.moneyswagger.account.model.TransferRole;
import com.kakaopay.moneyswagger.account.model.Account;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneyPortion;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneySwagging;
import com.kakaopay.moneyswagger.chatroom.model.ChatRoom;
import com.kakaopay.moneyswagger.member.model.Member;
import com.kakaopay.moneyswagger.member.MemberService;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneyPortionRepository;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneySwaggingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final AccountService accountService;

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

    public Optional<MoneySwagging> retrieveByToken(String token) {
        return moneySwaggingRepository.findByToken(token);
    }

    public Boolean isChatRoomMember(Long userId, MoneySwagging moneySwagging) {
        ChatRoom chatRoom = moneySwagging.getChatRoom();
        return chatRoom.getMembers()
                .stream()
                .anyMatch(member -> userId == member.getId());
    }

    public synchronized MoneyPortion acceptMoney(MoneySwagging moneySwagging, List<MoneyPortion> moneyPortions, Long userId) {
        MoneyPortion availableMoneyPortion = moneyPortions.get(0);

        Member moneySwagger = moneySwagging.getMember();
        Account giver = moneySwagger.getMajorAccount();
        Member user = memberService.retrieveMemberById(userId).get();
        Account receiver = user.getMajorAccount();

        availableMoneyPortion.assignReceiver(user);
        Integer amount = availableMoneyPortion.getAmount();

        Map<TransferRole, Account> transfer = accountService.transfer(giver, receiver, amount);

        return availableMoneyPortion;
    }

    public List<MoneyPortion> getAvailableMoneyPortions(MoneySwagging moneySwagging, Long userId) {
        List<MoneyPortion> moneyPortions = moneySwagging.getMoneyPortions();

        if (isAgainAcceptance(moneyPortions, userId)) {
            return Collections.emptyList();
        }

        return moneyPortions.stream()
                .filter(moneyPortion -> !moneyPortion.isReceived())
                .collect(Collectors.toList());
    }

    private Boolean isAgainAcceptance(List<MoneyPortion> moneyPortions, Long userId) {
        return moneyPortions.stream()
                .filter(MoneyPortion::isReceived)
                .anyMatch(moneyPortion -> moneyPortion.getReceiver().getId().equals(userId));

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