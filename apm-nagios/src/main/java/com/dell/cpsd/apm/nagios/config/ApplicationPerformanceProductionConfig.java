/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.apm.nagios.config;

import com.dell.cpsd.common.rabbitmq.connectors.RabbitMQCachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Application Performance production config.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
@Configuration
@Import(ApplicationPerformancePropertiesConfig.class)
public class ApplicationPerformanceProductionConfig
{
    public static final    String CONFIG_PACKAGE = "com.dell.cpsd.apm";
    protected static final String PROFILE        = "production";

    @Autowired
    private ApplicationPerformancePropertiesConfig propertiesConfig;

    /**
     * @return The <code>ConnectionFactory</code> to use.
     * @since SINCE-TBD
     */
    @Bean
    @Qualifier("rabbitConnectionFactory")
    public ConnectionFactory productionCachingConnectionFactory()
    {
        final com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();
        return new RabbitMQCachingConnectionFactory(connectionFactory, propertiesConfig);
    }
}

