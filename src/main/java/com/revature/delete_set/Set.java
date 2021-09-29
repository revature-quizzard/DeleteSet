package com.revature.delete_set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

import java.util.List;


@Data
@DynamoDBTable(tableName = "Sets")
public class Set {

    public Set(){}
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String set_name;

    @DynamoDBAttribute
    private List<Tag> tags;

    @DynamoDBAttribute
    private List<Card> cards;

    @DynamoDBAttribute
    private String author;

    @DynamoDBAttribute
    private boolean is_public;

    @DynamoDBAttribute
    private int views;

    @DynamoDBAttribute
    private int plays;

    @DynamoDBAttribute
    private int studies;

    @DynamoDBAttribute
    private int favorites;


}
