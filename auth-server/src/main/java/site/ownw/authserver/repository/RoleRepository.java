package site.ownw.authserver.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import site.ownw.authserver.entity.Role;
import site.ownw.authserver.entity.User;

import java.util.Optional;

/**
 * @author sofior
 * @date 2018/8/16 23:35
 */
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

}
