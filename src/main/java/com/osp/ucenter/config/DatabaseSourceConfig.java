package com.osp.ucenter.config;

import java.sql.SQLException;
import java.util.Arrays;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import com.osp.ucenter.common.model.DruidDatasource;

/**
 * mybatis与druid整合
 * 
 * @author zhangmingcheng
 */
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(DruidDatasource.class)
@MapperScan("com.osp.ucenter.persistence.dao")
/**  
 * Druid的DataResource配置类  
 * 凡是被Spring管理的类，实现接口 EnvironmentAware 重写方法 setEnvironment 可以在工程启动时，  
 * 获取到系统环境变量和application配置文件中的变量。 还有一种方式是采用注解的方式获取 @value("${变量的key值}")  
 * 获取application配置文件中的变量。 这里采用第一种要方便些  
 * Created by sun on 2017-1-20.  
 */  
public class DatabaseSourceConfig implements EnvironmentAware {

	/**
	 * 注入自定义druid数据库连接池配置属性
	 */
	@Autowired
	private DruidDatasource druidDatasource;

	private Environment environment;
	private RelaxedPropertyResolver propertyResolver;

	public void setEnvironment(Environment environment) {
		this.environment = environment;
		// 使用RelaxedPropertyResolver获取yaml中的数据库连接配置信息
		this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
	}

	@Bean(initMethod = "init", destroyMethod = "close")
	public DruidDataSource dataSource() throws SQLException {
		if (StringUtils.isEmpty(propertyResolver.getProperty("url"))) {
			System.out.println("Your database connection pool configuration is incorrect!"
					+ " Please check your Spring profile, current profiles are:"
					+ Arrays.toString(environment.getActiveProfiles()));
			throw new ApplicationContextException("Database connection pool is not configured correctly");
		}
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(propertyResolver.getProperty("driver-class-name"));
		dataSource.setUrl(propertyResolver.getProperty("url"));
		dataSource.setUsername(propertyResolver.getProperty("username"));
		dataSource.setPassword(propertyResolver.getProperty("password"));
		dataSource.setMaxActive(druidDatasource.getMaxActive());
		dataSource.setInitialSize(druidDatasource.getInitialSize());
		dataSource.setMinIdle(druidDatasource.getMinIdle());
		dataSource.setInitialSize(druidDatasource.getInitialSize());
		dataSource.setQueryTimeout(druidDatasource.getQueryTimeout());
		dataSource.setTransactionQueryTimeout(druidDatasource.getTransactionQueryTimeout());
		dataSource.setRemoveAbandoned(druidDatasource.getRemoveAbandoned());
		dataSource.setRemoveAbandonedTimeout(druidDatasource.getRemoveAbandonedTimeout());
		dataSource.setPoolPreparedStatements(druidDatasource.getPoolPreparedStatements());
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(druidDatasource.getMaxPoolPreparedStatementPerConnectionSize());
		dataSource.setMaxWait(druidDatasource.getMaxWait());
		dataSource.setTimeBetweenEvictionRunsMillis(druidDatasource.getTimeBetweenEvictionRunsMillis());
		dataSource.setMinEvictableIdleTimeMillis(druidDatasource.getMinEvictableIdleTimeMillis());
		dataSource.setValidationQuery(druidDatasource.getValidationQuery());
	    dataSource.setTestOnBorrow(druidDatasource.isTestOnBorrow());
	    dataSource.setTestWhileIdle(druidDatasource.isTestWhileIdle());
	    dataSource.setTestOnReturn(druidDatasource.isTestOnReturn());
	    dataSource.setLogAbandoned(druidDatasource.isLogAbandoned());
		return dataSource;
	}

	/**
	 * 可在此处注入mybatis分页插件
	 * @return
	 * @throws Exception
	 */
	@Bean
	@ConditionalOnMissingBean  
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver
                .getResources("classpath:com/osp/ucenter/persistence/dao/mapping/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
		return sqlSessionFactoryBean.getObject();
	}
    @Bean
    @ConditionalOnMissingBean  
	public PlatformTransactionManager platformTransactionManager() throws SQLException {
		return new DataSourceTransactionManager(dataSource());
	}

	public DruidDatasource getDruidDatasource() {
		return druidDatasource;
	}

	public void setDruidDatasource(DruidDatasource druidDatasource) {
		this.druidDatasource = druidDatasource;
	}
}