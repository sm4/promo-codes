package com.czequered.promocodes.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Martin Varga
 */
public class AbstractDynamoDBRepository<T> {
    DynamoDBMapper mapper;

    @Autowired
    public AbstractDynamoDBRepository(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public T save(T item) {
        mapper.save(item);
        return item;
    }

    public void delete(T item) {
        mapper.delete(item);
    }
}
