package site.ownw.authserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.WebRequest;

/**
 * @author sofior
 * @date 2018/11/22 17:39
 */
//@Slf4j
//@Configuration
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class LoggedErrorAttributes extends DefaultErrorAttributes {
//
//    @Override
//    public Throwable getError(WebRequest webRequest) {
//        Throwable error = super.getError(webRequest);
//        log.error("系统异常:", error);
//        return error;
//    }
//}
