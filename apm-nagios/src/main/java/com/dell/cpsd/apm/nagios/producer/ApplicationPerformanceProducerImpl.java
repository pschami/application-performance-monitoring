/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.apm.nagios.producer;

import com.dell.cpsd.apm.nagios.api.ApplicationPerformanceEvent;
import com.dell.cpsd.apm.nagios.config.ApplicationPerformanceRabbitConfig;
import com.dell.cpsd.apm.nagios.services.ApplicationPerformanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Endpoint registration Producer implementation class.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public class ApplicationPerformanceProducerImpl implements ApplicationPerformanceProducer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationPerformanceProducerImpl.class);
    private Exchange       applicationPerformanceEventExchange;
    private RabbitTemplate rabbitTemplate;

    /**
     * @param rabbitTemplate                    Rabbit Template
     * @param applicationPerformanceEventExchange Event Exchange
     */
    public ApplicationPerformanceProducerImpl(final RabbitTemplate rabbitTemplate, final Exchange applicationPerformanceEventExchange)
    {
        this.applicationPerformanceEventExchange = applicationPerformanceEventExchange;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishApplicationPerformanceEvent(ApplicationPerformanceEvent event) throws ApplicationPerformanceException
    {
        if (event == null)
        {
            throw new ApplicationPerformanceException("Endpoint discovered message is null.");
        }

        rabbitTemplate.convertAndSend(applicationPerformanceEventExchange.getName(),
                ApplicationPerformanceRabbitConfig.ROUTING_KEY_APPLICATION_PERFORMANCE, event);

    }
    
}
    

