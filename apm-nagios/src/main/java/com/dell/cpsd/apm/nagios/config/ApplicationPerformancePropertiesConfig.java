/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.apm.nagios.config;

import com.dell.cpsd.common.rabbitmq.config.RabbitMQPropertiesConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 *  Application Performance properties config.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
@Configuration
@PropertySources({@PropertySource(value = "classpath:META-INF/spring/apm-nagios/rabbitmq.properties"),
        @PropertySource(value = "file:/opt/dell/cpsd/registration-services/apm-nagios/conf/rabbitmq-config.properties", ignoreResourceNotFound = true)})
@Qualifier("rabbitPropertiesConfig")
public class ApplicationPerformancePropertiesConfig extends RabbitMQPropertiesConfig
{
    /**
     * Application Performance Properties Config constructor
     */
    public ApplicationPerformancePropertiesConfig()
    {
        super();
    }
}

