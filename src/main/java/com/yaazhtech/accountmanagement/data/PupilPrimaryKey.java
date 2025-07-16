package com.yaazhtech.accountmanagement.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import lombok.Data;



@Data
public class PupilPrimaryKey  {

    @DynamoDBHashKey
    private String category;
    @DynamoDBRangeKey
    private String createdAt;
    @DynamoDBIndexHashKey(attributeName = "pupilEmail", globalSecondaryIndexName = "pupilEmail")
    private String pupilEmail;
}
