package kr.hhplus.be.server.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRequest {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ChargePointDto {
        private int point;
    }
}
