package com.revature.delete_set;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.revature.Exceptions.InvalidRequestException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



    public class UserRepo {

        private final DynamoDBMapper dbReader;

        public UserRepo(){
            dbReader = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
        }

        public List<User> getAllUsers(){ return dbReader.scan(User.class, new DynamoDBScanExpression()); }

        public User getUser(String name) {
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":val1", new AttributeValue().withS(name));

            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                    .withFilterExpression("username = :val1").withExpressionAttributeValues(eav);

            List<User> author = dbReader.scan(User.class, scanExpression);
            if(author == null) {
                throw new InvalidRequestException("null Author");
            }
            return author.get(0);
        }

        public User addSet(Set newSet, User user){
            return user;
        }
}
