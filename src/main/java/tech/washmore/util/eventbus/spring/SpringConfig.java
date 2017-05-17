package tech.washmore.util.eventbus.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 获取spring上下文环境
 *
 * @author Washmore
 * @version V1.0
 * @since 2017/5/7
 */
public class SpringConfig {

    private static ApplicationContext delegateContext;

    static void setContext(ApplicationContext applicationContext) {
        delegateContext = applicationContext;
    }

    public static <T> T of(Class<T> cls) {
        return delegateContext.getBean(cls);
    }

    public static <T> T withName(String beanName, Class<T> cls) {
        return delegateContext.getBean(beanName, cls);
    }

    @Configuration
    static class ServicesConfig implements ApplicationContextAware {
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            SpringConfig.setContext(applicationContext);
        }
    }
}
