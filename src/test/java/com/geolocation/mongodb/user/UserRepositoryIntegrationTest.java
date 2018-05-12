package com.geolocation.mongodb.user;

import com.geolocation.mongodb.user.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.geo.Point;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private User a,b,c,d;

    @Autowired
    private ApplicationContext context;

    @Before
    public void setUp() {

        userRepository.deleteAll();
        a = new User("test A", "123.123.123.123",new Point(-120,20),null);
        b = new User("test B", "32.123.13.123",new Point(-10,30),null);
        c = new User("test C", "12.123.123.123",new Point(-20,80),null);
        d = new User("test D", "42.123.123.12",new Point(-10,40),null);
        userRepository.saveAll(Arrays.asList(a,b,c,d));

    }
    @Test
    public void checkIfSaves(){
        String ipAddress = "34.12.54.12";
        User u = userRepository.save( new User(ipAddress));
        Assert.isTrue(userRepository.findById(ipAddress).isPresent(),"Repository doesn't save users");
        Assert.notNull(u.getEditedDate(),"Auditing doesn't set editedDate");

    }

    @Test
    public void searchUsingQuerydsl(){
        QUser qUser = QUser.user;
        QuerydslPredicateBuilder b  = new QuerydslPredicateBuilder(new DefaultConversionService(),
                SimpleEntityPathResolver.INSTANCE);

        ClassTypeInformation<User> typeInformation = ClassTypeInformation.from(User.class);
        MultiValueMap<String, String> values = new LinkedMultiValueMap<>();
        values.add("name","test"); //expect field 'name' to contain 'test'
        QuerydslBindingsFactory f = new QuerydslBindingsFactory(SimpleEntityPathResolver.INSTANCE);
        f.setApplicationContext(context); //find QuerydslBinderCustomizer using ListableBeanFactory

        Predicate p = b.getPredicate(typeInformation,values,f.createBindingsFor(typeInformation));

        Iterable<User> all = userRepository.findAll(p, new QSort(qUser.name.asc()));
        assertThat(all).hasSize(4).extracting("name").containsSequence("test A","test B","test C","test D");

    }
}
