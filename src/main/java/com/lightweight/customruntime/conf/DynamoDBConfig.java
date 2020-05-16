package com.lightweight.customruntime.conf;

import org.springframework.util.StringUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class DynamoDBConfig {

	private static final String AMAZON_DYNAMO_DB_ENDPOINT = "AMAZON_DYNAMO_DB_ENDPOINT";

	private static final String AMAZON_AWS_ACCESS_KEY = "AMAZON_AWS_ACCESS_KEY";

	private static final String AMAZON_AWS_SECRET_KEY = "AMAZON_AWS_SECRET_KEY";

	public AmazonDynamoDB amazonDynamoDB() {
		AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());
		if (!StringUtils.isEmpty(System.getenv(AMAZON_DYNAMO_DB_ENDPOINT))) {
			amazonDynamoDB.setEndpoint(System.getenv(AMAZON_DYNAMO_DB_ENDPOINT));
		}
		return amazonDynamoDB;
	}

	public AWSCredentials amazonAWSCredentials() {
		return new BasicAWSCredentials(System.getenv(AMAZON_AWS_ACCESS_KEY), System.getenv(AMAZON_AWS_SECRET_KEY));
	}
}