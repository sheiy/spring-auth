package site.ownw.resourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Sofior
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {


    @GetMapping("/test")
    public Object principal(Principal principal) {
        return principal;
    }

}
