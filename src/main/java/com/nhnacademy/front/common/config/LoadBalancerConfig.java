package com.nhnacademy.front.common.config;

import java.util.List;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import reactor.core.publisher.Flux;

@Configuration
public class LoadBalancerConfig {

	private static final String LOCALHOST = "localhost";
	private static final String SERVICE_ID = "gateway-service";

	@Profile("prod")
	@Bean
	public ServiceInstanceListSupplier serviceInstanceListSupplierProd(ConfigurableApplicationContext context) {
		return ServiceInstanceListSupplier.builder()
			.withBase(new ServiceInstanceListSupplier() {

				@Override
				public String getServiceId() {
					return SERVICE_ID;
				}

				@Override
				public Flux<List<ServiceInstance>> get() {
					List<ServiceInstance> instances = List.of(
						new DefaultServiceInstance(SERVICE_ID + "-1", SERVICE_ID, LOCALHOST, 10232, false),
						new DefaultServiceInstance(SERVICE_ID + "-2", SERVICE_ID, LOCALHOST, 10233, false)
					);
					return Flux.just(instances);
				}
			})
			.build(context);
	}


	@Profile("dev")
	@Bean
	public ServiceInstanceListSupplier serviceInstanceListSupplierDev(ConfigurableApplicationContext context) {
		return ServiceInstanceListSupplier.builder()
			.withBase(new ServiceInstanceListSupplier() {

				@Override
				public String getServiceId() {
					return SERVICE_ID;
				}

				@Override
				public Flux<List<ServiceInstance>> get() {
					List<ServiceInstance> instances = List.of(
						new DefaultServiceInstance(SERVICE_ID + "-1", SERVICE_ID, LOCALHOST, 10232, false)
					);
					return Flux.just(instances);
				}
			})
			.build(context);
	}
}
