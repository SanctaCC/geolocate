package com.geolocation.mongodb;

import com.geolocation.mongodb.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableMongoAuditing
@EnableFeignClients
@EnableAsync
@EnableWebMvc
//@EnableReactiveMongoRepositories
//@EnableMongoRepositories
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
	    String os = System.getProperty("os.name").toUpperCase();
	    new SpringApplicationBuilder(Application.class)
                .profiles((os.contains("win".toUpperCase())? "dev":"prod")).build().run(args);
//	    SpringApplication.run(Application.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Bean
    public ThreadPoolTaskExecutor executorPool(){
	    ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
	    tpte.setCorePoolSize(2);
	    tpte.setMaxPoolSize(10);
	    return tpte;
    }

	public void run(String... args) {
	    //db init
////		repository.deleteAll();
//        for (int i = 1; i < 255; i++) {
//		User hubert = new User();
//		hubert.setIpAddress("94.254.181." + i);
//		hubert.setName("hubert");
//		Random r = new Random();

//		hubert.setLocation(new Point(ThreadLocalRandom.current().nextDouble(-180, 180),
//              ThreadLocalRandom.current().nextDouble(-90, 90));
//            		userRepository.save(hubert);
//        }
//

    }
}