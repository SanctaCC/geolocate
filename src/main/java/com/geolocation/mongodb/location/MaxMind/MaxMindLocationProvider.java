package com.geolocation.mongodb.location.maxmind;

import com.geolocation.mongodb.location.LocationProvider;
import com.geolocation.mongodb.location.LocationService;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@LocationService
@Slf4j
public class MaxMindLocationProvider implements LocationProvider{

    private final String dbPath;

    private final File dbFile;

    DatabaseReader reader;

    public MaxMindLocationProvider(@Value("${maxmind.path}") String dbPath) throws IOException {
        this.dbPath = dbPath;
        Assert.hasText(dbPath,"Max Mind path not specified");
        dbFile = new File(dbPath);
        reader = new DatabaseReader.Builder(dbFile).build();
    }

    @Override
    public Point getOne(String ip) {
        InetAddress ipAddress = null;
        double x = 0,y = 0;
        try {
            ipAddress = InetAddress.getByName(ip);
            CityResponse r = reader.city(ipAddress);
            Location location = r.getLocation();
            x = (location.getLongitude());
            y = (location.getLatitude());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Point(x,y);
    }
}
