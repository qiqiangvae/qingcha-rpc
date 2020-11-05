# 项目介绍

本项目是一个基于 netty 的 rpc 框架，可以使用纯 java 编程或者跟 SpringBoot 集成。

本项目纯属练手项目，未经测试和生产使用，谨慎使用！欢迎交流。

# 使用介绍

## java 方式

### Server 端

1. 引入依赖

   ```xml
   <dependency>
     <groupId>com.qingcha.rpc</groupId>
     <artifactId>qingcha-rpc-server</artifactId>
     <version>1.0-SNAPSHOT</version>
   </dependency>
   <dependency>
     <groupId>com.alibaba</groupId>
     <artifactId>fastjson</artifactId>
     <version>1.2.68</version>
   </dependency>
   ```

2. 编写需要暴露的服务，在类上或者方法上加上`@RpcInvoke`注解表示将服务暴露出去。

   ```java
   @RpcInvoke
   public class HelloServiceImpl implements HelloService{
     @Override
     public String hello(String name) {
       return "hello qingcha";
     }
   }
   public class BookServiceImpl implements BookService {
     @Override
     @RpcInvoke
     public List<Book> findByAuthor(String author) {
       return new ArrayList<Book>() {{
         add(new Book("天龙八部"));
         add(new Book("射雕英雄转"));
         add(new Book("倚天屠龙记"));
       }};
     }
   }
   ```

3. 编写启动类。

   ```java
   public class RpcServerExample {
     public static void main(String[] args) {
       // 配置需要扫描的服务实现类所在包
       RpcServerConfiguration.configuration().setPackagePath("com.qingcha.rpc.examples.quickstart.service");
       RpcServer rpcServer = new RpcServer(9900);
       rpcServer.start();
     }
   }
   ```

   

### Client 端

1. 引入依赖。

   ```xml
   <dependency>
     <groupId>com.qingcha.rpc</groupId>
     <artifactId>qingcha-rpc-client</artifactId>
     <version>1.0-SNAPSHOT</version>
   </dependency>
   <dependency>
     <groupId>com.alibaba</groupId>
     <artifactId>fastjson</artifactId>
     <version>1.2.68</version>
   </dependency>
   ```

   

2. 编写启动类。

   ```java
   public class RpcClientConcurrencyExample {
     public static void main(String[] args) {
       RpcClientConfiguration.configuration().setHost("localhost");
       RpcClientConfiguration.configuration().setPort(9900);
       RpcClientConfiguration.configuration().setPackagePath("com.qingcha.rpc.examples.quickstart.service");
       ProxyProcessor proxyProcessor = ProxyProcessor.instance();
       HelloService helloService = proxyProcessor.getProxy(HelloService.class);
       for (int i = 0; i < 10; i++) {
         System.out.println(helloService.hello("JonKee" + i));
       }
       ExecutorService executorService = Executors.newFixedThreadPool(4);
       BookService bookService = proxyProcessor.getProxy(BookService.class);
       for (int i = 0; i < 10; i++) {
         executorService.execute(new Runnable() {
           @Override
           public void run() {
             for (int j = 0; j < 10; j++) {
               List<Book> books = bookService.findByAuthor("金庸");
               System.out.println(JSON.toJSONString(books));
             }
           }
         });
       }
     }
   }
   ```

   

## SpringBoot 集成

### Server

1. 引入依赖。

   ```xml
   <dependency>
     <groupId>com.qingcha.rpc</groupId>
     <artifactId>qingcha-rpc-spring-boot-starter</artifactId>
     <version>1.0-SNAPSHOT</version>
   </dependency>
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter</artifactId>
     <version>2.3.2.RELEASE</version>
   </dependency>
   <dependency>
     <groupId>com.alibaba</groupId>
     <artifactId>fastjson</artifactId>
     <version>1.2.68</version>
   </dependency>
   ```

   

2. 新增配置文件。

   ```yaml
   qingcha:
     rpc:
        port: 9900
   ```

   

3. 编写启动类。

   ```java
   @SpringBootApplication
   @EnableRpcServer
   public class SpringServerApplication {
     public static void main(String[] args) {
       SpringApplication.run(SpringServerApplication.class, args);
     }
   }
   ```

   

### Client

1. 引入依赖，同服务端。

2. 新增配置文件。

   ```yaml
   qingcha:
     rpc:
       client:
         package-path: com.qingcha.rpc.examples.spring.service
         host: localhost
         port: 9900
   ```

