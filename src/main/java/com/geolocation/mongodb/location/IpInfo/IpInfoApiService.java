package com.geolocation.mongodb.location.IpInfo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@FeignClient(url="https://ipinfo.io/",value = "service")
interface IpInfoApiService {

    @RequestMapping(value="/{ip}",produces = {"application/json"})
    IpInfoObject getOne(@PathVariable("ip") String ip);
}
