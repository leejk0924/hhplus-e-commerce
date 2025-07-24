package kr.hhplus.be.server.user.application.dto;

import kr.hhplus.be.server.user.domain.BalancePoint;

public record BalanceDto(Integer balance) {
    public static BalanceDto toDto(BalancePoint balancePoint) {
        return new BalanceDto(balancePoint.getBalance());
    }
}
