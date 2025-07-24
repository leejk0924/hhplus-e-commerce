package kr.hhplus.be.server.user.presentation.dto;

import kr.hhplus.be.server.user.application.dto.BalanceDto;

public record UserResponse(Integer point) {
    public static UserResponse from(BalanceDto balanceDto) {
        return new UserResponse(balanceDto.balance());
    }
}
