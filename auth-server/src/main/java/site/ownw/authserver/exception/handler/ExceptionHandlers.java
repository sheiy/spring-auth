package site.ownw.authserver.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author Sofior
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExceptionHandlers {

    private final ErrorController errorController;

    @ExceptionHandler({DataIntegrityViolationException.class})
    public String DataIntegrityViolationExceptionHandler(HttpServletRequest request, DataIntegrityViolationException e) {
        return resolve(request, "存在关联数据");
    }

    private String resolve(HttpServletRequest request, String message) {
        request.setAttribute("javax.servlet.error.message", message);
        request.setAttribute("javax.servlet.error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "forward:" + errorController.getErrorPath();
    }
}
