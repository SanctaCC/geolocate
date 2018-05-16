package com.geolocation.mongodb.location.ipinfo;

import com.geolocation.mongodb.location.LocationProvider;
import com.geolocation.mongodb.location.LocationService;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@LocationService
public class IpInfoLocationProvider implements LocationProvider {

    public IpInfoLocationProvider(IpInfoApiService apiService) {
        this.apiService = apiService;
    }

    private final IpInfoApiService apiService;

    @Override
    public Point getOne(String ip) {
        IpInfoObject response = apiService.getOne(ip);
        String[] loc = response.getLoc().split(",");
        return new Point(Double.valueOf(loc[1]),Double.valueOf(loc[0]));
    }
}
