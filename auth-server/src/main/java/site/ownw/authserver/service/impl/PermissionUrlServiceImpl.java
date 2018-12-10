package site.ownw.authserver.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ownw.authserver.entity.PermissionUrl;
import site.ownw.authserver.repository.PermissionUrlRepository;
import site.ownw.authserver.service.PermissionUrlService;
import site.ownw.authserver.util.BeanUtils;

/**
 * @author sofior
 * @date 2018/8/17 00:03
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionUrlServiceImpl implements PermissionUrlService {

    private final PermissionUrlRepository permissionUrlRepository;

    @Override
    public Page<PermissionUrl> findPermissionUrls(final @NonNull Integer page, final @NonNull Integer size) {
        return permissionUrlRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PermissionUrl savePermissionUrl(PermissionUrl permissionUrl) {
        if (!permissionUrl.isNew()) {
            var optional = permissionUrlRepository.findById(permissionUrl.getId());
            BeanUtils.copyProperties(permissionUrl, optional.orElseThrow(() -> new RuntimeException("修改失败")), BeanUtils.findNullFields(permissionUrl));
            permissionUrl = optional.get();
        }
        return permissionUrlRepository.save(permissionUrl);
    }

    @Override
    public PermissionUrl delPermissionUrl(Long permissionUrlId) {
        PermissionUrl permissionUrl = permissionUrlRepository.findById(permissionUrlId).orElseThrow(() -> new RuntimeException("删除失败"));
        permissionUrlRepository.deleteById(permissionUrlId);
        return permissionUrl;
    }
}
