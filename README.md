# springcloudNetflix-demo

### 先决条件

首先本机先要安装以下环境。

- [git](https://git-scm.com/)
- [java8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 
- [maven](http://maven.apache.org/)

例外本机用到了以下中间件，请自行部署。
- rocketmq
- mysql
- redis

### 技术栈
- 核心框架：springcloud-alibaba:2.1 + springcloud:Greenwich.RELEASE，指定版本springboot:2.1.4.RELEASE
- 持久层框架：springDataJpa
- 数据库连接池：Alibaba Druid
- 日志管理：Logback
- 数据存储：mysql
- 公用缓存：redis
- 消息中间件：rocketmq
- 链路监控：sleuth + zipkin
- 分布式事务：seata

### 已实现的功能
- 基于Nacos Discovery的服务管理
- 基于Nacos Config的配置管理和动态刷新配置
- 基于Feign和RestTemplate的服务调用
- 基于Gateway的网关服务
- 基于Sleuth + Zipkin的链路监控服务
- 基于Rocketmq的消息驱动
- 基于Sentinel的流量控制和服务熔断、服务降级
- 基于Seata的改良型2PC分布式事务
- 支持Redis的数据服务缓存

### 项目目录结构说明

```
├─springcloudAlibaba-demo-----------------------父项目，公共依赖
│  │
│  ├─api-gateway-------------------------------微服务网关
│  │
│  ├─shop-common---------------------------------全局公共依赖
│  │  │
│  │  ├─common-core-----------------------------核心基础依赖
│  │  │
│  │  └─common-api------------------------------api基础依赖
│  │
│  ├─shop-product--------------------------------商品服务
│  │  │
│  │  ├─product-common--------------------------商品服务基础依赖
│  │  │
│  │  ├─product-client--------------------------商品服务客户端
│  │  │
│  │  └─product-server--------------------------商品服务服务端
│  │
│  ├─shop-order----------------------------------订单服务
│  │  │
│  │  ├─order-common----------------------------订单服务基础依赖
│  │  │
│  │  └─order-server----------------------------订单服务服务端
│  │
│  ├─shop-user-----------------------------------用户服务
│  │  │
│  │  ├─user-common-----------------------------用户服务基础依赖
│  │  │
│  │  └─user-server-----------------------------用户服务服务端
```

### 部署配置
默认的部署分布如下：

|  服务          |   服务名         |  端口     | 备注                                            |
|---------------|-----------------|-----------|-------------------------------------------------|
|  数据库        |   mysql        |  3306     |  目前各应用共用一个，应用可建不同的database     |
|  公共缓存        |   redis         |  6379     |  目前共用                     |
|  消息中间件     |   rocketmq      |  9876     |  共用                          |
|  日志收集中间件  |   zipkin       |  9411     |  共用                          |
|  注册中心  |   nacos       |  8848     |  目前单点，可以配置高可用        
|  配置中心  |   nacos       |  8848     |  目前单点，可以配置高可用        
|  网关中心  |   api-gateway       |  8100     |  目前单点，可以配置高可用
|  商品服务  |   service-product       |  8081     |  作为提供者的示例
|  订单服务  |   service-order         |  8091     |  作为消费者的示例
|  用户服务  |   service-user          |  8071     |  与网关配合实现用户鉴权        

其中redis,rocketmq,zipkin都用docker部署，注意配置里应填宿主机地址。

### 开发环境

1. 克隆代码库： `https://github.com/dantegarden/springcloudAlibaba-demo.git`
2. 在根目录下安装依赖：`mvn clean install -U`
3. 准备一个空数据库
3. 部署并启动nacos
4. 修改配置文件（本项目的配置已托管到配置中心，如果暂时不用配置中心，可以打开application.yml的注释）
5. 按照任意顺序，启动项目

可对比本人仓库下的
[springcloud-netflix-demo](https://github.com/dantegarden/springcloudNetflix-demo) 
项目，来理解springcloud-alibaba的优劣。