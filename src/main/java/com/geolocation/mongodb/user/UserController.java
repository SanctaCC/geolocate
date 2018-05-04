package com.geolocation.mongodb.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user")
    public ResponseEntity<User> add(@RequestBody User user, HttpServletRequest request){
        user.setId(null);
        user.setIpAddress(request.getRemoteAddr());
        user = userRepository.save(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);

    }

}
