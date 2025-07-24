package kr.hhplus.be.server.user.presentation.controller;

import kr.hhplus.be.server.exception.UserNotFoundException;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.User;
import kr.hhplus.be.server.user.infrastructure.persistence.jpa.UsersEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserPointControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UsersEntityRepository usersEntityRepository;

    @Test
    void 포인트_조회_성공_케이스() throws Exception {
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
}