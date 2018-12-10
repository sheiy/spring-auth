package site.ownw.resourceserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sofior
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RBAC {

    private final Set<String> defaultPermissionUrls = Set.of("/logout");

    private final ResourceServerProperties resourceServerProperties;

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @SuppressWarnings("unchecked")
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        if (checkPermission(request, defaultPermissionUrls)) {
            return true;
        }
        if (authentication instanceof OAuth2Authentication) {
            var details = (Map<String, Object>) ((OAuth2Authentication) authentication).getUserAuthentication().getDetails();
            var authorities = (List<Map<String, Object>>) details.get("authorities");
            var scopes = (List<Map<String, Object>>) ((Map<String, Object>) details.get("oAuth2Request")).get("scope");
            authorities.addAll((List<Map<String, Object>>) ((Map<String, Object>) details.get("oAuth2Request")).get("authorities"));
            //先匹配role的url路径匹配成功的再匹配scope的url
            //Client的Scope允许访问的地址
            Set<String> scopePermissionUrls = scopes.stream()
                    //拿出所有scope的permissionUrls
                    .map(scope -> (ArrayList<Map<String, Object>>) scope.get("permissionUrls"))
                    //合并scope的permissionUrls为一个数组
                    .flatMap(Collection::stream)
                    //过滤掉未启用和不是当前资源服务器的permissionUrls
                    .filter(permissionUrl -> Objects.equals(permissionUrl.get("enable"), true) && Objects.equals(resourceServerProperties.getId(), permissionUrl.get("resourceId")))
                    //将permissionUrl中的url提取成字符串数组
                    .map(permissionUrl -> (ArrayList<String>) permissionUrl.get("url"))
                    //合并每个permissionUrl中提取出来的字符串数组为一个数组
                    .flatMap(Collection::stream)
                    //收集为一个集合
                    .collect(Collectors.toSet());
            //用户和Client的Role允许访问的地址
            Set<String> rolePermissionUrls = authorities.stream()
                    //拿出所有role的permissionUrls
                    .map(item -> (List<Map<String, Object>>) item.get("permissionUrls"))
                    //合并role的permissionUrls为一个数组
                    .flatMap(Collection::stream)
                    //过滤掉未启用和不是当前资源服务器的permissionUrls
                    .filter(permissionUrl -> Objects.equals(permissionUrl.get("enable"), true) && Objects.equals(resourceServerProperties.getId(), permissionUrl.get("resourceId")))
                    //将permissionUrl中的url提取成字符串数组
                    .map(permissionUrl -> (ArrayList<String>) permissionUrl.get("url"))
                    //合并每个permissionUrl中提取出来的字符串数组为一个数组
                    .flatMap(Collection::stream)
                    //收集为一个集合
                    .collect(Collectors.toSet());
            //添加默认可以访问的Url
            return checkPermission(request, rolePermissionUrls, scopePermissionUrls);
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

    private boolean checkPermission(HttpServletRequest request, Collection<String> rolePermissionUrls, Collection<String> scopePermissionUrls) {
        //添加额外的默认允许访问的地址
//        rolePermissionUrls.addAll(defaultPermissionUrls);
//        scopePermissionUrls.addAll(defaultPermissionUrls);
        //访问的地址在role的允许范围内并且在scope的允许范围内则可访问
        return rolePermissionUrls.stream().anyMatch(url -> PATH_MATCHER.match(url, request.getRequestURI()))
                && scopePermissionUrls.stream().anyMatch(url -> PATH_MATCHER.match(url, request.getRequestURI()));
    }
}