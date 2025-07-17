package kr.hhplus.be.server.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserResponse {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CheckPointDto {
        private int point;
        public CheckPointDto(int point) {
            this.point = point;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ChargePointDto {
        private String message;
        private int AvailablePoint;

        public ChargePointDto(String message, int point) {
            this.message = message;
            this.AvailablePoint = point;
        }
    }
}
