package com.example.architecture.config;

import com.example.architecture.handler.ConverHandler;
import com.example.architecture.interceptor.MybatisDecryptInterceptor;
import com.example.architecture.interceptor.MybatisEncryptInterceptor;
import org.apache.ibatis.executor.loader.cglib.CglibProxyFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.StringTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class MybatisConfig {

    @Autowired
//    @Qualifier("dataSource")
    private DataSource dataSource;

//    @Autowired
//    @Qualifier("dataSourceOne")
//    private DataSource dataSourceOne;

//    @Autowired
//    @Qualifier("dataSourceTwo")
//    private DataSource dataSourcetwo;

    @Bean("sqlSessionFactory")
    SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        ResourcePatternResolver reslover = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean  factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeHandlers(new TypeHandler[]{new ConverHandler()});//注册自定义的handler
        factoryBean.setPlugins(new Interceptor[]{new MybatisEncryptInterceptor(),new MybatisDecryptInterceptor()});
//        factoryBean.setPlugins(new Interceptor[]{new MybatisEncryptInterceptor()});
        //手动管理数据源时，需要指定mapper路径
        //mapper集中在单个路径下时
        factoryBean.setMapperLocations(reslover.getResources("classpath:com/example/architecture/mapper/*.xml"));
        SqlSessionFactory factory = factoryBean.getObject();
//        factory.getConfiguration().setLazyLoadingEnabled(true);
//        factory.getConfiguration().setAggressiveLazyLoading(false);
//        factory.getConfiguration().setProxyFactory(new jdkf);
        return factory;
    }

    @Bean("sqlSession")
    SqlSessionTemplate sqlSessison() throws Exception {
        SqlSessionFactory sqlSessionFactory = sqlSessionFactory(dataSource);
        //TODO
        return new SqlSessionTemplate(sqlSessionFactory);
    }

//    @Bean("sqlSession")
//    SqlSession sqlSessison(DataSource dataSource) throws Exception {
//        SqlSessionFactory sqlSessionFactory = sqlSessionFactory(dataSource);
//        //TODO
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }

//    @Bean("sqlSessionOne")
//    SqlSession sqlSessionOne() throws Exception {
//        return sqlSessison(dataSourceOne);
//    }

//    @Bean("sqlSessionTwo")
//    SqlSession sqlSessionTwo() throws Exception {
//        return sqlSessison(dataSourcetwo);
//    }


}
