/**
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 */

package com.dell.cpsd.apm.nagios.capabilityintegration;

import static com.dell.cpsd.hdp.capability.registry.client.builder.AmqpProviderEndpointBuilder.AMQP_PROTOCOL;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.dell.cpsd.apm.nagios.config.ApplicationPerformanceRabbitConfig;
import com.dell.cpsd.hdp.capability.registry.api.Capability;
import com.dell.cpsd.hdp.capability.registry.api.Element;
import com.dell.cpsd.hdp.capability.registry.api.Identity;
import com.dell.cpsd.hdp.capability.registry.api.ProviderEndpoint;
import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryBindingManager;
import com.dell.cpsd.hdp.capability.registry.client.binding.amqp.producer.IAmqpCapabilityRegistryControlProducer;
import com.dell.cpsd.hdp.capability.registry.client.builder.AmqpProviderEndpointBuilder;
import com.dell.cpsd.service.common.client.context.IConsumerContextConfig;

/**
 * A class to handle the integration between the application performance monitor
 * and the Capability Registry.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
@Component
public class CapabilityRegistryIntegration {
	/**
	 * The logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CapabilityRegistryIntegration.class);

	/**
	 * The capability registry binding manager.
	 */
	private final ICapabilityRegistryBindingManager capabilityRegistryBindingManager;

	/**
	 * The consumer context for this data provider.
	 */
	private final IConsumerContextConfig consumerContextConfig;

	/**
	 * The capability registry producer;
	 */
	private final IAmqpCapabilityRegistryControlProducer capabilityRegistryControlProducer;

	/**
	 * The event exchange
	 */
	private final TopicExchange applicationPerformanceEventExchange;

	/**
	 * Constructor
	 * 
	 * @param capabilityRegistryBindingManager
	 * @param applicationPerformanceEventExchange
	 * @param consumerContextConfig
	 * @param capabilityRegistryControlProducer
	 */
	@Autowired
	public CapabilityRegistryIntegration(final ICapabilityRegistryBindingManager capabilityRegistryBindingManager,
			final TopicExchange applicationPerformanceEventExchange, final IConsumerContextConfig consumerContextConfig,
			final IAmqpCapabilityRegistryControlProducer capabilityRegistryControlProducer) {
		notNull(capabilityRegistryBindingManager, "capabilityRegistryBindingManager cannot be null");
		notNull(consumerContextConfig, "consumerContextConfig cannot be null");
		notNull(capabilityRegistryControlProducer, "capabilityRegistryControlProducer cannot be null");
		notNull(applicationPerformanceEventExchange, "applicationPerformanceEventExchange cannot be null");

		this.capabilityRegistryBindingManager = capabilityRegistryBindingManager;
		this.consumerContextConfig = consumerContextConfig;
		this.capabilityRegistryControlProducer = capabilityRegistryControlProducer;
		this.applicationPerformanceEventExchange = applicationPerformanceEventExchange;
	}

	/**
	 * Starts the client
	 */
	public void start() {
		register();

		this.capabilityRegistryBindingManager.start();
	}

	/**
	 * This is called when the application context is closed.
	 *
	 * @param event
	 *            The context closed event.
	 * @since 0.1
	 */
	@EventListener
	public void event(ContextClosedEvent event) throws Exception {
		this.unregister();

	}

	/**
	 * Register with the capability registry
	 */
	private void register() {
		// create the capability provider identity for this data provider
		final Identity identity = new Identity(consumerContextConfig.consumerName(),
				consumerContextConfig.consumerUuid());

		final List<Capability> capabilities = new ArrayList<>();
		capabilities.add(createLookupCapability());

		// register with the capability registry
		try {
			capabilityRegistryBindingManager.registerCapabilityProvider(identity, capabilities);
		} catch (CapabilityRegistryException exception) {
			LOGGER.error(exception.getMessage(), exception);

		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
		}

	}

	/**
	 * This unregisters the capabilities.
	 *
	 * @since 0.1
	 */
	private void unregister() {
		if ((this.consumerContextConfig != null) && (this.capabilityRegistryBindingManager != null)) {
			final Identity identity = new Identity(this.consumerContextConfig.consumerName(),
					this.consumerContextConfig.consumerUuid());

			try {
				this.capabilityRegistryBindingManager.stop();
				String correlationId = UUID.randomUUID().toString();
				this.capabilityRegistryControlProducer.publishUnregisterCapabilityProvider(correlationId, identity);
			} catch (CapabilityRegistryException exception) {
				LOGGER.error(exception.getMessage(), exception);
			}
		}
	}

	/**
	 * Creates new capability which can be registered
	 * 
	 * @return capability
	 */
	private Capability createLookupCapability() {
		// create the Application Performance event capability
		AmqpProviderEndpointBuilder endpointBuilder = new AmqpProviderEndpointBuilder(AMQP_PROTOCOL);

		// TODO: Extract these properties/capabilities.
		// TODO: Update to use event exchange instead of request/response
		endpointBuilder.requestExchange(applicationPerformanceEventExchange.getName());
		endpointBuilder.requestRoutingKey(ApplicationPerformanceRabbitConfig.ROUTING_KEY_APPLICATION_PERFORMANCE);
		endpointBuilder.requestMessageType("com.dell.cpsd.apm.event");

		endpointBuilder.responseExchange(applicationPerformanceEventExchange.getName());
		endpointBuilder.responseRoutingKey(ApplicationPerformanceRabbitConfig.ROUTING_KEY_APPLICATION_PERFORMANCE);
		endpointBuilder.responseMessageType("com.dell.cpsd.apm.event");

		final ProviderEndpoint providerEndpoint = endpointBuilder.build();
		final List<Element> elements = Collections.emptyList();

		return new Capability("application-performance-event", elements, providerEndpoint);
	}
}
