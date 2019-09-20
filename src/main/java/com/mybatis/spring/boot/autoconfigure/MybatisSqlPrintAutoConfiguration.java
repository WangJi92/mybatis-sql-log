package com.mybatis.spring.boot.autoconfigure;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;


@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MybatisSqlPrintAutoConfiguration {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    /**
     * 兼容一下 PageHelper，让拦截器在最后一个处理 {@literal https://github.com/pagehelper/pagehelper-spring-boot}
     * 或者通过原生的进行处理
     */
    @Configuration
    @ConditionalOnExpression("${mybatis.print:true}")
    @ConditionalOnMissingClass({"com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration"})
    public class SupportPageHelper {

        @PostConstruct
        public void addPrintInterceptor() {
            MybatisSqlCompletePrintInterceptor printInterceptor = new MybatisSqlCompletePrintInterceptor();
            for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
                sqlSessionFactory.getConfiguration().addInterceptor(printInterceptor);
            }
        }
    }

    /**
     * sql 打印需要在拦截器最后一个才能统计 {@literal https://github.com/pagehelper/pagehelper-spring-boot}
     */
    @Configuration
    @ConditionalOnClass({PageHelperAutoConfiguration.class})
    @AutoConfigureAfter(PageHelperAutoConfiguration.class)
    @ConditionalOnExpression("${mybatis.print:true}")
    public class AutoConfigPrintInterceptor {
        @PostConstruct
        public void addPrintInterceptor() {
            MybatisSqlCompletePrintInterceptor printInterceptor = new MybatisSqlCompletePrintInterceptor();
            for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
                sqlSessionFactory.getConfiguration().addInterceptor(printInterceptor);
            }
        }
    }


}
