/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.apm.nagios.services;



/**
 * Application Performance custom exception class.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
public class ApplicationPerformanceException extends Exception
{
    /*
     * The serial version identifier.
     */
    private static final long serialVersionUID = 13264591L;

    /**
     * ApplicationPerformanceException constructor.
     *
     * @param message The exception message.
     * @since 0.1
     */
    public ApplicationPerformanceException(final String message)
    {
        super(message);
    }

    /**
     * ApplicationPerformanceException constructor.
     *
     * @param cause The cause of the exception.
     * @since 0.1
     */
    public ApplicationPerformanceException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * ApplicationPerformanceException constructor.
     *
     * @param message The exception message.
     * @param cause   The cause of the exception.
     * @since 0.1
     */
    public ApplicationPerformanceException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}

