package com.yaazhtech.accountmanagement.runner;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.yaazhtech.accountmanagement.config.DynamoDBConfig;
import com.yaazhtech.accountmanagement.data.PupilAccount;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Log
@Import({PropertyPlaceholderAutoConfiguration.class, DynamoDBConfig.class})
public class DynamoTableService {

    private final AmazonDynamoDB amazonDynamoDB;

    private final DynamoDBMapper mapper;
    private final List<Class> modelClasses;
    private boolean isTestTableCreated;

    @Autowired
    public DynamoTableService(DynamoDBMapper mapper, AmazonDynamoDB amazonDynamoDB) {
        this.mapper = mapper;
        this.amazonDynamoDB = amazonDynamoDB;

        modelClasses = new ArrayList<>();
        modelClasses.add(PupilAccount.class);

    }

    public void initializeDynamoDbTables() throws Exception {
        for (Class clazz : modelClasses) {
            if (clazz.getName().equalsIgnoreCase("com.yaazhtech.accountmanagement.data.pupilAccount")) {
                log.info("inside initializeDynamoDbTables:{}");
                createTable("pupilAccount", "category", "createdAt", "pupilEmail");
            }



        }

        log.info("Current DynamoDB tables are: ");
        listDynamoDbTables().forEach(log::info);
    }


    private void createTable(String tableName, String hashKey, String rangeKey, String secondaryIndex) throws Exception {

        log.info("Creating DynamoDB table for " + tableName);

        ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();

        attributeDefinitions.add(new AttributeDefinition().withAttributeName(hashKey).withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName(rangeKey).withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName(secondaryIndex).withAttributeType("S"));

        // Key schema for table
        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement().withAttributeName(hashKey).withKeyType(KeyType.HASH)); // Partition
        // key
        tableKeySchema.add(new KeySchemaElement().withAttributeName(rangeKey).withKeyType(KeyType.RANGE)); // Sort

        // Initial provisioned throughput settings for the indexes
        ProvisionedThroughput ptIndex = new ProvisionedThroughput().withReadCapacityUnits(1L)
                .withWriteCapacityUnits(1L);

        // CreateDateIndex
        GlobalSecondaryIndex userNameIndex = new GlobalSecondaryIndex().withIndexName(secondaryIndex + "index")
                .withProvisionedThroughput(ptIndex)
                .withKeySchema(new KeySchemaElement().withAttributeName(secondaryIndex).withKeyType(KeyType.HASH)) // Partition
                // key
                .withProjection(new Projection().withProjectionType("ALL"));

        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
                .withProvisionedThroughput(
                        new ProvisionedThroughput().withReadCapacityUnits((long) 1).withWriteCapacityUnits((long) 1))
                .withAttributeDefinitions(attributeDefinitions).withKeySchema(tableKeySchema)
                .withGlobalSecondaryIndexes(userNameIndex);

        System.out.println("Creating table " + tableName + "...");
        //amazonDynamoDB.createTable(createTableRequest);
        isTestTableCreated = TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);

        if (isTestTableCreated) {
            log.info("Created DynamoDB table for " + createTableRequest.getTableName());
        } else {
            log.info("Table already exists for " + createTableRequest.getTableName());
        }

        TableUtils.waitUntilActive(amazonDynamoDB, createTableRequest.getTableName());

        log.info("Table " + createTableRequest.getTableName() + " is active");
    }


    private List<String> listDynamoDbTables() {

        ListTablesResult tablesResult = amazonDynamoDB.listTables();

        return new ArrayList<>(tablesResult.getTableNames());
    }
}


