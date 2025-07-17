package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.controller.spec.UserApiSpec;
import kr.hhplus.be.server.domain.dto.UserRequest;
import kr.hhplus.be.server.domain.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController implements UserApiSpec {
    @GetMapping("/users/{userId}/points")
    public ResponseEntity<UserResponse.CheckPointDto> checkPoint(@PathVariable Long userId) {
        return ResponseEntity.ok(new UserResponse.CheckPointDto(1000));
    }

    @PostMapping("/users/{userId}/points")
    public ResponseEntity<UserResponse.ChargePointDto> chargePoint(
            @PathVariable Long userId,
            @RequestBody UserRequest.ChargePointDto ChargePointDto
    ) {
        return ResponseEntity.ok(new UserResponse.ChargePointDto("충전이 완료 되었습니다.", 1000));
    }
}
