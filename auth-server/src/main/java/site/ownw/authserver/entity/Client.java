package site.ownw.authserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import site.ownw.authserver.entity.converter.JpaJSONConverter;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sofior
 * @date 2018/10/25 16:28
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Client extends BaseEntity implements ClientDetails {

    private static final long serialVersionUID = 3096897435219135198L;

    /**
     * 此client属于哪个组织
     */
    private String owner;

    private String clientId;

    @JsonIgnore
    private String clientSecret;

    /**
     * 访问范围
     */
    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(targetEntity = Scope.class, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = {@JoinColumn(name = "client_id")}, inverseJoinColumns = {@JoinColumn(name = "scope_id")})
    private List<Scope> scopes;

    /**
     * 授权模式,参见OAuth2文档
     * 测试支持Authorization Code和Client Credentials
     * 其他模式,未测试,这两种模式应该能满足业务需求
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JpaJSONConverter.class)
    private List<String> authorizedGrantTypes;

    /**
     * 注册的回调地址
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JpaJSONConverter.class)
    private List<String> registeredRedirectUri;

    /**
     * Client角色
     */
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = {@JoinColumn(name = "client_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    /**
     * accessToken有效时间
     */
    private Integer accessTokenValiditySeconds;

    /**
     * refreshToken有效时间
     */
    private Integer refreshTokenValiditySeconds;

    /**
     * 在进行Authorization Code时是否自动同意授权而不提示用户确认
     */
    private boolean autoApprove;

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        //直接返回空,使Spring允许访问所有的resource,权限resourceID的访问权限通过PermissionUrl控制
        return Set.of();
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        //使用RBAC控制这个应该是没有用了
        return true;
    }

    @Override
    public Set<String> getScope() {
        return scopes == null ? Set.of() : scopes.stream().map(Scope::getName).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes == null ? Set.of() : Set.copyOf(authorizedGrantTypes);
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri == null ? Set.of() : Set.copyOf(registeredRedirectUri);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return roles == null ? Set.of() : Set.copyOf(roles);
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return autoApprove;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return Map.of();
    }

}