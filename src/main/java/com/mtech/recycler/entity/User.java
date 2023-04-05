package com.mtech.recycler.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.mtech.recycler.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "User")
public class User {

    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String userName;

    @DynamoDBAttribute
    private String email;

    @DynamoDBAttribute
    private String password;

    private String token;

    @DynamoDBAttribute
    private String firstName;

    @DynamoDBAttribute
    private String lastName;

    @DynamoDBAttribute
    @DynamoDBTypeConvertedEnum
    private Role role;

}
