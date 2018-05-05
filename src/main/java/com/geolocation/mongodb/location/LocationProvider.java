package com.geolocation.mongodb.location;

import org.springframework.data.geo.Point;

public interface LocationProvider {

    Point getOne(String ip);
}
