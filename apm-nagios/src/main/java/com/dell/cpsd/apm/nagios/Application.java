/**
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 */
package com.dell.cpsd.apm.nagios;

import java.util.Date;
import java.util.UUID;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import com.dell.cpsd.apm.nagios.api.ApplicationPerformanceEvent;
import com.dell.cpsd.apm.nagios.producer.ApplicationPerformanceProducer;
import com.dell.cpsd.hdp.capability.registry.client.binding.config.CapabilityRegistryBindingManagerConfig;

/**
 * Application Performance Alerting Application
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
@SpringBootApplication
@EnableAsync
@Import(CapabilityRegistryBindingManagerConfig.class)
public class Application extends AsyncConfigurerSupport {

	public static void main(String[] args) throws Exception {

		// alert default values

		String severity = "high";
		String alertId = UUID.randomUUID().toString();
		String impactedSystem = "vxrack 0";
		String errorMessage = "CPU % Ready remains above 10% for 6 consecutive 10-minute periods.";

		//read in the arguments

		for (int i = 0; i < args.length; i++) {
			if (i == 0) {
				impactedSystem = args[i];
			}

			if (i == 1) {
				errorMessage = args[i];
			}
		}
		
		//start the application

		ApplicationContext applicationContext = new SpringApplicationBuilder().sources(Application.class)
				.bannerMode(Banner.Mode.OFF).run(args);

		//publish the event
		
		final ApplicationPerformanceProducer producer = applicationContext
				.getBean(ApplicationPerformanceProducer.class);

		ApplicationPerformanceEvent event = new ApplicationPerformanceEvent(severity, alertId, new Date(),
				impactedSystem, errorMessage);

		producer.publishApplicationPerformanceEvent(event);

	}

}
