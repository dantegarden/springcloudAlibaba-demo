package com.example.processor;

import io.seata.rm.datasource.DataSourceProxy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @description: 对dataSource偷梁换柱，将它的bean改为seata的DataSourceProxy
 * @author: lij
 * @create: 2020-02-26 00:13
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

    private static String targetBeanName = "dataSource";

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StringUtils.endsWithIgnoreCase(beanName, targetBeanName)) {
            boolean containsBean = defaultListableBeanFactory.containsBean(targetBeanName);
            if (containsBean) {
                DataSource dataSource = (DataSource) defaultListableBeanFactory.getBean(targetBeanName);
                //移除bean的定义和实例
                defaultListableBeanFactory.removeBeanDefinition(targetBeanName);
                //注册新的bean定义和实例
                defaultListableBeanFactory.registerBeanDefinition(targetBeanName, BeanDefinitionBuilder.genericBeanDefinition(DataSourceProxy.class).getBeanDefinition());
                bean = null;
                return new DataSourceProxy(dataSource);
            }

        }
        return bean;
    }
}
