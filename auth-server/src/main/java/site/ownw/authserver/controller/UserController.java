package site.ownw.authserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.ownw.authserver.entity.User;
import site.ownw.authserver.model.request.UserRequestModel;
import site.ownw.authserver.service.UserService;

import java.security.Principal;

/**
 * @author Sofior
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public Principal principal(Principal principal) {
        return principal;
    }

    @GetMapping("/findUsers")
    public Page<User> findUsers(@RequestParam(required = false, defaultValue = "0") Integer page,
                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.findUsers(page, size);
    }

    @PostMapping("/saveUser")
    public User saveUser(@RequestBody UserRequestModel user) {
        return userService.saveUser(user);
    }
}
