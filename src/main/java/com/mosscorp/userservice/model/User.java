package com.mosscorp.userservice.model;

import com.mosscorp.userservice.Constants;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public record User(String id, String name) {
    public static User fromDdbMap(final Map<String, AttributeValue> ddbMap) {
        return new User(ddbMap.get(Constants.ID_FIELD).s(), ddbMap.get(Constants.NAME_FIELD).s());
    }

    public Map<String, AttributeValue> toDdbMap() {
        return Map.of(
                Constants.ID_FIELD, AttributeValue.builder().s(id).build(),
                Constants.NAME_FIELD, AttributeValue.builder().s(name).build()
        );
    }
}
