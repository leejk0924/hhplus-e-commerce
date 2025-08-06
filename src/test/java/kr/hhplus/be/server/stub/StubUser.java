package kr.hhplus.be.server.stub;

import kr.hhplus.be.server.user.domain.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

public class StubUser extends User {
    public StubUser(Long id, String username, Integer pointBalance) {
        super();
        ReflectionTestUtils.setField(this, "id", id);
        ReflectionTestUtils.setField(this, "username", username);
        ReflectionTestUtils.setField(this, "pointBalance", pointBalance);
    }
    public static StubUser of(Long id, String username, Integer balance) {
        return new StubUser(id, username , balance);
    }
}
