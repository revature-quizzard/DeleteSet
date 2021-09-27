package com.revature.delete_set;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SetRepo {
    private final DynamoDBMapper dbReader;

    public SetRepo() {
        dbReader = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
    }

    public boolean deleteSetById(String target_set_id) {
        //creating new set_document for query
        Set s = new Set();
        s.setId(target_set_id);
        //creating query using the set_document as a hash key value
        // limit of one for now just because i know ids are unique
        DynamoDBQueryExpression<Set> query =
                new DynamoDBQueryExpression<Set>()
                        .withHashKeyValues(s)
                        .withLimit(1);
        // storing resultant set in a list
        List<Set> target_set = dbReader.query(Set.class , query);
        // checking if the result is null , if not , that document is used to identify the document we want to delete
            if(target_set.get(0) != null)
            {
                dbReader.delete(target_set);
                return true;
            }
            //return false and print debug line
            System.out.println("Tags List From Tag Repo : " + target_set);
            return  false;

    }
}
