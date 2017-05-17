## EventBusAdapter.jar功能介绍
简单来讲,EventBus是一个事件发布和订阅的框架.而本工具进一步简化EventBus的配置以及与Spring环境的结合,拿来即用!

### Maven配置
1.在项目中新增本插件私服仓库节点
```

    <repositories>
        <repository>
            <id>washmore</id>
            <url>http://maven.washmore.tech/nexus/content/repositories/public/url>
        </repository>
    </repositories>  

    <pluginRepositories>
        <pluginRepository>
            <id>washmore</id>
            <url>http://maven.washmore.tech/nexus/content/repositories/public</url>
        </pluginRepository>
    </pluginRepositories>   
         
```

2.在项目中引入最新版本的Maven依赖
```
    <dependency>
        <groupId>tech.washmore</groupId>
        <artifactId>util.eventbus</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```

### 使用方法
1:首先将本插件托管给spring   
使用xml声明式:  
在spring配置文件中增加一行代码  
```
    <import resource="classpath*:eventbus/spring-context.xml"/>
```
或者使用注解式:  
新建类EvetnBusAdapterConfig类(确保此类能被自动扫描到):  
```
@Configuration
@ImportResource("classpath*:eventbus/spring-context.xml")
public class EvetnBusAdapterConfig {
}
```
   
2:推送事件源  
在需要监听的地方调用 EventBusAdapter.post(event) 推送事件源  

3:监听事件源
创建监听类(观察者),需在类上添加@Subscriber注解才会被本插件注册;  
然后实现监听方法,参数类型同event,需在方法上添加@Subscribe注解,如有需要,可按照此步骤实现多个监听方法;

####tips:
如果不能理解本文所述,请先自行了解EventBus相关基础知识!



