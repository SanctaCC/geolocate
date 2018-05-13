package com.geolocation.mongodb.location.IpInfo;

import com.geolocation.mongodb.location.LocationProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@Profile("prod")
@Service
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
