package tech.washmore.util.eventbus.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 带有此注解的class才会被扫描是否含有Subscribe注解的方法(观察者)
 *
 * @author Washmore
 * @version V1.0
 * @since 2017/5/17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Subscriber {
}
