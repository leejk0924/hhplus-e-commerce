package kr.hhplus.be.server.user.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import kr.hhplus.be.server.exception.UserNotFoundException;
import kr.hhplus.be.server.user.application.repository.UserPointRepository;
import kr.hhplus.be.server.user.domain.BalancePoint;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.User;
import kr.hhplus.be.server.user.infrastructure.persistence.jpa.UsersEntityRepository;

@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {
    private final UsersEntityRepository userEntityRepository;
    @Override
    public BalancePoint loadPoint(Long userId) {
        User user = userEntityRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return new BalancePoint(user.hasBalanced());
    }
}