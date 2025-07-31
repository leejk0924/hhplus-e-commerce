package kr.hhplus.be.server.coupon.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.application.dto.CouponDto;
import kr.hhplus.be.server.coupon.application.facade.CouponFacade;
import kr.hhplus.be.server.coupon.domain.entity.Coupon;
import kr.hhplus.be.server.coupon.domain.entity.UserCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
@AutoConfigureMockMvc
class CouponControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CouponFacade couponFacade;
    @Autowired
    private ObjectMapper objectMapper;
    @DisplayName("[Controller:단위테스트] : 쿠폰 발행 성공 테스트")
    @Test
    void 쿠폰_발행_성공_테스트() throws Exception {
        // Given
        Coupon coupon = Coupon.of(1L, "쿠폰명", "고정", 1000, 100);
        UserCoupon issuedCoupon = UserCoupon.of(1L, coupon, "발급", null, LocalDateTime.now().plusDays(30));
        CouponDto couponDto = CouponDto.from(coupon, issuedCoupon);
        given(couponFacade.issuedCoupon(1L, 1L)).willReturn(couponDto);

        // When && Then
        mockMvc.perform(
                        get("/api/coupon/{couponId}/first-come/users/{userId}", 1L, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couponId").value(coupon.getId()))
                .andExpect(jsonPath("$.couponName").value(coupon.getCouponName()))
                .andExpect(jsonPath("$.discountType").value(coupon.getDiscountType()))
                .andExpect(jsonPath("$.discountRate").value(coupon.getDiscountRate()))
                .andExpect(jsonPath("$.expiredAt").value(couponDto.expiredAt().toString()));
    }
}