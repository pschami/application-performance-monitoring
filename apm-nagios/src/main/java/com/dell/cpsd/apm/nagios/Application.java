package com.dell.cpsd.apm.nagios; /**
									* Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
									*/

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import com.dell.cpsd.apm.nagios.api.ApplicationPerformanceEvent;
import com.dell.cpsd.apm.nagios.producer.ApplicationPerformanceProducer;
import com.dell.cpsd.apm.nagios.services.ApplicationPerformanceException;
import com.dell.cpsd.hdp.capability.registry.client.binding.config.CapabilityRegistryBindingManagerConfig;

/**
 * Spring boot application class
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
@EnableAsync
@Import(CapabilityRegistryBindingManagerConfig.class)
public class Application extends AsyncConfigurerSupport {
	
	public static int count = 0;
	public static UUID uuid = UUID.randomUUID();
	
	public static void main(String[] args) throws Exception {

		ApplicationContext applicationContext = new SpringApplicationBuilder().sources(Application.class)
				.bannerMode(Banner.Mode.LOG).run(args);

		final ApplicationPerformanceProducer producer = applicationContext
				.getBean(ApplicationPerformanceProducer.class);

		
			
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {				
					count++;				
					
					ApplicationPerformanceEvent event = new ApplicationPerformanceEvent("high",
							uuid.toString(), new Date(), "patricks-pc", "the message from nagios");

					producer.publishApplicationPerformanceEvent(event);
					
					if (count > 5) {
						count = 0;
						uuid = UUID.randomUUID();						
					}						
					
				} catch (ApplicationPerformanceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		}, 5, 5, TimeUnit.SECONDS);
	}

}
