package kr.hhplus.be.server.user.domain;

import kr.hhplus.be.server.exception.InvalidPointAmountException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BalancePoint {
    @Getter
    private Integer balance;
    public static final int MAX_POINT = 10_000_000;
    public static final int MIN_POINT = 0;
    public void isValid() {
        if (this.balance == null) {
            this.balance = 0;
        }
        if (this.balance < MIN_POINT || this.balance > MAX_POINT) {
            throw new InvalidPointAmountException();
        }

    }
}
