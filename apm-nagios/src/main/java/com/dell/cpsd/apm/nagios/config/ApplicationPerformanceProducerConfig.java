/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.apm.nagios.config;

import com.dell.cpsd.apm.nagios.producer.ApplicationPerformanceProducer;
import com.dell.cpsd.apm.nagios.producer.ApplicationPerformanceProducerImpl;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This the Application Performance producer spring config required
 * for instantiating the producer instance.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
@Configuration
@ComponentScan(ApplicationPerformanceProductionConfig.CONFIG_PACKAGE)
public class ApplicationPerformanceProducerConfig
{
    /**
     * The Spring RabbitMQ template.
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Exchange applicationPerformanceEventExchange;

    @Autowired
    private String hostName;

    @Bean
    ApplicationPerformanceProducer  applicationPerformancenProducer()
    {
        return new ApplicationPerformanceProducerImpl(rabbitTemplate, applicationPerformanceEventExchange);
    }
  
}

