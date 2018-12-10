package site.ownw.authserver.controller.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sofior
 */
@Slf4j
@Controller
public class ErrorController extends BasicErrorController {

    private final JavaMailSender mailSender;
    private final Environment environment;
    private final Profiles prod = Profiles.of("prod");

    public ErrorController(ErrorAttributes errorAttributes,
                           ServerProperties serverProperties,
                           ObjectProvider<ErrorViewResolver> errorViewResolvers,
                           JavaMailSender mailSender, Environment environment) {
        super(errorAttributes, serverProperties.getError(), errorViewResolvers.orderedStream().collect(Collectors.toList()));
        this.mailSender = mailSender;
        this.environment = environment;
    }

    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(request.getCharacterEncoding())) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        } else {
            response.setCharacterEncoding(request.getCharacterEncoding());
        }
        var modelAndView = super.errorHtml(request, response);
        sendErrorMail(modelAndView.getModel().toString());
        modelAndView.getModel().remove("trace");
        return modelAndView;
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        var result = super.error(request);
        if (result.getBody() != null) {
            sendErrorMail(result.getBody().toString());
            result.getBody().remove("trace");
        }
        return result;
    }

    private void sendErrorMail(String error) {
        if (environment.acceptsProfiles(prod)) {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom("发送异常信息的邮箱");
            mail.setTo("接收异常信息的邮箱");
            mail.setSubject("邮件主题");
            mail.setText(error);
            mailSender.send(mail);
        }
    }
}