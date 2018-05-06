package com.geolocation.mongodb.user.repository;

import com.geolocation.mongodb.user.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReactiveUserRepository extends ReactiveMongoRepository<User,String>{
}
