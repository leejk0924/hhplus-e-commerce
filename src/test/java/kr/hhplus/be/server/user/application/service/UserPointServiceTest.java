package kr.hhplus.be.server.user.application.service;

import kr.hhplus.be.server.user.application.dto.PointDto;
import kr.hhplus.be.server.user.application.repository.UserPointRepository;
import kr.hhplus.be.server.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserPointServiceTest {
    @InjectMocks
    private UserPointService sut;
    @Mock
    private UserPointRepository userPointRepository;

    @ParameterizedTest(name = "point={0} -> expected={1}")
    @ValueSource(ints =  {0, 10_000_000, 9_999_999})
    void 포인트_조회_서비스_테스트(Integer amount) throws Exception {
        // Given
        Long userId = 1L;
        User balancePoint = User.of(userId, "test", amount);
        given(userPointRepository.loadPoint(anyLong())).willReturn(balancePoint);

        // When
        PointDto result = sut.loadPoint(userId);

        // Then
        verify(userPointRepository).loadPoint(anyLong());
        assertThat(result.balance()).isEqualTo(amount);
    }
    @DisplayName("[Service:단위테스트] : 포인트 충전 테스트")
    @Test
    void 포인트_충전_서비스_테스트() throws Exception {
        // Given
        Long userId = 1L;
        int initPoint = 1;
        int chargePoint = 1;
        int expectedPoint = initPoint + chargePoint;
        User user = User.of(userId, "test", initPoint);
        given(userPointRepository.loadPoint(anyLong())).willReturn(user);

        // When
        PointDto result = sut.chargePoint(userId, chargePoint);

        // Then
        verify(userPointRepository).loadPoint(anyLong());
        assertThat(result.balance()).isEqualTo(expectedPoint);
    }
}