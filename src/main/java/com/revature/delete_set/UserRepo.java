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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class UserRepo {
    private final DynamoDbTable<User> userTable;

    public UserRepo(){
        DynamoDbClient db = DynamoDbClient.builder().httpClient(ApacheHttpClient.create()).build();
        DynamoDbEnhancedClient dbClient = DynamoDbEnhancedClient.builder().dynamoDbClient(db).build();
        userTable = dbClient.table("Users", TableSchema.fromBean(User.class));
    }

    /**
     * Gets all users from the Users table
     * @Authors Jack Raney and Alfonso Holmes
     */
    public List<User> getAllUsers(){ return userTable.scan().items().stream().collect(Collectors.toList()); }

    /**
     * Gets a given user from the Users table by username
     * @Authors Jack Raney and Alfonso Holmes
     * @param name
     */
    public User getUser(String name) {
        AttributeValue val = AttributeValue.builder().s(name).build();
        Expression filter = Expression.builder().expression("#a = :b").putExpressionName("#a", "username") .putExpressionValue(":b", val).build();
        ScanEnhancedRequest request = ScanEnhancedRequest.builder().filterExpression(filter).build();
        User user = userTable.scan(request).stream().findFirst().orElseThrow(ResourceNotFoundException::new).items().get(0);
        System.out.println("USER WITH ID: " + user);
        return user;
    }

    /**
     * Handles referential integrity by removing the given set from all users created_sets and favorited_sets lists
     * @Authors Jack Raney and Alfonso Holmes
     * @param id
     */
    //Jack hates this...
    public void purgeSet(String id) {
        //get rid of set from author's created_sets and all users favorite_sets
        List<User> users = getAllUsers();
        for(User u : users) {
            //Using toKeep to avoid ConcurrentModificationException
            List<User.UserSetDoc> toKeep = new ArrayList<>();
            for (User.UserSetDoc set : u.getCreated_sets()) {
                if (!set.getId().equals(id)) {
                    toKeep.add(set);
                }
            }
            u.setCreated_sets(toKeep);

            //Reset toKeep
            toKeep = new ArrayList<>();
            for (User.UserSetDoc set : u.getFavorite_sets()) {
                if (!set.getId().equals(id)) {
                    toKeep.add(set);
                }
            }
            u.setFavorite_sets(toKeep);
            userTable.putItem(u);
        }
    }
}