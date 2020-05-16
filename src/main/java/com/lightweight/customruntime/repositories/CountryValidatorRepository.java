package com.lightweight.customruntime.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.context.support.GenericApplicationContext;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

public class CountryValidatorRepository {

	private final GenericApplicationContext context;

	public CountryValidatorRepository(GenericApplicationContext context) {
		this.context = context;
	}

	public String findById(String id) {
		Table table = getTable();

		HashMap<String, String> nameMap = new HashMap<String, String>();
		nameMap.put("#Id", "Id");

		HashMap<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put(":Id", id);

		QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#Id = :Id").withNameMap(nameMap)
				.withValueMap(valueMap);

		List<Item> itemList = new ArrayList<>();
		table.query(querySpec).forEach(item -> {
			itemList.add(item);
		});

		Optional<Item> item = itemList.stream().findFirst();
		return item.isPresent() ? item.get().getString("Validity") : null;
	}

	private Table getTable() {
		AmazonDynamoDB client = context.getBean(AmazonDynamoDB.class);

		DynamoDB dynamoDB = new DynamoDB(client);

		return dynamoDB.getTable("country_validator");
	}
}