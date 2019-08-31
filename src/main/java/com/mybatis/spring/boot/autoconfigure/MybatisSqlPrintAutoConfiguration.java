package com.mybatis.spring.boot.autoconfigure;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
public class MybatisSqlPrintAutoConfiguration {


    @Bean
    @ConditionalOnExpression("${mybatis.print:true}")
    public MybatisSqlCompletePrintInterceptor printInterceptor() {
        return new MybatisSqlCompletePrintInterceptor();
    }



}
