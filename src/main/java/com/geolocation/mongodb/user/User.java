package com.geolocation.mongodb.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@ToString
@EqualsAndHashCode
public class User {

    public User(String ip){
        this.ipAddress = ip;
    }

    private String name;
    @Id
    @JsonProperty(value="id")
    private String ipAddress;
    @GeoSpatialIndexed(type= GeoSpatialIndexType.GEO_2DSPHERE)
    private Point location;

    @LastModifiedDate
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date editedDate;

}
