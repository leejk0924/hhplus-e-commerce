package kr.hhplus.be.server.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.dto.UserRequest;
import kr.hhplus.be.server.domain.dto.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Tag(name = "유저", description = "유저 관련 API")
public interface UserApiSpec {
    @Operation(summary = "포인트 조회")
    ResponseEntity<UserResponse.CheckPointDto> checkPoint(Long userId);

    @Operation(summary = "포인트 충전")
    ResponseEntity<UserResponse.ChargePointDto> chargePoint (
            Long userId,
            UserRequest.ChargePointDto checkPointDto
    );
}
