package com.revature.delete_set;

import com.revature.exceptions.ResourceNotFoundException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.stream.Collectors;


public class UserRepo {

        private final DynamoDbTable<User> userTable;

        public UserRepo(){
            DynamoDbClient db = DynamoDbClient.builder().httpClient(ApacheHttpClient.create()).build();
            DynamoDbEnhancedClient dbClient = DynamoDbEnhancedClient.builder().dynamoDbClient(db).build();
            userTable = dbClient.table("Users", TableSchema.fromBean(User.class));
        }

        public List<User> getAllUsers(){ return userTable.scan().items().stream().collect(Collectors.toList()); }

        public User getUser(String name) {
            AttributeValue val = AttributeValue.builder().s(name).build();
            Expression filter = Expression.builder().expression("#a = :b").putExpressionName("#a", "username") .putExpressionValue(":b", val).build();
            ScanEnhancedRequest request = ScanEnhancedRequest.builder().filterExpression(filter).build();

            User user = userTable.scan(request).stream().findFirst().orElseThrow(ResourceNotFoundException::new).items().get(0);
            System.out.println("USER WITH ID: " + user);
            return user;
        }

        public User addSet(Set newSet, User user){
            return user;
        }
}
