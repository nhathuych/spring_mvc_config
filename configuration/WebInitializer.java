package com.nhathuy.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class WebInitializer implements WebApplicationInitializer {

	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(SpringConfiguration.class);
		context.setServletContext(servletContext);
		
		DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		
		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dispatcherServlet);
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
		
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("UTF-8", true);
		servletContext.addFilter("encodingFilter", characterEncodingFilter).addMappingForUrlPatterns(null, false, "/*");
	}
}
