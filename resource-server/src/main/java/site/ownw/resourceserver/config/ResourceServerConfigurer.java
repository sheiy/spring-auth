package site.ownw.resourceserver.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Sofior
 */
@Slf4j
@Configuration
@EnableResourceServer
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;
    private final OAuth2WebSecurityExpressionHandler expressionHandler;

    private final ResourceServerProperties resourceServerProperties;
    private final UserInfoRestTemplateFactory restTemplateFactory;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .expressionHandler(expressionHandler)
                .stateless(true)
                .tokenStore(tokenStore);

        UserInfoTokenServices userInfoTokenServices = new UserInfoTokenServices(resourceServerProperties.getUserInfoUri(), resourceServerProperties.getClientId());
        resources.tokenServices(userInfoTokenServices);

        userInfoTokenServices.setRestTemplate(this.restTemplateFactory.getUserInfoRestTemplate());
        userInfoTokenServices.setTokenType(this.resourceServerProperties.getTokenType());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().access("@RBAC.hasPermission(request,authentication)");
    }
}