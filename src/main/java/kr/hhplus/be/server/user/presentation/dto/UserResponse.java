package kr.hhplus.be.server.user.presentation.dto;

import kr.hhplus.be.server.user.application.dto.PointDto;

public class UserResponse {
    public record ChargePointDto(Integer point) {

        public static ChargePointDto from(PointDto pointDto) {
            return new ChargePointDto(pointDto.balance());
        }
    }
    public record CheckPointDto(Integer point) {
        public static CheckPointDto from(PointDto pointDto) {
            return new CheckPointDto(pointDto.balance());
        }
    }
}