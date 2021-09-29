package com.revature.delete_set;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.revature.Exceptions.InvalidRequestException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SetRepo {
    private final DynamoDBMapper dbReader;

    public SetRepo() {
        dbReader = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
    }

    public boolean deleteSetById(String target_set_id ) {
        //creating new set_document for query
        Set s = new Set();
        s.setId(target_set_id);

       // setting query statement to target sets with targeted id
        Map<String, AttributeValue> queryInputs = new HashMap<>();
        queryInputs.put(":id", new AttributeValue().withS(target_set_id));
        DynamoDBScanExpression target_user_favorited_set_batch = new DynamoDBScanExpression()
                .withFilterExpression("contains(Users.favorite_sets , :id)")
                .withExpressionAttributeValues(queryInputs);
        System.out.println("From SETRepo :" + target_user_favorited_set_batch);
        //storing resultant scan in a list
        List<Set> queryResult = dbReader.scan(Set.class, target_user_favorited_set_batch);
        System.out.println("From SETRepo" + queryResult);
        // batch loading all target users to delete target set from their favorites list
        Map<String, List<Object>> loaded_docs = dbReader.batchLoad(queryResult);
        // batch deleting all references
        dbReader.batchDelete(loaded_docs);
        /*
            creating query using the set_document as a hash key value
            limit of one for now just because i know ids are unique
        */
        DynamoDBQueryExpression<Set> query =
                new DynamoDBQueryExpression<Set>()
                        .withHashKeyValues(s)
                        .withLimit(1);
        // storing resultant set in a list
        List<Set> target_set = dbReader.query(Set.class , query);
        // checking if the result is null , if not , that document is used to identify the document we want to delete
            if(target_set.get(0) == null)
            {
                //return false and print debug line
                System.out.println("Tags List From Tag Repo : " + target_set);
                throw new InvalidRequestException("No Such Document");
            }
            dbReader.delete(target_set);
            return true;



    }
}
