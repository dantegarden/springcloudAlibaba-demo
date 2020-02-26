package com.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @description: Seata代理数据源
 * @author: lij
 * @create: 2020-02-25 21:50
 */
@Configuration
public class DataSourceProxyConfig {

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DruidDataSource druidDataSource(){
//        return new DruidDataSource();
//    }

//    @Primary
//    @Bean
//    public DataSourceProxy dataSource(DruidDataSource druidDataSource){
//        return new DataSourceProxy(druidDataSource);
//    }
}
