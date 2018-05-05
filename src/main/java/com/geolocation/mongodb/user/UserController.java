package com.geolocation.mongodb.user;

import com.geolocation.mongodb.location.LocationProvider;
import com.geolocation.mongodb.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:4200")
public class UserController {

    private final UserRepository userRepository;

    private final LocationProvider lp;

    @Autowired Environment env;

    public UserController(UserRepository userRepository ,LocationProvider lp) {
        this.userRepository = userRepository;
        this.lp = lp;
    }

//    @PostMapping("/users")
//    public ResponseEntity<User> add(@RequestBody User user, HttpServletRequest request){
//        user.setIpAddress(request.getRemoteAddr());
//        Point point = new Point(30.32121,10.123);
//        user.setLocation(point);
////        user =
//        userRepository.save(user);
//        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}")
//                .buildAndExpand(user.getIpAddress()).toUri();
//        return ResponseEntity.created(uri).body(user);
//    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> get(Pageable pageable){
        return new ResponseEntity<>(userRepository.findAllByOrderByEditedDateDesc(pageable), HttpStatus.OK);
    }

    @GetMapping("/geo/users")
    public ResponseEntity<GeoPage<User>> getPaged(Pageable pageable){
        return ResponseEntity.ok(userRepository.findAllByLocationNear(new Point(30,10.10),
                new Distance(20000,Metrics.KILOMETERS),pageable));
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<User> get(@PathVariable String id){
        return userRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping("/users/nearest")
//    public ResponseEntity<List<User>> getNear(){
//        GeoResults<User> gUser = userRepository.findTop20ByLocationNear(new Point(30,10.10),
//                new Distance(20000,Metrics.KILOMETERS));
//
//        return ResponseEntity.ok(gUser.getContent().stream().map(GeoResult::getContent).collect(Collectors.toList()));
//    }


    @GetMapping("/users/nearest")
    public ResponseEntity<GeoResults<User>> getNearG(){
        GeoResults<User> gUser = userRepository.findTop20ByLocationNear(new Point(30,10.10),
                new Distance(20000,Metrics.KILOMETERS));
        return ResponseEntity.ok(gUser);
    }

    @GetMapping("test")
    public Point test(HttpServletRequest req){
        boolean prod=
        Stream.of(env.getActiveProfiles()).filter(p-> "prod".equals(p)).findAny().isPresent();
        if(prod) return lp.getOne(req.getRemoteAddr());

        return lp.getOne("8.8.8.8"); //dev
    }

}
