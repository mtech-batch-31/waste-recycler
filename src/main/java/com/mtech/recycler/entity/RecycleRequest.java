package com.mtech.recycler.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.mtech.recycler.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "RecycleRequest")
public class RecycleRequest {

    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String contactPerson;

    @DynamoDBAttribute
    private String contactNumber;

    @DynamoDBAttribute
    private List<Category> recycleItemList;

}