3. 编写启动类。

   ```java
   @SpringBootApplication
   @EnableRpcClient
   public class SpringClientApplication {
     public static void main(String[] args) {
       ConfigurableApplicationContext context = SpringApplication.run(SpringClientApplication.class, args);
       BookService bookService = context.getBean(BookService.class);
       ExecutorService executorService = Executors.newFixedThreadPool(4);
       for (int i = 0; i < 10; i++) {
         executorService.execute(() -> {
           for (int j = 0; j < 10; j++) {
             List<Book> books = bookService.findByAuthor("金庸");
             System.out.println(JSON.toJSONString(books));
           }
         });
       }
     }
   }
   ```

   

## 扩展点

### 协议序列化工具扩展

实现`com.qingcha.rpc.core.protocol.ProtocolSerialize`接口。然后交由`ProtocolSerializeManager`管理，可以使用静态方法`setProtocolSerialize`设置或者使用 SPI 机制，默认使用`FastJsonProtocolSerialize`,如果使用默认，需要添加 fastjson 依赖。

```java
public class ProtocolSerializeManager {
    private static ProtocolSerialize protocolSerialize;

    public static void setProtocolSerialize(ProtocolSerialize protocolSerialize) {
        ProtocolSerializeManager.protocolSerialize = protocolSerialize;
    }

    public static synchronized ProtocolSerialize getProtocolSerialize() {
        // 从 SPI 中获取
        if (protocolSerialize == null) {
            ServiceLoader<ProtocolSerialize> serviceLoader = ServiceLoader.load(ProtocolSerialize.class);
            for (ProtocolSerialize serialize : serviceLoader) {
                if (serialize != null) {
                    protocolSerialize = serialize;
                    break;
                }
            }
        }
        // 使用默认的序列化工具
        if (protocolSerialize == null) {
            protocolSerialize = new FastJsonProtocolSerialize();
        }
        return protocolSerialize;
    }
}
```

### 客户端代理池扩展

客户端代理池用于代理接口的管理，具体实现可参考以下两种组合。

1. `com.qingcha.rpc.client.proxy.DefaultProxyPoolFactory`和`com.qingcha.rpc.client.proxy.DefaultProxyPool`。
2. `com.qingcha.rpc.springboot.client.SpringRpcProxyPoolFactory`和`com.qingcha.rpc.springboot.client.SpringProxyPool`。

```java
public class ProxyPoolManager {
  private static ProxyPool proxyPool;

  public static void setProxyPool(ProxyPool proxyPool) {
    ProxyPoolManager.proxyPool = proxyPool;
  }

  public static ProxyPool getProxyPool() {
    // 通过 SPI 机制获取 ProxyPoolFactory，从而获取代理池 proxyPool
    if (proxyPool == null) {
      ServiceLoader<ProxyPoolFactory> factories = ServiceLoader.load(ProxyPoolFactory.class);
      for (ProxyPoolFactory factory : factories) {
        if (factory != null) {
          proxyPool = factory.getProxyPool();
        }
      }
      if (proxyPool == null) {
        RpcClientConfiguration configuration = RpcClientConfiguration.configuration();
        proxyPool = new DefaultProxyPoolFactory(configuration.getPackagePath()).getProxyPool();
      }
    }
    return proxyPool;
  }
}
```

### 服务端方法池扩展

服务端方法池用于暴露服务的管理，具体可以参考一下两种组合。

1. `com.qingcha.rpc.server.invoke.DefaultMethodPoolFactory`和`com.qingcha.rpc.server.invoke.DefaultMethodPool`。
2. `com.qingcha.rpc.springboot.server.SpringRpcMethodPoolFactory`和`com.qingcha.rpc.springboot.server.SpringMethodPool`。

```java
public class MethodPoolManager {
  private static MethodPool methodPool;

  public static void setMethodPool(MethodPool methodPool) {
    MethodPoolManager.methodPool = methodPool;
  }

  public static synchronized MethodPool getMethodPool() {
    if (methodPool == null) {
      // 从 spi 中获取 methodPool
      ServiceLoader<MethodPoolFactory> methodPoolFactories = ServiceLoader.load(MethodPoolFactory.class);
      for (MethodPoolFactory factory : methodPoolFactories) {
        if (factory != null) {
          methodPool = factory.getMethodPool();
          break;
        }
      }
      // 如果获取不到，则用默认的
      if (methodPool == null) {
        RpcServerConfiguration configuration = RpcServerConfiguration.configuration();
        methodPool = new DefaultMethodPoolFactory(configuration.getPackagePath()).getMethodPool();
      }
    }
    return methodPool;
  }
}
```

