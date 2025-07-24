package kr.hhplus.be.server.user.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name = "USERS")
@NoArgsConstructor
public class User extends BaseTime {
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "USER_NAME", length = 50, nullable = false)
    private String username;
    @Column(name = "POINT_BALANCE", columnDefinition = "INT UNSIGNED")
    private Integer pointBalance;

    @Builder
    public User(String username, Integer pointBalance) {
        this.pointBalance = pointBalance;
        this.username = username;
    }

    public Integer hasBalanced() {
        return this.pointBalance == null ? 0 : this.pointBalance;
    }
}
