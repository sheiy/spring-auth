package site.ownw.authserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import site.ownw.authserver.entity.*;
import site.ownw.authserver.repository.ClientRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sofior
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RBAC {

    private final Set<String> defaultPermissionUrls = Set.of("/logout", "/api/auth/**", "/test**","/error");

    private final ResourceServerProperties resourceServerProperties;
    private final ClientRepository clientRepository;

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public boolean userHasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (checkPermission(request, defaultPermissionUrls)) {
            return true;
        }
        if (principal instanceof User) {
            User user = (User) principal;
            Set<String> permissionUrls = user.getRoles().stream()
                    .map(Role::getPermissionUrls)
                    .flatMap(Collection::stream)
                    .filter(PermissionUrl::getEnable)
                    .map(PermissionUrl::getUrl)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
//            permissionUrls.addAll(defaultPermissionUrls);
            return checkPermission(request, permissionUrls);
        }
        return false;
    }

    private boolean checkPermission(HttpServletRequest request, Collection<String> permissionUrls) {
        for (String permissionUrl : permissionUrls) {
            if (PATH_MATCHER.match(permissionUrl, request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean clientHasPermission(HttpServletRequest request, Authentication authentication) {
        if (checkPermission(request, defaultPermissionUrls)) {
            return true;
        }
        if (authentication instanceof OAuth2Authentication) {
            Optional<Client> optionalClient = clientRepository.findFirstByClientId(((OAuth2Authentication) authentication).getOAuth2Request().getClientId());
            if (!optionalClient.isPresent()) {
                return false;
            }
            Client client = optionalClient.get();
            User user = (User) authentication.getPrincipal();

            List<Scope> scopes = client.getScopes();
            var authorities = new HashSet<Role>();
            authorities.addAll(user.getRoles());
            authorities.addAll(client.getRoles());
            //Client的Scope允许访问的地址
            Set<String> scopePermissionUrls = scopes.stream()
                    //拿出所有scope的permissionUrls
                    .map(Scope::getPermissionUrls)
                    //合并scope的permissionUrls为一个数组
                    .flatMap(Collection::stream)
                    //过滤掉未启用和不是当前资源服务器的permissionUrls
                    .filter(permissionUrl -> permissionUrl.getEnable() && Objects.equals(resourceServerProperties.getId(), permissionUrl.getResourceId()))
                    //将permissionUrl中的url提取成字符串数组
                    .map(PermissionUrl::getUrl)
                    //合并每个permissionUrl中提取出来的字符串数组为一个数组
                    .flatMap(Collection::stream)
                    //收集为一个集合
                    .collect(Collectors.toSet());
            //用户和Client的Role允许访问的地址
            Set<String> rolePermissionUrls = authorities.stream()
                    //拿出所有role的permissionUrls
                    .map(Role::getPermissionUrls)
                    //合并role的permissionUrls为一个数组
                    .flatMap(Collection::stream)
                    //过滤掉未启用和不是当前资源服务器的permissionUrls
                    .filter(permissionUrl -> permissionUrl.getEnable() && Objects.equals(resourceServerProperties.getId(), permissionUrl.getResourceId()))
                    //将permissionUrl中的url提取成字符串数组
                    .map(PermissionUrl::getUrl)
                    //合并每个permissionUrl中提取出来的字符串数组为一个数组
                    .flatMap(Collection::stream)
                    //收集为一个集合
                    .collect(Collectors.toSet());
            //添加默认可以访问的Url
            return checkPermission(request, rolePermissionUrls, scopePermissionUrls);
        }
        return false;
    }

    private boolean checkPermission(HttpServletRequest request, Collection<String> rolePermissionUrls, Collection<String> scopePermissionUrls) {
        //添加额外的默认允许访问的地址
//        rolePermissionUrls.addAll(defaultPermissionUrls);
//        scopePermissionUrls.addAll(defaultPermissionUrls);
        //访问的地址在role的允许范围内并且在scope的允许范围内则可访问
        return rolePermissionUrls.stream().anyMatch(url -> PATH_MATCHER.match(url, request.getRequestURI()))
                && scopePermissionUrls.stream().anyMatch(url -> PATH_MATCHER.match(url, request.getRequestURI()));
    }
}
