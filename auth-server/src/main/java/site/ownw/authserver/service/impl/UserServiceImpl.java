package site.ownw.authserver.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ownw.authserver.entity.User;
import site.ownw.authserver.model.request.UserRequestModel;
import site.ownw.authserver.repository.RoleRepository;
import site.ownw.authserver.repository.UserRepository;
import site.ownw.authserver.service.UserService;
import site.ownw.authserver.util.BeanUtils;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author sofior
 * @date 2018/8/17 00:03
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder = new SCryptPasswordEncoder();

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(final @NonNull String username) throws UsernameNotFoundException {
        return userRepository.findFirstByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public Page<User> findUsers(final @NonNull Integer page, final @NonNull Integer size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User saveUser(UserRequestModel userRequestModel) {
        User user;
        if (userRequestModel.isNew()) {
            user = new User();
            BeanUtils.copyProperties(userRequestModel, user);
            user.setPassword(generatePassword());
        } else {
            var optional = userRepository.findById(userRequestModel.getId());
            BeanUtils.copyProperties(userRequestModel, optional.orElseThrow(() -> new RuntimeException("修改失败")), BeanUtils.findNullFields(userRequestModel));
            user = optional.get();
        }
        user.setRoles(new ArrayList<>(userRequestModel.getRoles().size()));
        for (Long roleId : userRequestModel.getRoles()) {
            roleRepository.findById(roleId).ifPresent(role -> user.getRoles().add(role));
        }
        return userRepository.save(user);
    }

    private String encodePassword(@NonNull final String password) {
        return "{scrypt}" + passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return encodePassword("123456");
    }
}
