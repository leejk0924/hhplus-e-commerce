package kr.hhplus.be.server.user.presentation.controller;

import kr.hhplus.be.server.user.presentation.controller.spec.UserApiSpec;
import kr.hhplus.be.server.user.presentation.dto.UserRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import kr.hhplus.be.server.user.application.dto.PointDto;
import kr.hhplus.be.server.user.application.service.UserPointService;
import kr.hhplus.be.server.user.presentation.dto.UserResponse;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserPointController implements UserApiSpec {
    private final UserPointService userPointService;
    @GetMapping("/users/{userId}/points")
    public ResponseEntity<UserResponse.CheckPointDto> checkPoint(@PathVariable Long userId) {
        PointDto pointDto = userPointService.loadPoint(userId);
        return ResponseEntity.ok(UserResponse.CheckPointDto.from(pointDto));
    }
    @PostMapping("/users/{userId}/points")
    public ResponseEntity<UserResponse.ChargePointDto> chargePoint(
            @PathVariable Long userId,
            @RequestBody UserRequest.ChargePointDto chargePointDto
    ) {
        PointDto pointDto = userPointService.chargePoint(userId, chargePointDto.point());
        return ResponseEntity.ok(UserResponse.ChargePointDto.from(pointDto));
    }
}