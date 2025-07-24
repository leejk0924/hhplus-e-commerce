package kr.hhplus.be.server.user.infrastructure.persistence.repository;

import kr.hhplus.be.server.exception.UserErrorCode;
import kr.hhplus.be.server.exception.UserNotFoundException;
import kr.hhplus.be.server.user.application.repository.UserPointRepository;
import kr.hhplus.be.server.user.domain.BalancePoint;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.User;
import kr.hhplus.be.server.user.infrastructure.persistence.jpa.UsersEntityRepository;
import kr.hhplus.be.server.user.infrastructure.adapter.UserPointRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserPointRepositoryImplTest {
    private UserPointRepository sut;
    @Mock
    private UsersEntityRepository usersEntityRepository;

    @BeforeEach
    void setUp() {
        sut = new UserPointRepositoryImpl(usersEntityRepository);
    }

    @DisplayName("[RepositoryImpl:단위테스트] : 저장되지 않은 유저의 예외 테스트")
    @Test
    void 저장되지_않은_유저의_ID_예외_케이스() throws Exception {
        // Given
        long userId = 1L;
        given(usersEntityRepository.findById(userId)).willReturn(Optional.empty());

        // When && Then
        Assertions.assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> sut.loadPoint(userId))
                .withMessage(UserErrorCode.USER_NOT_FOUND.getMessage());
    }
    @DisplayName("[RepositoryImpl:단위테스트] : 포인트 조회 테스트")
    @Test
    void 포인트_조회_성공_테스트() throws Exception {
        // Given
        Long userId = 1L;
        int initPoint = 1;
        User user = User.builder()
                .username("test")
                .pointBalance(initPoint)
                .build();
        given(usersEntityRepository.findById(userId)).willReturn(Optional.of(user));

        // When
        BalancePoint balancePoint = sut.loadPoint(userId);

        // Then
        assertThat(balancePoint.getBalance()).isEqualTo(initPoint);
    }
    @DisplayName("[RepositoryImpl:단위테스트] : 포인트 저장 테스트")
    @Test
    void 포인트_저장_성공_테스트() throws Exception {
        // Given
        Long userId = 1L;
        int initPoint = 1;
        int chargePoint = 1;
        int expectedPoint = initPoint + chargePoint;
        User user = User.builder()
                .username("test")
                .pointBalance(initPoint)
                .build();
        given(usersEntityRepository.findById(userId)).willReturn(Optional.of(user));
        given(usersEntityRepository.save(user)).willReturn(user);

        // When
        BalancePoint balancePoint = sut.savePoint(userId, chargePoint);

        // Then
        assertThat(balancePoint.getBalance()).isEqualTo(expectedPoint);
    }
}