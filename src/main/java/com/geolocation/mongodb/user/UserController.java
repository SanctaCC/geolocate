package com.geolocation.mongodb.user;

import com.geolocation.mongodb.location.LocationProvider;
import com.geolocation.mongodb.user.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.*;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.NoHandlerFoundException;

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

    private Point getCurrentPoint() {
        Point point= (Point) RequestContextHolder.getRequestAttributes()
                .getAttribute("location", RequestAttributes.SCOPE_REQUEST);
        return point;
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

    @RequestMapping("/**")
    public void noHandler(HttpServletRequest req) throws NoHandlerFoundException {
        throw new NoHandlerFoundException(req.getMethod(),req.getRequestURL().toString(),
                new ServletServerHttpRequest(req).getHeaders());
    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> get(@QuerydslPredicate(root = User.class) Predicate predicate,
                                          @PageableDefault(sort={"editedDate"},direction = Sort.Direction.DESC) Pageable pageable){
        predicate = (predicate==null)? new BooleanBuilder():predicate;
        return new ResponseEntity<>(userRepository.findAll(predicate,pageable), HttpStatus.OK);
    }

    @GetMapping("/geo/users")
    public ResponseEntity<GeoPage<User>> getPaged( Pageable pageable){
        return ResponseEntity.ok(userRepository.findAllByLocationNear(getCurrentPoint(),
                new Distance(20000,Metrics.KILOMETERS),pageable));
    }


    @GetMapping("/users/{id:.+}")
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
        GeoResults<User> gUser = userRepository.findTop20ByLocationNear(getCurrentPoint(),
                new Distance(20000,Metrics.KILOMETERS));
        return ResponseEntity.ok(gUser);
    }

    @GetMapping("test")
    public Point test(HttpServletRequest req/*,@RequestAttribute("location") Point point*/ ){
        boolean prod=
        Stream.of(env.getActiveProfiles()).anyMatch(p-> "prod".equals(p));
        if(prod) return lp.getOne(req.getRemoteAddr());

        return lp.getOne("8.8.8.8"); //dev
    }

}
