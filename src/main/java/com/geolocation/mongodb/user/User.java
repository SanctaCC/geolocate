package com.geolocation.mongodb.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@EqualsAndHashCode
public class User {
    @Id
    private String id;

    private String name;

    private String ipAddress;

    private Point location;

}
