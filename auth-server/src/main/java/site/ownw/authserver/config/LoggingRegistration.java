package site.ownw.authserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import site.ownw.authserver.annotation.IgnoreLogging;
import site.ownw.authserver.entity.Log;
import site.ownw.authserver.repository.LogRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static site.ownw.authserver.util.ApplicationUtils.getIpAddr;


/**
 * @author sofior
 * @date 2018/10/18 10:59
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoggingRegistration implements WebMvcRegistrations {

    private final LogRepository logRepository;
    private final String PATH_STRING = "path";
    private final String UNKNOWN = "unknown";

    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        return new RequestMappingHandlerAdapter() {
            @Override
            protected ServletInvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
                return new ServletInvocableHandlerMethod(handlerMethod) {
                    @Override
                    public Object invokeForRequest(NativeWebRequest request, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {

                        Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);

                        var ignoreLog = handlerMethod.getMethod().isAnnotationPresent(IgnoreLogging.class)
                                || handlerMethod.getBeanType().isAnnotationPresent(IgnoreLogging.class);

                        long startTime = System.currentTimeMillis();
                        Object returnValue = doInvoke(args);
                        long cost = System.currentTimeMillis() - startTime;

                        try {
                            if (!ignoreLog) {
                                String path = UNKNOWN;
                                HttpServletRequest servletRequest = null;
                                if (request instanceof ServletWebRequest) {
                                    servletRequest = ((ServletWebRequest) request).getRequest();
                                    path = servletRequest.getServletPath();
                                }
                                if (returnValue instanceof ModelAndView) {
                                    Map<String, Object> model = ((ModelAndView) returnValue).getModel();
                                    if (model.containsKey(PATH_STRING)) {
                                        path = model.get(PATH_STRING).toString();
                                    }
                                }
                                if (returnValue instanceof ResponseEntity) {
                                    Object body = ((ResponseEntity) returnValue).getBody();
                                    if (body instanceof Map) {
                                        if (((Map) body).containsKey(PATH_STRING)) {
                                            path = ((Map) body).get(PATH_STRING).toString();
                                        }
                                    }
                                }
                                String requestParam = argsToString(args);
                                String response = returnValue.toString();
                                Log log = Log.builder()
                                        .ip(getIpAddr(servletRequest))
                                        .handler(ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()))
                                        .request(requestParam.length() > 2048 ? "too long" : requestParam)
                                        .response(response.length() > 2048 ? "too long" : response)
                                        .path(path)
                                        .processTime(cost)
                                        .build();
                                this.logger.info(log.toString());
                                logRepository.save(log);
                            }
                        } catch (Throwable t) {
                            logger.info("日志记录异常:", t);
                        }
                        return returnValue;
                    }
                };
            }
        };
    }

    private String argsToString(Object[] args) {
        if (args == null) {
            return "null";
        }

        int iMax = args.length - 1;
        if (iMax == -1) {
            return "[]";
        }

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            if (args[i] instanceof MultipartFile) {
                var file = (MultipartFile) args[i];
                multipartFileToString(file,b);
            } else {
                b.append(args[i]);
            }
            if (i == iMax) {
                return b.append(']').toString();
            }
            b.append(", ");
        }
    }

    private void multipartFileToString(MultipartFile file,StringBuilder b){
        b.append("(")
                .append("name=").append(file.getName()).append(",")
                .append("originalFilename=").append(file.getOriginalFilename()).append(",")
                .append("contentType=").append(file.getContentType()).append(",")
                .append("size=").append(file.getSize())
                .append(")");
    }
}
