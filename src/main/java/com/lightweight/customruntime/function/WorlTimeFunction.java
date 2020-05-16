package com.lightweight.customruntime.function;

import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.client.RestTemplate;

import com.lightweight.customruntime.model.Country;
import com.lightweight.customruntime.model.WorldTime;
import com.lightweight.customruntime.repositories.CountryValidatorRepository;

public class WorlTimeFunction implements Function<GenericMessage<Country>, GenericMessage<WorldTime>> {

	private static Log logger = LogFactory.getLog(WorlTimeFunction.class);

	private final GenericApplicationContext context;

	public WorlTimeFunction(GenericApplicationContext context) {
		this.context = context;
	}

	@Override
	public GenericMessage<WorldTime> apply(GenericMessage<Country> value) {
		logger.info("Processing: " + value.getPayload());
		// logger.info("repository : " + Arrays.asList(context.getBeanDefinitionNames()));
		return new GenericMessage<WorldTime>(getWorldTime(value.getPayload().getCountry()), value.getHeaders());
	}

	private WorldTime getWorldTime(String country) {
		CountryValidatorRepository repository = context.getBean(CountryValidatorRepository.class);
		String validity = repository.findById(country);
		logger.info("Validity: " + validity);
		if (!"Valid".equals(validity)) {
			throw new RuntimeException("Invalid Country");
		}
		Environment env = context.getEnvironment();
		RestTemplate restTemplate = context.getBean(RestTemplate.class);
		WorldTime worldTime = restTemplate.getForObject(env.getProperty("aws.weather.gateway.url"), WorldTime.class,
				country);
		return worldTime;
	}
}
