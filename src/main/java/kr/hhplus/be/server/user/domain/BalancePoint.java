package kr.hhplus.be.server.user.domain;

import kr.hhplus.be.server.exception.ExceededPointBalanceException;
import kr.hhplus.be.server.exception.InvalidChargeAmountException;
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
        validPoint(this.balance, new InvalidPointAmountException());
    }
    public void chargePoint(int amount) {
        validPoint(amount, new InvalidChargeAmountException());
        this.balance += amount;
        if (this.balance > MAX_POINT) {
            throw new ExceededPointBalanceException();
        }
    }
    private static void validPoint(int amount, RuntimeException exception) {
        if (amount < MIN_POINT || amount > MAX_POINT) {
            throw exception;
        }
    }
}
