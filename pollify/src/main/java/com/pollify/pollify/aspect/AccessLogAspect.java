package com.pollify.pollify.aspect;

import com.pollify.pollify.model.AccessLog;
import com.pollify.pollify.model.User;
import com.pollify.pollify.repository.UserRepository;
import com.pollify.pollify.servis.AccessLogService;
import com.pollify.pollify.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
public class AccessLogAspect {

    private final AccessLogService accessLogService;
    private final UserRepository userRepository;

    public AccessLogAspect(AccessLogService accessLogService, UserRepository userRepository) {
        this.accessLogService = accessLogService;
        this.userRepository = userRepository;
    }

    @Pointcut("execution(public * com.pollify.pollify.controller..*(..))")
    public void controllerMethods() {}

    @AfterReturning("controllerMethods()")
    public void logSuccess(JoinPoint joinPoint) {
        saveLog(joinPoint, "BAŞARILI", null);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logError(JoinPoint joinPoint, Throwable ex) {
        saveLog(joinPoint, "HATA", ex.getMessage());
    }

    private void saveLog(JoinPoint joinPoint, String status, String errorMessage) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return;

        HttpServletRequest request = attributes.getRequest();
        String method = request.getMethod();
        String endpoint = request.getRequestURI();
        String entity = extractEntityName(joinPoint);
        String actionType = extractActionType(method);

        String userEmail = "Anonim";
        String role = "GIRIS_YAPMAMIS";

        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId != null) {
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    userEmail = user.getEmail();
                    role = user.getRole().name();
                }
            }
        } catch (Exception e) {
            // Giriş yapmamış kullanıcı için default değerler kullanılacak
        }

        AccessLog log = AccessLog.builder()
                .timestamp(LocalDateTime.now())
                .userEmail(userEmail)
                .role(role)
                .endpoint(endpoint)
                .method(method)
                .entity(entity)
                .actionType(actionType)
                .status(status)
                .errorMessage(errorMessage)
                .build();

        accessLogService.saveLog(log); // ❗ Eski kayıtları silmiyoruz artık
    }

    private String extractEntityName(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        return className.replace("Controller", "");
    }

    private String extractActionType(String method) {
        return switch (method.toUpperCase()) {
            case "GET" -> "READ";
            case "POST" -> "CREATE";
            case "PUT", "PATCH" -> "UPDATE";
            case "DELETE" -> "DELETE";
            default -> "OTHER";
        };
    }
}
