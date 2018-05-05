package com.geolocation.mongodb.user.config;

import com.geolocation.mongodb.location.LocationProvider;
import com.geolocation.mongodb.user.User;
import com.geolocation.mongodb.user.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

@Component
class UserInterceptor implements HandlerInterceptor{

    private final UserRepository userRepository;

    private final LocationProvider lp;

    public UserInterceptor(UserRepository userRepository, @Lazy LocationProvider lp) {
        this.userRepository = userRepository;
        this.lp = lp;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        User u = userRepository.findByIpAddress(ip).orElseGet(()->new User(ip));
        if(u.getLocation()==null) {
            u.setLocation(lp.getOne(ip));
            userRepository.save(u);
        }else {
            CompletableFuture<User> future =
                    userRepository.asyncSave(u);
        }
        return true;
    }

}
