package com.geolocation.mongodb.user;
//
//import com.geolocation.mongodb.user.repository.ReactiveUserRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.HttpHandler;
//import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
//import org.springframework.web.reactive.function.server.RequestPredicates;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Flux;
//import reactor.ipc.netty.http.server.HttpServer;
//import reactor.util.function.Tuple2;
//
//import java.time.Duration;
//
//import static org.springframework.web.reactive.function.server.ServerResponse.ok;
//
////@RestController
////@RequestMapping("/rx")
//@Configuration
public class UserReactiveController {}
//
//    private final ReactiveUserRepository reactiveUserRepository;
//
//    public UserReactiveController(ReactiveUserRepository reactiveUserRepository) {
//        this.reactiveUserRepository = reactiveUserRepository;
//    }
//
////    @RequestMapping("/users/{id}")
////    public Mono<User> getOne(@PathVariable String id){
////        return reactiveUserRepository.findById(id);
////    }
////
////    @GetMapping(value = "users", produces = "text/event-stream")
//////    public Flux getAll(){
////        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
////        Flux<User> all = reactiveUserRepository.findAll();
////
////        return Flux.zip(interval,all).map( (Tuple2 p) -> p.getT2());
////    }
//
//    @Bean
//    public RouterFunction<ServerResponse> router(){
//        RouterFunction<ServerResponse> r = RouterFunctions.route(RequestPredicates.GET("/users/{id}"), request ->
//                ok().body(reactiveUserRepository.findById(request.pathVariable("id")), User.class))
//                .andRoute(RequestPredicates.GET("/users/"), request ->
//                {
//                    Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
//                    Flux<User> all = reactiveUserRepository.findAll();
//
//                    Flux<User> ret = Flux.zip(interval, all).map(Tuple2::getT2);
//
//                    return ok().contentType(MediaType.TEXT_EVENT_STREAM).body(ret, User.class);
//                });
//
//        ReactorHttpHandlerAdapter a =
//                new ReactorHttpHandlerAdapter(RouterFunctions.toHttpHandler(r));
//        HttpServer server = HttpServer.create("localhost", 8081).newHandler(a).block();
//        return r;
//    }
//
//}
