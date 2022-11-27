package com.mosscorp.mooer.userservice.dao.impl;

import com.mosscorp.mooer.userservice.dao.UserDao;
import com.mosscorp.mooer.userservice.exception.UserNotFoundException;
import com.mosscorp.mooer.userservice.model.User;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mosscorp.mooer.userservice.Constants.ACCESS_TOKEN;
import static com.mosscorp.mooer.userservice.Constants.ID_FIELD;
import static com.mosscorp.mooer.userservice.Constants.NAME_FIELD;
import static com.mosscorp.mooer.userservice.Constants.SECRET_TOKEN;
import static com.mosscorp.mooer.userservice.Constants.TABLE_NAME;

@Component
public class DynamoDBUserDao implements UserDao {

    private final DynamoDbClient client;

    public DynamoDBUserDao() {
        this.client = DynamoDbClient
                .builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> AwsBasicCredentials.create(ACCESS_TOKEN, SECRET_TOKEN))
                .build();
    }

    @Override
    public User getUser(String id) {
        final GetItemResponse getItemResponse = client.getItem(
                GetItemRequest
                        .builder()
                        .key(Map.of(ID_FIELD, AttributeValue.builder().s(id).build()))
                        .attributesToGet(ID_FIELD, NAME_FIELD)
                        .tableName(TABLE_NAME)
                        .build()
        );

        if (!getItemResponse.hasItem()) {
            throw new UserNotFoundException(String.format("User with ID %s was not found", id));
        }
        return User.fromDdbMap(getItemResponse.item());
    }

    @Override
    public void createUser(User user) {
        client.putItem(
                PutItemRequest
                        .builder()
                        .item(user.toDdbMap())
                        .tableName(TABLE_NAME)
                        .build()
        );
    }

    @Override
    public List<User> listUsers() {
        final List<Map<String, AttributeValue>> items = client.scan(
                ScanRequest
                        .builder()
                        .tableName(TABLE_NAME)
                        .attributesToGet(ID_FIELD, NAME_FIELD)
                        .build()
        ).items();

        return items
                .stream()
                .map(User::fromDdbMap)
                .collect(Collectors.toList());
    }
}
