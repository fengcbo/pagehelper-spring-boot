package com.ourziu.pagehelper.autoconfigure;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Properties;

@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@EnableConfigurationProperties(PageHelperProperties.class)
public class PageHelperAutoConfiguration {


    @Bean
    public PageHelperBeanPostProcessor pageHelperBeanPostProcessor(PageHelperProperties pageHelperProperties){
        return new PageHelperBeanPostProcessor(pageHelperProperties);
    }


    private class PageHelperBeanPostProcessor implements BeanPostProcessor,EnvironmentAware{

        private PageHelperProperties pageHelperProperties;
        private RelaxedPropertyResolver resolver;
        private PageInterceptor pageInterceptor;

        public PageHelperBeanPostProcessor(PageHelperProperties pageHelperProperties){
            this.pageHelperProperties = pageHelperProperties;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (pageInterceptor != null && bean instanceof SqlSessionFactory){
                SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) bean;
                sqlSessionFactory.getConfiguration().addInterceptor(pageInterceptor);
            }
            return bean;
        }

        @Override
        public void setEnvironment(Environment environment) {
            resolver = new RelaxedPropertyResolver(environment, "pagehelper.");
            PageInterceptor interceptor = new PageInterceptor();
            Properties properties = pageHelperProperties.getProperties();
            Map<String, Object> subProperties = resolver.getSubProperties("");
            for (String key : subProperties.keySet()) {
                if (!properties.containsKey(key)) {
                    properties.setProperty(key, resolver.getProperty(key));
                }
            }
            interceptor.setProperties(properties);
            this.pageInterceptor = interceptor;
        }
    }

}
