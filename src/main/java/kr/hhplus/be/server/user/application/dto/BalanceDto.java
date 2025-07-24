package kr.hhplus.be.server.user.application.dto;

import kr.hhplus.be.server.user.domain.BalancePoint;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.User;

public record BalanceDto(Integer balance) {
    public static BalanceDto toDto(BalancePoint balancePoint) {
        return new BalanceDto(balancePoint.getBalance());
    }
    public User toEntity (String username, BalanceDto balanceDto) {
        return new User(username, balanceDto.balance());
    }
}
