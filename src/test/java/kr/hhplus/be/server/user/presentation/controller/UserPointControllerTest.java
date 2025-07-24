package kr.hhplus.be.server.user.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.User;
import kr.hhplus.be.server.user.infrastructure.persistence.jpa.UsersEntityRepository;
import kr.hhplus.be.server.user.presentation.dto.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserPointControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UsersEntityRepository usersEntityRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @DisplayName("[Controller:통합테스트] : 포인트 조회 테스트")
    @Test
    void 포인트_조회_성공_테스트() throws Exception {
        // Given
        long userId = 1L;
        int point = 1000;
        User user = User.builder().pointBalance(point).username("testName").build();
        given(usersEntityRepository.findById(userId)).willReturn(Optional.of(user));

        // When && Then
        mockMvc.perform(
                        get("/users/{id}/points", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(point));
    }
    @DisplayName("[Controller:통합테스트] : 포인트 저장 테스트")
    @Test
    void 포인트_저장_성공_테스트() throws Exception {
        // Given
        Long userId = 1L;
        int initPoint = 1000;
        int chargePoint = 1000;
        String userName = "testName";
        User initUser = User.builder().pointBalance(initPoint).username(userName).build();
        User savedUser = User.builder().pointBalance(initPoint).username(userName).build();

        UserRequest.ChargePointDto request = new UserRequest.ChargePointDto(chargePoint);
        String requestJson = objectMapper.writeValueAsString(request);

        given(usersEntityRepository.findById(anyLong())).willReturn(Optional.of(initUser));
        given(usersEntityRepository.save(any(User.class))).willReturn(savedUser);

        // When && Then
        mockMvc.perform(
                post("/users/{id}/points", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(chargePoint));
    }
}