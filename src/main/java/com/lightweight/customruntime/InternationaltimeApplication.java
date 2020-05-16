package com.lightweight.customruntime;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.function.context.FunctionType;
import org.springframework.cloud.function.context.FunctionalSpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.lightweight.customruntime.conf.AppConfiguration;
import com.lightweight.customruntime.conf.DynamoDBConfig;
import com.lightweight.customruntime.function.WorlTimeFunction;
import com.lightweight.customruntime.model.Country;
import com.lightweight.customruntime.model.WorldTime;
import com.lightweight.customruntime.repositories.CountryValidatorRepository;

@SpringBootConfiguration
public class InternationaltimeApplication implements ApplicationContextInitializer<GenericApplicationContext> {

	public static void main(String[] args) {
		FunctionalSpringApplication.run(InternationaltimeApplication.class, args);
	}

	@Override
	public void initialize(GenericApplicationContext context) {

		context.registerBean(AppConfiguration.class.getName(), AppConfiguration.class,
				() -> new AppConfiguration(context));

		context.registerBean(DynamoDBConfig.class.getName(), DynamoDBConfig.class, () -> new DynamoDBConfig());

		context.registerBean(RestTemplate.class.getName(), RestTemplate.class,
				() -> context.getBean(AppConfiguration.class).getRestTemplate());

		context.registerBean(AmazonDynamoDB.class.getName(), AmazonDynamoDB.class,
				() -> context.getBean(DynamoDBConfig.class).amazonDynamoDB());

		context.registerBean(AWSCredentials.class.getName(), AWSCredentials.class,
				() -> context.getBean(DynamoDBConfig.class).amazonAWSCredentials());

		context.registerBean(CountryValidatorRepository.class.getName(), CountryValidatorRepository.class,
				() -> new CountryValidatorRepository(context));

		context.registerBean("worldTimeFunction", FunctionRegistration.class,
				() -> new FunctionRegistration<>(new WorlTimeFunction(context))
						.type(FunctionType.from(Country.class).to(WorldTime.class).message()));
	}
}