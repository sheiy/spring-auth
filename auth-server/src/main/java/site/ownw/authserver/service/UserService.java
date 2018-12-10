package site.ownw.authserver.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import site.ownw.authserver.entity.User;
import site.ownw.authserver.model.request.UserRequestModel;

/**
 * @author sofior
 * @date 2018/8/17 00:03
 */
public interface UserService extends UserDetailsService {

    /**
     * 分页查询
     *
     * @param page 第几页，从0开始
     * @param size 页面大小
     * @return 分页信息
     */
    Page<User> findUsers(Integer page, Integer size);

    /**
     * 新增修改用户
     *
     * @param user 需要新增和修改的用户
     * @return 需要新增和修改后的用户
     */
    User saveUser(UserRequestModel user);

}
