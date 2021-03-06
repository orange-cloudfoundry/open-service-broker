package com.swisscom.cloud.sb.broker.cfextensions.serviceusage

import com.google.common.base.Optional
import com.google.common.base.Preconditions
import com.swisscom.cloud.sb.broker.model.ServiceInstance
import com.swisscom.cloud.sb.broker.services.common.ServiceProvider
import com.swisscom.cloud.sb.broker.services.common.ServiceProviderLookup
import com.swisscom.cloud.sb.model.usage.ServiceUsage
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@CompileStatic
class ServiceUsageLookup {
    private final ServiceProviderLookup serviceProviderLookup

    @Autowired
    ServiceUsageLookup(ServiceProviderLookup serviceProviderLookup) {
        this.serviceProviderLookup = serviceProviderLookup
    }

    ServiceUsage usage(ServiceInstance instance, Optional<Date> optionalEnddate) {
        Preconditions.checkNotNull(instance, "A valid ServiceInstance is required.")
        return findServiceUsageProvider(instance).findUsage(instance, optionalEnddate)
    }

    private ServiceUsageProvider findServiceUsageProvider(ServiceInstance serviceInstance) {
        ServiceProvider serviceProvider = serviceProviderLookup.findServiceProvider(serviceInstance.plan)
        if (!(serviceProvider instanceof ServiceUsageProvider)) {
            throw new RuntimeException("Service provider: ${serviceProvider.class.name} does not provide service usage information")
        }
        return serviceProvider as ServiceUsageProvider
    }
}
