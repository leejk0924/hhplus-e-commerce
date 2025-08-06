package kr.hhplus.be.server.order.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.application.facade.OrderFacade;
import kr.hhplus.be.server.order.application.facade.PaymentFacade;
import kr.hhplus.be.server.order.presentation.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(controllers = OrderController.class)
//@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @InjectMocks
    private OrderController sut;
    private MockMvc mockMvc;
    @Mock
    private OrderFacade orderFacade;
    @Mock
    private PaymentFacade paymentFacade;
    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(sut)
                .build();
    }

    @DisplayName("[Controller:단위테스트] : 주문 성공 테스트")
    @Test
    void 주문_성공_테스트() throws Exception {
        // Given
        OrderRequest.OrderItemDto productDto1 = new OrderRequest.OrderItemDto(1L, 10);
        OrderRequest.OrderItemDto productDto2 = new OrderRequest.OrderItemDto(2L, 20);
        List<OrderRequest.OrderItemDto> productDtos = List.of(productDto1, productDto2);
        OrderRequest.OrderDto orderDto = new OrderRequest.OrderDto(1L, productDtos);
        String requestJson = objectMapper.writeValueAsString(orderDto);

        // When && Then
        mockMvc.perform(
                        post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문이 완료되었습니다."));
    }
    @DisplayName("[Controller:단위테스트] : 결제 성공 테스트")
    @Test
    void 결제_성공_테스트() throws Exception {
        // Given
        OrderRequest.ProcessPaymentDto paymentDto = OrderRequest.ProcessPaymentDto.of(1L, 1L, 1L);
        String requestJson = objectMapper.writeValueAsString(paymentDto);

        // When && Then
        mockMvc.perform(
                        post("/api/orders/payments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("결제가 완료되었습니다."));
    }
}