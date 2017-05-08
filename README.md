[![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0)
[![Build Status](https://travis-ci.org/dellemc-symphony/application-performance-monitoring.svg?branch=master)](https://travis-ci.org/dellemc-symphony/application-performance-monitoring)
# application-performance-monitoring
## Description
This module demonstrates an integration of the Service Now REST APIs into Symphony.
## Documentation
## API overview
## Before you begin

Note, you will need to either download or build the following other Symphony containers before using this demo (git clone, mvn install):  
* cpsd-core-capability-registry-service
* cpsd-rackhd-adapter-service
* cpsd-vcenter-adapter-service
* cpsd-core-endpoint-registry-service
* cpsd-coprhd-adapter-service  

## Building
To compile the code and then create a Docker image you will need:
* Java 8+ SDK
* maven (including ~/.m2/settings.xml providing relevant repos)
* docker daemon
(note that currently maven relies on internal DellEMC repositories for build artifacts)  
  
```
mvn compile -U clean  
mvn install
```  

## Packaging
The code is packaged in a jar file inside a Docker container.  
## Deploying
The docker image will need to connect to a RabbitMQ service on 5672 and to a capability registry.

The application is designed to create an alert when called by Nagios:

example commands.cfg entry to create the new handler:

```
# command to launch apm-nagios
define command{
	command_name	notify-symphony
	command_line	/opt/dell/cpsd/apm-nagios/bin/run.sh "\"$HOSTALIAS$\"" "\"$SERVICEDESC$\""
	}
```

# example host config using the handler:

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

Now when alerts are triggered a message will be placed on the Symphony bus and can be actioned by other systems subscribed to this type of message.


## Contributing
## Community
