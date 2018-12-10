package site.ownw.authserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 忽略登陆
 * 用在Controller(RestController)上则此方法下所有方法无需登陆
 * 用在Controller(RestController)的public方法上则此方法无需登陆
 *
 * @author sofior
 * @date 2018/9/3 13:51
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface IgnoreLogin {
}
