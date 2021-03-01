package com.kakaopay.moneyswagger.moneyswagging;

import com.kakaopay.moneyswagger.moneyswagging.dto.CreateMoneySwaggingDto;
import com.kakaopay.moneyswagger.moneyswagging.dto.MoneyAcceptanceDto;
import com.kakaopay.moneyswagger.moneyswagging.dto.RetrieveMoneySwaggingDto;
import com.kakaopay.moneyswagger.moneyswagging.model.Header;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneyPortion;
import com.kakaopay.moneyswagger.moneyswagging.model.MoneySwagging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MoneySwaggingController {
    public static final String URL_CREATE_MONEY_SWAGGING = "/money-swaggings";
    public static final String URL_RETRIEVE_MONEY_SWAGGING = "/moeny-swaggings";
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
    public ResponseEntity<RetrieveMoneySwaggingDto.Response> retrieveByToken(HttpServletRequest request,
                                                                             @RequestParam(name = "token") String token) {

        Optional<MoneySwagging> optionalMoneySwagging
                = moneySwaggingService.retrieveByGiverAndToken(getHeader(Header.USER_ID, request), token);
        if (optionalMoneySwagging.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        MoneySwagging moneySwagging = optionalMoneySwagging.get();
        if (moneySwagging.isExpiredToRetrieve()) {
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
        Optional<MoneySwagging> optionalMoneySwagging = moneySwaggingService.retrieveByChatRoomAndToken(request.getChatRoomId(), request.getToken());
        if (optionalMoneySwagging.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        MoneySwagging moneySwagging = optionalMoneySwagging.get();
        if (isNotAuthorizedToAccept(getHeader(Header.USER_ID, httpServletRequest), moneySwagging)) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Long memberId = Long.valueOf(getHeader(Header.USER_ID, httpServletRequest));
        List<MoneyPortion> moneyPortions = moneySwagging.getAvailableMoneyPortions(memberId);
        if (isNotAvailableToAccept(moneySwagging, moneyPortions)) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        MoneyPortion moneyPortion = moneySwaggingService.acceptMoney(moneySwagging, moneyPortions, memberId);
        MoneyAcceptanceDto.Response responseBody = new MoneyAcceptanceDto.Response(moneyPortion.getAmount());
        return ResponseEntity
                .ok(responseBody);
    }

    private Boolean isNotAvailableToAccept(MoneySwagging moneySwagging, List<MoneyPortion> moneyPortions) {
        return moneyPortions.isEmpty() || moneySwagging.isExpiredToAcceptMoney();
    }

    private Boolean isNotAuthorizedToAccept(String userId, MoneySwagging moneySwagging) {
        try {
            Long memberId = Long.valueOf(userId);
            return moneySwagging.isGiver(memberId) || !moneySwagging.isChatRoomMember(memberId);
        } catch (NumberFormatException e) {
            return false;
        }
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
