package com.geolocation.mongodb.location;

import java.lang.annotation.*;

@Target(value={ElementType.TYPE})
@Retention(value= RetentionPolicy.RUNTIME)
@Inherited
public @interface LocationService {
}
