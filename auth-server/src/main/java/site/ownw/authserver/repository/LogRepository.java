package site.ownw.authserver.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import site.ownw.authserver.entity.Log;

/**
 * @author sofior
 * @date 2018/9/6 11:26
 */
public interface LogRepository extends PagingAndSortingRepository<Log,Long> {
}
