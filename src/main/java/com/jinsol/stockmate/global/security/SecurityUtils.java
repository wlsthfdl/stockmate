package com.jinsol.stockmate.global.security;

import com.jinsol.stockmate.domain.user.enums.Role;
import org.springframework.security.core.Authentication;

// 인증 정보 관련 공통 도우미 (객체 생성 없이 static 메서드로만 사용)
public final class SecurityUtils {

    private static final String ADMIN_AUTHORITY = "ROLE_" + Role.ADMIN.name();

    private SecurityUtils() {
        // 유틸 클래스라 인스턴스 생성 금지
    }

    public static boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> ADMIN_AUTHORITY.equals(a.getAuthority()));
    }
}