package site.ownw.authserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import site.ownw.authserver.entity.Client;
import site.ownw.authserver.entity.Scope;
import site.ownw.authserver.model.request.ScopeRequestModel;
import site.ownw.authserver.repository.PermissionUrlRepository;
import site.ownw.authserver.repository.ScopeRepository;
import site.ownw.authserver.service.ScopeService;
import site.ownw.authserver.util.BeanUtils;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author sofior
 * @date 2018/11/12 14:09
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScopeServiceImpl implements ScopeService {

    private final ScopeRepository scopeRepository;
    private final PermissionUrlRepository permissionUrlRepository;

    @Override
    public Page<Scope> findScopes(Integer page, Integer size) {
        return scopeRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Scope saveScope(ScopeRequestModel scopeRequestModel) {
        Scope scope;
        if (scopeRequestModel.isNew()) {
            scope = new Scope();
            BeanUtils.copyProperties(scopeRequestModel, scope);
        } else {
            var optional = scopeRepository.findById(scopeRequestModel.getId());
            BeanUtils.copyProperties(scopeRequestModel, optional.orElseThrow(() -> new RuntimeException("修改失败")), BeanUtils.findNullFields(scopeRequestModel));
            scope = optional.get();
        }
        scope.setPermissionUrls(new ArrayList<>(scopeRequestModel.getPermissionUrls().size()));
        for (Long permissionUrlId : scopeRequestModel.getPermissionUrls()) {
            permissionUrlRepository.findById(permissionUrlId).ifPresent(permissionUrl -> scope.getPermissionUrls().add(permissionUrl));
        }
        return scopeRepository.save(scope);
    }

    @Override
    public Optional<Scope> loadScopeByScopeName(String scopeName) {
        return scopeRepository.findFirstByName(scopeName);
    }

    @Override
    public Scope delScope(Long scopeId) {
        Scope scope = scopeRepository.findById(scopeId).orElseThrow(() -> new RuntimeException("删除失败"));
        scopeRepository.deleteById(scopeId);
        return scope;
    }
}
