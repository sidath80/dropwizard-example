package com.personal.dw.example;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.personal.dw.example.config.DatabaseConfiguration;
import com.personal.dw.example.config.ExampleServiceConfiguration;
import com.personal.dw.example.config.MessagesConfiguration;
import com.personal.dw.example.dao.UserDao;
import com.personal.dw.example.dao.UserDaoImpl;
import com.personal.dw.example.resource.HelloResource;
import com.personal.dw.example.resource.UserResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import java.util.Properties;

public class ExampleService extends Service<ExampleServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new ExampleService().run(args);
    }

    @Override
    public void initialize(final Bootstrap<ExampleServiceConfiguration> bootstrap) {
        bootstrap.setName("dropwizard-example-guice");
    }

    // you probably would like to move this method to separate class
    private Injector createInjector(final ExampleServiceConfiguration conf) {
        return Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                // if someone would like to @Inject ExampleServiceConfiguration
                bind(ExampleServiceConfiguration.class).toInstance(conf);
                // for ExampleResource, which does @Inject MessagesConfiguration
                bind(MessagesConfiguration.class).toInstance(conf.getMessages());
                // for UserDao
                bind(UserDao.class).to(UserDaoImpl.class);
            }
        }, createJpaPersistModule(conf.getDatabase()));
    }

    // you probably would like to move this method to separate class
    private JpaPersistModule createJpaPersistModule(DatabaseConfiguration conf) {
        Properties props = new Properties();
        props.put("javax.persistence.jdbc.url", conf.getUrl());
        props.put("javax.persistence.jdbc.user", conf.getUser());
        props.put("javax.persistence.jdbc.password", conf.getPassword());
        props.put("javax.persistence.jdbc.driver", conf.getDriverClass());
        JpaPersistModule jpaModule = new JpaPersistModule("Default");
        jpaModule.properties(props);
        return jpaModule;
    }

    @Override
    public void run(final ExampleServiceConfiguration conf, final Environment env) {
        Injector injector = createInjector(conf);
        env.addFilter(injector.getInstance(PersistFilter.class), "/*");
        env.addResource(injector.getInstance(HelloResource.class));
        env.addResource(injector.getInstance(UserResource.class));
    }

}
