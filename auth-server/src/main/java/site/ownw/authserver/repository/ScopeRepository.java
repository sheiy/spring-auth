package site.ownw.authserver.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import site.ownw.authserver.entity.Scope;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ScopeRepository extends PagingAndSortingRepository<Scope, Long> {

    List<Scope> findAllByNameIn(Collection<String> names);

    Optional<Scope> findFirstByName(String name);
}
