package site.ownw.authserver.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import site.ownw.authserver.entity.Client;

import java.util.Optional;

/**
 * @author sofior
 * @date 2018/10/26 13:36
 */
public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {

    Optional<Client> findFirstByClientId(String clientId);

}
