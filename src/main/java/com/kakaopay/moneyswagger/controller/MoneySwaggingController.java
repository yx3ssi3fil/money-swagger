package com.kakaopay.moneyswagger.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MoneySwaggingController {
    public static final String URL_CREATE_MONEY_SWAGGING = "/money-swaggings";
}
