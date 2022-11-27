package com.mosscorp.mooer.userservice.model;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

import static com.mosscorp.mooer.userservice.Constants.ID_FIELD;
import static com.mosscorp.mooer.userservice.Constants.NAME_FIELD;

public record User(String id, String name) {
    public static User fromDdbMap(final Map<String, AttributeValue> ddbMap) {
        return new User(ddbMap.get(ID_FIELD).s(), ddbMap.get(NAME_FIELD).s());
    }

    public Map<String, AttributeValue> toDdbMap() {
        return Map.of(
                ID_FIELD, AttributeValue.builder().s(id).build(),
                NAME_FIELD, AttributeValue.builder().s(name).build()
        );
    }
}
