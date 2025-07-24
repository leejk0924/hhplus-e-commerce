package kr.hhplus.be.server.user.presentation.controller;

import kr.hhplus.be.server.user.presentation.dto.UserRequest;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping("/users/{userId}/points")
    public ResponseEntity<UserResponse> chargePoint(
            @PathVariable Long userId,
            @RequestBody UserRequest.ChargePointDto chargePointDto
    ) {
        BalanceDto balanceDto = userPointService.chargePoint(userId, chargePointDto.point());
        return ResponseEntity.ok(UserResponse.from(balanceDto));
    }
}
