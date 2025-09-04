package kr.hhplus.be.server.user.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.hhplus.be.server.user.domain.entity.User;

public interface UsersEntityRepository extends JpaRepository<User, Long> {
}
