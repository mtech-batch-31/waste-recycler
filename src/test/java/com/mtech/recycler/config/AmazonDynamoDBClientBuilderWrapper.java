package com.mtech.recycler.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class AmazonDynamoDBClientBuilderWrapper {
    public AmazonDynamoDB build() {
        return AmazonDynamoDBClientBuilder.standard().build();
    }
}