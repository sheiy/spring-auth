package site.ownw.authserver.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ownw.authserver.entity.Client;
import site.ownw.authserver.entity.Scope;
import site.ownw.authserver.entity.User;
import site.ownw.authserver.service.ClientService;
import site.ownw.authserver.service.ScopeService;
import site.ownw.authserver.service.UserService;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * @author sofior
 * @date 2018/11/20 14:42
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final ClientService clientService;
    private final ScopeService scopeService;
    private final UserService userService;

    @GetMapping("/user")
    public Object principal(Principal principal) {
        var result = new LinkedHashMap<String, Object>();
        //考虑加缓存，此接口访问频率会比较高
        User user = (User) userService.loadUserByUsername(principal.getName());
        result.put("name", principal.getName());
        result.put("principal", user);
        result.put("authorities", user.getRoles());
        if (principal instanceof OAuth2Authentication) {
            Client client = (Client) clientService.loadClientByClientId(((OAuth2Authentication) principal).getOAuth2Request().getClientId());
            var clientItem = new LinkedHashMap<String, Object>();
            clientItem.put("scope", client.getScopes());
            clientItem.put("authorities", client.getRoles());
            result.put("oAuth2Request", clientItem);
        }
        return result;
    }

    @GetMapping("/client/{clientId}")
    public Client getClient(@PathVariable String clientId) {
        return (Client) clientService.loadClientByClientId(clientId);
    }

    @GetMapping("/scope/{scopeName}")
    public Optional<Scope> getScope(@PathVariable String scopeName) {
        return scopeService.loadScopeByScopeName(scopeName);
    }
}
