package com.geolocation.mongodb.user.repository;

import com.geolocation.mongodb.user.QUser;
import com.geolocation.mongodb.user.User;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String>,AsyncRepository,
        QuerydslPredicateExecutor<User>,QuerydslBinderCustomizer<QUser> {

    Page<User> findAllByOrderByEditedDateDesc(Pageable page);

    GeoResults<User> findTop20ByLocationNear(Point point, Distance distance);

//    GeoResults<User> findAllByIpAddressNotNull(Predicate predicate, Pageable pageable);


    Optional<User> findByIpAddress(String ipAddress);

    GeoPage<User> findAllByLocationNear(Point point, Distance distance, Pageable pageable);

    @Override
    default public void customize(QuerydslBindings bindings, QUser root) {

        bindings.bind(root.location).first((path, value) ->

                path.x.between ( value.getY()-1,value.getY()+1 )
                        .and(path.y.between(value.getX()-1,value.getX()+1)));

        bindings.bind(root.name).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.ipAddress).first(StringExpression::eq);

    }
}