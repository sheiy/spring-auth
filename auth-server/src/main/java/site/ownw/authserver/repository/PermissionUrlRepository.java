package site.ownw.authserver.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import site.ownw.authserver.entity.PermissionUrl;
import site.ownw.authserver.entity.User;

import java.util.Optional;

/**
 * @author sofior
 * @date 2018/8/16 23:35
 */
public interface PermissionUrlRepository extends PagingAndSortingRepository<PermissionUrl, Long> {

}
