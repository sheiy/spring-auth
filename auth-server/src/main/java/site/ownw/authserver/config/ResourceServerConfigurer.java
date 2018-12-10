package site.ownw.authserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author sofior
 * @date 2018/10/30 17:29
 */
@Configuration
@EnableResourceServer
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    private final ResourceServerProperties resourceServerProperties;
    private final TokenStore tokenStore;
    private final OAuth2WebSecurityExpressionHandler expressionHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceServerProperties.getResourceId())
                .expressionHandler(expressionHandler)
                .stateless(true)
                .tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**").authorizeRequests().anyRequest()
                .access("@RBAC.clientHasPermission(request,authentication)");
    }

}
