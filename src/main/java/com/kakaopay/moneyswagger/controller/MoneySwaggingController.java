package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.dto.CreateMoneySwaggingDto;
import com.kakaopay.moneyswagger.dto.RetrieveMoneySwaggingDto;
import com.kakaopay.moneyswagger.entity.Header;
import com.kakaopay.moneyswagger.entity.account.MoneyPortion;
import com.kakaopay.moneyswagger.entity.account.MoneySwagging;
import com.kakaopay.moneyswagger.service.MoneySwaggingService;
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
    public static final String URL_RETRIEVE_MONEY_SWAGGING = "/moeny-swaggings/{id]";

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
    public ResponseEntity<RetrieveMoneySwaggingDto.Response> retrieveByToken(@RequestParam(name = "token") String token) {
        Optional<MoneySwagging> optionalMoneySwagging = moneySwaggingService.retrieveByToken(token);
        if (optionalMoneySwagging.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        MoneySwagging moneySwagging = optionalMoneySwagging.get();
        RetrieveMoneySwaggingDto.Response responseBody = RetrieveMoneySwaggingDto.Response.from(moneySwagging);
        return ResponseEntity
                .ok(responseBody);
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
