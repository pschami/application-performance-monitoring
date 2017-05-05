package com.dell.cpsd.apm.nagios;

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

		String impactedSystem = "vxrack 0";
		String errorMessage = "CPU % Ready remains above 10% for 6 consecutive 10-minute periods.";

		for (int i = 0; i < args.length; i++) {
			if (i == 0) {
				impactedSystem = args[i];
			}

			if (i == 1) {
				errorMessage = args[i];
			}
		}

		ApplicationContext applicationContext = new SpringApplicationBuilder().sources(Application.class)
				.bannerMode(Banner.Mode.OFF).run(args);

		final ApplicationPerformanceProducer producer = applicationContext
				.getBean(ApplicationPerformanceProducer.class);

		ApplicationPerformanceEvent event = new ApplicationPerformanceEvent("high", uuid.toString(), new Date(),
				impactedSystem, errorMessage);

		producer.publishApplicationPerformanceEvent(event);

	}

}
