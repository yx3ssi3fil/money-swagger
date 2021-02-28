package com.kakaopay.moneyswagger.moneyswagging;

import com.kakaopay.moneyswagger.moneyswagging.dto.CreateMoneySwaggingDto;
import com.kakaopay.moneyswagger.moneyswagging.dto.MoneyAcceptanceDto;
import com.kakaopay.moneyswagger.moneyswagging.dto.RetrieveMoneySwaggingDto;
import com.kakaopay.moneyswagger.moneyswagging.model.Header;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneyPortion;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneySwagging;
import com.kakaopay.moneyswagger.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MoneySwaggingController {
    public static final String URL_CREATE_MONEY_SWAGGING = "/money-swaggings";
    public static final String URL_RETRIEVE_MONEY_SWAGGING = "/moeny-swaggings/{id}";
    public static final String URL_ACCEPT_MONEY = "/money-swaggings/acceptances";


    private final MoneySwaggingService moneySwaggingService;

    @PostMapping(URL_CREATE_MONEY_SWAGGING)
    public ResponseEntity<CreateMoneySwaggingDto.Response> create(HttpServletRequest httpServletRequest,
                                                                  @RequestBody @Valid CreateMoneySwaggingDto.Request request) {
        MoneySwagging moneySwagging;
        try {
            moneySwagging = buildMoneySwaggingFromRequest(httpServletRequest, request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        MoneySwagging savedMoneySwagging = moneySwaggingService.createMoneySwagging(moneySwagging);
        CreateMoneySwaggingDto.Response responseBody = CreateMoneySwaggingDto.Response.from(savedMoneySwagging);

        return ResponseEntity
                .created(URI.create(URL_CREATE_MONEY_SWAGGING + "/" + savedMoneySwagging.getId()))
                .body(responseBody);
    }

    @GetMapping(URL_RETRIEVE_MONEY_SWAGGING)
    public ResponseEntity<RetrieveMoneySwaggingDto.Response> retrieveByToken(HttpServletRequest request, @RequestParam(name = "token") String token) {
        Optional<MoneySwagging> optionalMoneySwagging = moneySwaggingService.retrieveByToken(token);
        if (optionalMoneySwagging.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        MoneySwagging moneySwagging = optionalMoneySwagging.get();
        if (isAuthorizedToRetrieve(moneySwagging, request)) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        RetrieveMoneySwaggingDto.Response responseBody = RetrieveMoneySwaggingDto.Response.from(moneySwagging);
        return ResponseEntity
                .ok(responseBody);
    }

    @PostMapping(URL_ACCEPT_MONEY)
    public ResponseEntity<MoneyAcceptanceDto.Response> acceptMoney(HttpServletRequest httpServletRequest,
                                                                   @RequestBody MoneyAcceptanceDto.Request request) {
        String strUserId = getHeader(Header.USER_ID, httpServletRequest);
        String token = request.getToken();
        Optional<MoneySwagging> optionalMoneySwagging = moneySwaggingService.retrieveByToken(token);

        if (optionalMoneySwagging.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        MoneySwagging moneySwagging = optionalMoneySwagging.get();
        Long moneySwaggerId = moneySwagging.getMember().getId();
        Long userId = Long.valueOf(strUserId);

        if (moneySwaggerId == userId) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Boolean isMember = moneySwaggingService.isChatRoomMember(userId, moneySwagging);
        if (!isMember) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        if (isOverTenMinutes(moneySwagging)) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        List<MoneyPortion> moneyPortions = moneySwaggingService.getAvailableMoneyPortions(moneySwagging, userId);
        if (moneyPortions.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        MoneyPortion moneyPortion = moneySwaggingService.acceptMoney(moneySwagging, moneyPortions, userId);
        MoneyAcceptanceDto.Response responseBody = new MoneyAcceptanceDto.Response(moneyPortion.getAmount());
        return ResponseEntity
                .ok(responseBody);
    }

    private Boolean isAuthorizedToRetrieve(MoneySwagging moneySwagging, HttpServletRequest request) {
        return isGiver(moneySwagging, request) || isOverSevenDaysAgo(moneySwagging);
    }

    private Boolean isGiver(MoneySwagging moneySwagging, HttpServletRequest request) {
        try {
            String userId = getHeader(Header.USER_ID, request);
            Member moneySwagger = moneySwagging.getMember();
            return Long.valueOf(userId) != moneySwagger.getId();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Boolean isOverTenMinutes(MoneySwagging moneySwagging) {
        LocalDateTime nowInKst = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        LocalDateTime createdTimeInKst = moneySwagging.getCreatedDate().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        LocalDateTime sevenDaysAgo = nowInKst.minusMinutes(10);

        return createdTimeInKst.isBefore(sevenDaysAgo);
    }

    private Boolean isOverSevenDaysAgo(MoneySwagging moneySwagging) {
        LocalDateTime nowInKst = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        LocalDateTime createdTimeInKst = moneySwagging.getCreatedDate().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        LocalDateTime sevenDaysAgo = nowInKst.minusDays(7);

        return createdTimeInKst.isBefore(sevenDaysAgo);
    }

    private String getHeader(Header header, HttpServletRequest request) {
        return request.getHeader(header.getKey());
    }

    private MoneySwagging buildMoneySwaggingFromRequest(HttpServletRequest httpServletRequest, CreateMoneySwaggingDto.Request request) {
        return moneySwaggingService.buildMakeSwagging(
                getHeader(Header.USER_ID, httpServletRequest),
                getHeader(Header.CHAT_ROOM_ID, httpServletRequest),
                request);
    }
}
