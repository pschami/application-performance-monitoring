package com.dell.cpsd.apm.nagios; /**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

import com.dell.cpsd.apm.nagios.api.ApplicationPerformanceEvent;
import com.dell.cpsd.apm.nagios.producer.ApplicationPerformanceProducer;
import com.dell.cpsd.apm.nagios.services.ApplicationPerformanceException;
import com.dell.cpsd.hdp.capability.registry.client.binding.config.CapabilityRegistryBindingManagerConfig;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Spring boot application class
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
@EnableAsync
@Import(CapabilityRegistryBindingManagerConfig.class)
public class Application extends AsyncConfigurerSupport
{
    public static void main(String[] args) throws Exception
    {
      
    	ApplicationContext applicationContext = new SpringApplicationBuilder().sources(Application.class).bannerMode(Banner.Mode.LOG).run(args);
    	
    	final ApplicationPerformanceProducer producer = applicationContext.getBean(ApplicationPerformanceProducer.class);
    	    		
    	Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				try {
					producer.publishApplicationPerformanceEvent(new ApplicationPerformanceEvent("test alert......"));
				} catch (ApplicationPerformanceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}, 5, 5, TimeUnit.SECONDS);
    }

}

