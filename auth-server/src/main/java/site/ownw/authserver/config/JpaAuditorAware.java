package site.ownw.authserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import site.ownw.authserver.entity.User;

import java.util.Optional;

/**
 * @author sofior
 * @date 2018/8/16 22:35
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditorAware implements AuditorAware<Long> {

    /**
     * 获取登陆用户ID，未登陆用户为0
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null
                || context.getAuthentication() == null
                || context.getAuthentication().getPrincipal() == null
                || !(context.getAuthentication().getPrincipal() instanceof User)) {
            return Optional.empty();
        }
        return Optional.ofNullable(((User) context.getAuthentication().getPrincipal()).getId());
    }
}
