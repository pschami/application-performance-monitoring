/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.cpsd.apm.nagios.producer;

import com.dell.cpsd.apm.nagios.api.ApplicationPerformanceEvent;
import com.dell.cpsd.apm.nagios.services.ApplicationPerformanceException;

/**
 * Application Performance Producer Interface
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
public interface ApplicationPerformanceProducer
{
    /**
     * This method publishes an Application Performance event. 
     *
     * @param event Application Performance Event
     * @throws ApplicationPerformanceException  Exception
     */
    void publishApplicationPerformanceEvent(final ApplicationPerformanceEvent event) throws ApplicationPerformanceException;

   
}
