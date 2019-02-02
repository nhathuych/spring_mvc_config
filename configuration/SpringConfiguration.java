package com.nhathuy.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.nhathuy.controller", "com.nhathuy.dao", "com.nhathuy.service" })
@EnableTransactionManagement
public class SpringConfiguration implements WebMvcConfigurer {

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean(name = "multipartResolver")
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxUploadSize(-1); // truyền -1 vô là nó hiểu ko giới hạn kích thước file

		return commonsMultipartResolver;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/bstore");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");

		return dataSource;
	}

	// cấu hình hibernate cho mysql
	@Bean
	public LocalSessionFactoryBean localSessionFactoryBean() {
		LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
		bean.setDataSource(dataSource());
		bean.setPackagesToScan("com.nhathuy.entity");

		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		hibernateProperties.put("hibernate.show_sql", true);

		bean.setHibernateProperties(hibernateProperties);

		return bean;
	}

	@Bean(name = "transactionManager")
	@Autowired
	public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(sessionFactory);

		return hibernateTransactionManager;
	}

//	// xài hibernate rồi thì bỏ cái này
//
//	@Bean
//	public JdbcTemplate jdbcTemplate() {
//		return new JdbcTemplate(dataSource());
//	}
//
//	// @transactional dùng để quản lý giao dịch, đảm bảo nếu truy vấn thất bại thì rollback
//	// khi truy vấn nhiều lệnh sql, nếu câu truy vấn nào có lỗi thì rollback lại đảm bảo toàn vẹn dữ liệu
//	@Bean(name="dataSourceTransactionManager")
//	public DataSourceTransactionManager dataSourceTransactionManager() {
//		return new DataSourceTransactionManager(dataSource());
//	}
}
