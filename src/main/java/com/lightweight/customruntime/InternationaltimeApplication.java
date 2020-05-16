package com.lightweight.customruntime;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.function.context.FunctionType;
import org.springframework.cloud.function.context.FunctionalSpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.client.RestTemplate;

import com.lightweight.customruntime.conf.AppConfiguration;
import com.lightweight.customruntime.function.WorlTimeFunction;
import com.lightweight.customruntime.model.Country;
import com.lightweight.customruntime.model.WorldTime;

@SpringBootConfiguration
public class InternationaltimeApplication implements ApplicationContextInitializer<GenericApplicationContext> {

	public static void main(String[] args) {
		FunctionalSpringApplication.run(InternationaltimeApplication.class, args);
	}

	@Override
	public void initialize(GenericApplicationContext context) {

		context.registerBean(AppConfiguration.class.getName(), AppConfiguration.class,
				() -> new AppConfiguration(context));

		context.registerBean("restTemplate", RestTemplate.class,
				() -> context.getBean(AppConfiguration.class).getRestTemplate());

		context.registerBean("worldTimeFunction", FunctionRegistration.class,
				() -> new FunctionRegistration<>(new WorlTimeFunction(context))
						.type(FunctionType.from(Country.class).to(WorldTime.class).message()));
	}
}