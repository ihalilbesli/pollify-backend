package com.pollify.pollify.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("🔍 [SecurityUtil] Authentication nesnesi: " + authentication);

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            System.out.println("🧾 [SecurityUtil] Principal: " + principal);

            if (principal instanceof com.pollify.pollify.model.User user) {
                System.out.println("✅ [SecurityUtil] Kullanıcı ID'si: " + user.getId());
                return user.getId();
            } else {
                System.out.println("⚠️ [SecurityUtil] Principal beklenen User tipi değil: " + principal.getClass());
            }
        }

        System.out.println("❌ [SecurityUtil] Kullanıcı kimliği alınamadı.");
        return null;
    }

    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("🔍 [SecurityUtil] Rol kontrolü - authentication: " + authentication);

        if (authentication != null && authentication.getAuthorities() != null) {
            return authentication.getAuthorities().stream()
                    .findFirst()
                    .map(grantedAuthority -> {
                        System.out.println("🧾 [SecurityUtil] Kullanıcı rolü: " + grantedAuthority.getAuthority());
                        return grantedAuthority.getAuthority();
                    })
                    .orElse(null);
        }

        System.out.println("❌ [SecurityUtil] Rol bulunamadı.");
        return null;
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean result = authentication != null && authentication.isAuthenticated();
        System.out.println("🔐 [SecurityUtil] Kullanıcı oturumda mı? " + result);
        return result;
    }
}
