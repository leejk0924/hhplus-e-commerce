package kr.hhplus.be.server.user.application.dto;

import kr.hhplus.be.server.user.domain.entity.User;

public record PointDto(Integer balance) {
    public static PointDto toDto(User user) {
        return new PointDto(user.hasBalanced());
    }
}
