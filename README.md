[![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0)
[![Build Status](https://travis-ci.org/dellemc-symphony/application-performance-monitoring.svg?branch=master)](https://travis-ci.org/dellemc-symphony/application-performance-monitoring)
# application-performance-monitoring
## Description
This repository demonstrates an integration of the ServiceNow REST APIs into Project Symphony.
## Documentation
You can find additional documentation for Project Symphony at [dellemc-symphony.readthedocs.io][documentation].

## Before you begin
Before using this demo, download or build the following Project Symphony containers (git clone, mvn install):
 
* cpsd-core-capability-registry-service
* cpsd-rackhd-adapter-service
* cpsd-vcenter-adapter-service
* cpsd-core-endpoint-registry-service
* cpsd-coprhd-adapter-service  

Make sure the following is installed:

* Java Development Kit (version 8)
* Apache Maven 3.0.5+ (including ~/.m2/settings.xml providing relevant repositories)
* Docker daemon
* RabbitMQ  3.6.6 

## Building
Run the following commands to compile the code and create a Docker image:
  
```
mvn compile -U clean  
mvn install
```  

The code is packaged in a JAR file inside a Docker container.

## Deploying
The Docker image needs to connect to a RabbitMQ service on port 5672 and to a capability registry.

The application is designed to create an alert when called by Nagios.

**Example commands.cfg entry to create the new handler:**

```
# command to launch apm-nagios
define command{
	command_name	notify-symphony
	command_line	/opt/dell/cpsd/apm-nagios/bin/run.sh "\"$HOSTALIAS$\"" "\"$SERVICEDESC$\""
	}
```

**Example host config using the handler:**

```
# Define the host that we'll be monitoring

define host{
	host_name	test-cluster		; The name we're giving to this host
	alias		VMware vSphere Cluster	; A longer name associated with the host
	address		localhost		; IP address of the host
	hostgroups	symphony		; Host groups this is associated with
	max_check_attempts	10
	}

# Create a new hostgroup for symphony

define hostgroup{
	hostgroup_name	symphony		; The name of the hostgroup
	alias		VMware vSphere		; Long name of the group
	}

# Create a service to PING to host

define service{
	use			generic-service	; Inherit values from a template
	host_name		test-cluster	; The name of the host the service is associated with
	service_description	PING		; The service description
	check_command		check_ping!200.0,20%!600.0,60%	; The command used to monitor the service
	check_interval	5		; Check the service every 5 minutes under normal conditions
	retry_interval	1		; Re-check the service every minute until its final/hard state is determined
	event_handler	notify-symphony	; Use the new handler that we created above
	}
```

When alerts are triggered, a message is placed on the AMQP service bus and can be actioned by other systems subscribed to this type of message.

## Contributing
Project Symphony is a collection of services and libraries housed at [GitHub][github].
Contribute code and make submissions at the relevant GitHub repository level.

See [our documentation][contributing] for details on how to contribute.
## Community
Reach out to us on the Slack [#symphony][slack] channel. Request an invite at [{code}Community][codecommunity].

You can also join [Google Groups][googlegroups] and start a discussion.


[slack]: https://codecommunity.slack.com/messages/symphony
[googlegroups]: https://groups.google.com/forum/#!forum/dellemc-symphony
[codecommunity]: http://community.codedellemc.com/
[contributing]: http://dellemc-symphony.readthedocs.io/en/latest/contributingtosymphony.html
[github]: https://github.com/dellemc-symphony
[documentation]: https://dellemc-symphony.readthedocs.io/en/latest/
