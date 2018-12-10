package site.ownw.authserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Component;

/**
 * @author sofior
 * @date 2018/10/30 11:07
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Beans {

    private final Environment environment;
    private final ApplicationContext applicationContext;
    private final Profiles dev = Profiles.of("dev");

    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler() {
        OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Bean
    public BCryptPasswordEncoder clientPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SCryptPasswordEncoder userPasswordEncoder() {
        return new SCryptPasswordEncoder();
    }

    @Bean
    public TokenStore tokenStore(RedisConnectionFactory connectionFactory) {
        if (environment.acceptsProfiles(dev)) {
            return new InMemoryTokenStore();
        }
        return new RedisTokenStore(connectionFactory);
    }
}
