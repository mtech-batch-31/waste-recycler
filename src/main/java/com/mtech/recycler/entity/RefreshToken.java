package com.mtech.recycler.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "RefreshToken")
public class RefreshToken {

    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBAttribute
    private String refreshToken;

    @DynamoDBAttribute
    private Date tokenExpiryDateTime;
}
