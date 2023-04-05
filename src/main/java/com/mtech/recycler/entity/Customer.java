package com.mtech.recycler.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Customer")
public class Customer extends User {

    @DynamoDBAttribute
    private String contactNumber;

    @DynamoDBAttribute
    private String address;

    @DynamoDBAttribute
    private String postalCode;


    @Override
    public String toString() {
        return "Customer{" +
                "id='" + this.getId() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                ", password='" + this.getPassword() + '\'' +
                ", token='" + this.getToken() + '\'' +
                ", firstName='" + this.getFirstName() + '\'' +
                ", lastName='" + this.getLastName() + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
