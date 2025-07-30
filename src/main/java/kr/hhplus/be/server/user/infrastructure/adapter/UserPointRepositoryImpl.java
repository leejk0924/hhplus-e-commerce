package kr.hhplus.be.server.user.infrastructure.adapter;

import kr.hhplus.be.server.exception.RestApiException;
import kr.hhplus.be.server.user.application.repository.UserPointRepository;
import kr.hhplus.be.server.user.domain.entity.User;
import kr.hhplus.be.server.user.exception.UserErrorCode;
import kr.hhplus.be.server.user.infrastructure.persistence.jpa.UsersEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {
    private final UsersEntityRepository userEntityRepository;
    @Override
    public User loadPoint(Long userId) {
        return userEntityRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }
    @Override
    public User chargePoint(Long userId, int amount) {
        User user = userEntityRepository.findById(userId).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        user.chargePoint(amount);
        return user;
    }
}