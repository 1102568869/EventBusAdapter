package tech.washmore.util.eventbus.adapter;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.washmore.util.eventbus.annotations.Subscriber;
import tech.washmore.util.eventbus.spring.SpringConfig;

import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 本类中已经包含eventBus的一套已经配置好的异步线程池实现, 直接以EventBusAdapter.post(event)的方式使用
 *
 * @author Washmore
 * @version V1.0
 * @since 2017/5/17
 */
@Configuration(value = "WashmoreEventBusAdapter")
public class EventBusAdapter {

    static final String BEANNAME_EVENTBUS = "ASYNC_BEANNAME_EVENTBUS_WASHMORE";


    public static void post(Object event) {
        SpringConfig.withName(BEANNAME_EVENTBUS, EventBus.class).post(event);
    }

    @Configuration
    public static class RentServiceContext implements ApplicationContextAware {

        @Autowired
        @Qualifier(BEANNAME_EVENTBUS)
        private EventBus eventBus;

        @Bean(name = BEANNAME_EVENTBUS)
        protected EventBus initEventBus() {
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 50, 60, TimeUnit.SECONDS, new ArrayBlockingQueue(1000),
                    new ThreadFactoryBuilder().setDaemon(true).setNameFormat(BEANNAME_EVENTBUS.toLowerCase() + "-thread-%d").build());
            EventBus eventBus = new AsyncEventBus(BEANNAME_EVENTBUS, threadPool);
            return eventBus;
        }

        private Set<Object> listenerSet = new HashSet<Object>();

        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(Subscriber.class);
            if (CollectionUtils.isEmpty(beanMap)) {
                return;
            }
            for (String k : beanMap.keySet()) {
                Object v = beanMap.get(k);
                Method[] methods = v.getClass().getMethods();
                if (methods == null || methods.length == 0) {
                    continue;
                }

                for (Method m : methods) {
                    if (m.getAnnotation(Subscribe.class) == null) {
                        continue;
                    }
                    this.eventBus.register(v);
                    this.listenerSet.add(v);
                }
            }
        }

        @PreDestroy
        public void destroy() {
            if (this.listenerSet.isEmpty()) {
                return;
            }

            for (Object l : this.listenerSet) {
                this.eventBus.unregister(l);
            }
        }
    }

}
