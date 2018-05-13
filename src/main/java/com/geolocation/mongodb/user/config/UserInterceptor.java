package com.geolocation.mongodb.user.config;

import com.geolocation.mongodb.location.LocationProvider;
import com.geolocation.mongodb.user.User;
import com.geolocation.mongodb.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Component
@Slf4j
class UserInterceptor implements HandlerInterceptor{

    private final UserRepository userRepository;

    private final LocationProvider lp;

    private final Environment env;

    public UserInterceptor(UserRepository userRepository, @Lazy LocationProvider lp, Environment env) {
        this.userRepository = userRepository;
        this.lp = lp;
        this.env = env;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.trace("preHandle {} ",handler);
        String ip;
        boolean prod = env.acceptsProfiles("prod");
        ip = prod ? request.getRemoteAddr() : "8.8.8.8";
        User u = userRepository.findByIpAddress(ip).orElseGet(()->new User(ip));
        if(u.getLocation()==null) {
            u.setLocation(lp.getOne(ip));
            u = userRepository.save(u);
            log.info("New user: {}",ip);
        }else {
            CompletableFuture<User> future =
                    userRepository.asyncSave(u);
        }

        RequestContextHolder.currentRequestAttributes()
                .setAttribute("location",u.getLocation(), RequestAttributes.SCOPE_REQUEST);

        return true;
    }

}
