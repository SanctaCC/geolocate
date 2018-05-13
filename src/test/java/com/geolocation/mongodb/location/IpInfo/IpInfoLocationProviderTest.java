package com.geolocation.mongodb.location.IpInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.geo.Point;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class IpInfoLocationProviderTest {

    @InjectMocks
    private IpInfoLocationProvider ipInfoLocationProvider;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IpInfoApiService ipInfoApiService;

    @Mock
    private IpInfoObject obj;

    String ipAddress = "123.35.21.36";

    double a = -45;
    double b = 87;
    @Before
    public void init(){

        Mockito.when(ipInfoApiService.getOne(ipAddress)).thenReturn(obj);
        Mockito.when(obj.getLoc()).thenReturn(a+","+b);

    }

    @Test
    public void getLocation(){
        Point point = ipInfoLocationProvider.getOne(ipAddress);
        assertThat(point).isNotNull();
        assertThat(point.getX()).isEqualTo(b);
        assertThat(point.getY()).isEqualTo(a);
    }

}
