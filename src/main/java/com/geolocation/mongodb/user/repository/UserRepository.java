package com.geolocation.mongodb.user.repository;

import com.geolocation.mongodb.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String>,AsyncRepository {

    Page<User> findAllByOrderByEditedDateDesc(Pageable page);

    GeoResults<User> findTop20ByLocationNear(Point point,Distance distance);

    Optional<User> findByIpAddress(String ipAddress);

    GeoPage<User> findAllByLocationNear(Point point, Distance distance,Pageable pageable);

}