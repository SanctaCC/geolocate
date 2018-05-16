package com.geolocation.mongodb.location;

import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompositeLocationProvider implements LocationProvider{

    private final List<LocationProvider> providers;

    public CompositeLocationProvider(List<LocationProvider> providers) {
        this.providers = providers;

    }

    public Point getOne(String ipAddress){
        for (LocationProvider provider : providers) {
            Point p = provider.getOne(ipAddress);
            if(p!=null){
                return p;
            }
        }
        return  null;
    }
}
