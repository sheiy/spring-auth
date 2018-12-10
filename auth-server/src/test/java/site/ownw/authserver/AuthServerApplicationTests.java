package site.ownw.authserver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.WebApplicationContext;
import site.ownw.authserver.repository.ClientRepository;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class AuthServerApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    @Autowired
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();   //构造MockMvc
    }

    @Test
    public void demo() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/role/test"))
                .andReturn();
    }

    @Test
    public void uuid() {
        System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
    }

    @Test
    public void userPassword() {
        System.out.println(new SCryptPasswordEncoder().encode("admin"));
    }
}