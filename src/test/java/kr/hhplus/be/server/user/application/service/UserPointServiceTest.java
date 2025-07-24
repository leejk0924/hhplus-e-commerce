package kr.hhplus.be.server.user.application.service;

import kr.hhplus.be.server.user.application.dto.BalanceDto;
import kr.hhplus.be.server.user.application.repository.UserPointRepository;

import kr.hhplus.be.server.user.domain.BalancePoint;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
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
        BalancePoint balancePoint = new BalancePoint(amount);
        given(userPointRepository.loadPoint(anyLong())).willReturn(balancePoint);

        // When
        BalanceDto result = sut.loadPoint(userId);

        // Then
        verify(userPointRepository).loadPoint(anyLong());
        assertThat(result.balance()).isEqualTo(amount);
    }
}