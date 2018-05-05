package com.geolocation.mongodb.location.MaxMind;

import com.geolocation.mongodb.location.LocationProvider;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@Service
@Profile("dev")
@Slf4j
public class MaxMindLocationProvider implements LocationProvider{

    private final String dbPath;

    private final File dbFile;

    DatabaseReader reader;

    public MaxMindLocationProvider(@Value("${maxmind.path}") String dbPath){
        this.dbPath = dbPath;
        Assert.hasText(dbPath,"Max Mind path not specified");
        dbFile = new File(dbPath);
        try {
            reader = new DatabaseReader.Builder(dbFile).build();
        } catch (IOException e) {
            log.error("exception caught in "+getClass(),e);
        }
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
            log.error("exception caught in "+getClass(),e);
            throw new RuntimeException();
        }
        return new Point(x,y);
    }
}
