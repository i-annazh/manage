package com.nh.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@ComponentScan
@SpringBootApplication
public class App extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer
{
	
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
        System.out.println( "Hello World!" );
    }

	@Override
	public void customize(ConfigurableEmbeddedServletContainer arg0) {
		// TODO Auto-generated method stub
		arg0.setPort(8081); 
	}


}
