package com.geolocation.mongodb.user.repository;

import com.geolocation.mongodb.user.User;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public class AsyncRepositoryImpl implements AsyncRepository{

    private final MongoOperations template;

    public AsyncRepositoryImpl(MongoOperations template) {
        this.template = template;
    }

    @Override
    @Async
    public CompletableFuture<User> asyncSave(User u) {
        template.save(u);
        return CompletableFuture.completedFuture(u);
    }
}
