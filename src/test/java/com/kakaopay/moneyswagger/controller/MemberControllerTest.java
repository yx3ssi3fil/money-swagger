package com.kakaopay.moneyswagger.controller;

import com.kakaopay.moneyswagger.AbstractControllerTest;
import com.kakaopay.moneyswagger.dto.CreateMemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberControllerTest extends AbstractControllerTest {
    private MemberHttpTest memberHttpTest;

    @BeforeEach
    public void setUp() {
        this.memberHttpTest = new MemberHttpTest(webTestClient);
    }

    @DisplayName("회원가입")
    @Test
    void signUp() {
        //given
        String name = "name";

        //when
        CreateMemberDto.Response responseBody = memberHttpTest.createMember(name);

        //then
        assertThat(responseBody.getId()).isNotNull().isNotNegative();
        assertThat(responseBody.getName()).isEqualTo("name");
    }
}
