package com.yaazhtech.accountmanagement.runner;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Log
@ConditionalOnProperty(prefix = "app.init-db", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamoDBInitializer implements CommandLineRunner {

    private final DynamoTableService dynamoTableService;

    @Autowired
    public DynamoDBInitializer(DynamoTableService dynamoTableService) {
        this.dynamoTableService = dynamoTableService;
    }

    @Override
    public void run(String... args) throws Exception {

        dynamoTableService.initializeDynamoDbTables();
    }
}

