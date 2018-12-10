package site.ownw.authserver.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import site.ownw.authserver.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 工具类
 * @author sofior
 * @date 2018/9/5 16:05
 */
public abstract class ApplicationUtils {

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isEmpty(ip)) {
            ip = "unknown";
        }
        return ip;
    }

    public static Optional<User> getLoginUserWithOutException() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return Optional.empty();
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return Optional.empty();
        }
        if (principal instanceof User) {
            return Optional.of((User) principal);
        }
        return Optional.empty();
    }

    public static User getLoginUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            throw new AccessDeniedException("请先登陆");
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("请先登陆");
        }
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            throw new AccessDeniedException("请先登陆");
        }
        if (principal instanceof User) {
            return (User) principal;
        }
        throw new AccessDeniedException("请先登陆");
    }

}
