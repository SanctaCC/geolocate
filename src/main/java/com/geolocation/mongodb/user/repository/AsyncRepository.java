package com.geolocation.mongodb.user.repository;

import com.geolocation.mongodb.user.User;

import java.util.concurrent.CompletableFuture;

public interface AsyncRepository {
    CompletableFuture<User> asyncSave(User u);
}
