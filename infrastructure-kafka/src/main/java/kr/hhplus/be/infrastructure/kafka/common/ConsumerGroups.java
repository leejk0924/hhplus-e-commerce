package kr.hhplus.be.infrastructure.kafka.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConsumerGroups {
    COUPON_ISSUE(Topics.COUPON_ISSUE, "coupon_issue_group");
    private final Topics topic;
    private final String groupId;
}
