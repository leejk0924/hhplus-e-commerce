package kr.hhplus.be.server.user.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import kr.hhplus.be.server.user.application.dto.BalanceDto;
import kr.hhplus.be.server.user.application.service.UserPointService;
import kr.hhplus.be.server.user.presentation.dto.UserResponse;

@RestController
@RequiredArgsConstructor
public class UserPointController {
    private final UserPointService userPointService;

    @GetMapping("/users/{userId}/points")
    public ResponseEntity<UserResponse> checkPoints(@PathVariable Long userId) {
        BalanceDto balanceDto = userPointService.loadPoint(userId);
        return ResponseEntity.ok(UserResponse.from(balanceDto));
    }
}
