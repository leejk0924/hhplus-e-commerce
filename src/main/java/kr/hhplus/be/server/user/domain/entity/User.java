package kr.hhplus.be.server.user.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTime;
import kr.hhplus.be.server.exception.ErrorCode;
import kr.hhplus.be.server.exception.RestApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static kr.hhplus.be.server.user.exception.UserErrorCode.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity(name = "USERS")
public class User extends BaseTime {
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "USER_NAME", length = 50, nullable = false)
    private String username;
    @Column(name = "POINT_BALANCE", columnDefinition = "INT UNSIGNED")
    private Integer pointBalance;

    public Integer hasBalanced() {
        return this.pointBalance == null ? 0 : this.pointBalance;
    }

    private static final int MAX_POINT = 10_000_000;
    private static final int MIN_POINT = 0;

    public static User of(Long id, String username, Integer balance) {
        balance = (balance == null) ? 0 : balance;
        validPointRange(balance, INVALID_POINT);
        return new User(id, username, balance);
    }

    public void chargePoint(int amount) {
        validChargeRange(amount, INVALID_CHARGE_POINT);
        this.pointBalance += amount;
        if (this.pointBalance > MAX_POINT) {
            throw new RestApiException(EXCEEDED_POINT);
        }
    }
    // 메서드를 나눈 이유 : 1. 의미론적 명확성을 위해 , 2. 미래의 변경 가능성 : 미래에 정책이 변경 될 수 있으므로 (보유 포인트와 충전 포인트의 정책이 별도로 변경 가능하므로)
    // 보유 포인트의 유효 범위를 확인하는 의미의 메서드
    private static void validPointRange(int amount, ErrorCode errorCode) {
        if (amount < MIN_POINT || amount > MAX_POINT) {
            throw new RestApiException(errorCode);
        }
    }
    // 충전하는 금액 자체가 유효한 범위인지 확인하는 의미의 메서드
    private static void validChargeRange(int amount, ErrorCode errorCode) {
        if (amount < MIN_POINT || amount > MAX_POINT) {
            throw new RestApiException(errorCode);
        }
    }
    public void withdrawPoint(int amount) {
        if (amount > this.pointBalance) {
            throw new RestApiException(INSUFFICIENT_BALANCE);
        }
        this.pointBalance -= amount;
    }
}
