package com.geolocation.mongodb.location;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
@Slf4j
public class ClassPathScanningCandidateLocationProvidersProvider {

    @Autowired
    private ApplicationContext context;

    @Bean
    public List<LocationProvider> providers() throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(LocationService.class));
        Set<BeanDefinition> lpDefinitions = provider.findCandidateComponents(ClassUtils.getPackageName(getClass()));
        AutowireCapableBeanFactory acbf = context.getAutowireCapableBeanFactory();
        List<LocationProvider> lp = new ArrayList<>(lpDefinitions.size());
        for (BeanDefinition bean: lpDefinitions) {
            try {
                Class clazz = Class.forName(bean.getBeanClassName());
                lp.add(acbf.<LocationProvider>createBean(clazz));
            }catch (BeanCreationException e){
                log.warn("Error creating bean {}.",e.getBeanName());
            }
        }
        if(lp.isEmpty()){
            throw new BeanCreationException("No Location Providers available.");
        }
        return lp;
    }
}
