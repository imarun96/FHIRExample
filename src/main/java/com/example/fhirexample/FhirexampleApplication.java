package com.example.fhirexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRegistration;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class FhirexampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(FhirexampleApplication.class, args);
	}
	@Bean
	public ServletRegistrationBean servletRegistrationBean(){
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new SimpleRestfulServer(), "/api/*");
		registrationBean.setName("FhirServlet");
		return registrationBean;
	}
}