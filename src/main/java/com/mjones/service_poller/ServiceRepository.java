package com.mjones.service_poller;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.PoolOptions;

public class ServiceRepository extends AbstractVerticle {
	private static final List<Service> SERVICES = new ArrayList<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    static final String DB_URL = "jdbc:mysql://localhost:3306/services";
    static final String USER = "username";
    static final String PASS = "password";

    @Override
    public void start() throws Exception {
        JDBCPool pool = JDBCPool.pool(vertx, 
            new JDBCConnectOptions()
                .setJdbcUrl(DB_URL)
                .setUser(USER)
                .setPassword(PASS),
            new PoolOptions()
                .setMaxSize(16)
        );


        vertx.eventBus().consumer("service.service-add", msg -> {
            Date date = new Date();
            Service service = Json.decodeValue((String)msg.body(), Service.class);
            service.setId(UUID.randomUUID());
            service.setCreated(dateFormat.format(date));
            service.setLastUpdated(dateFormat.format(date));
            SERVICES.add(service);
            msg.reply(Json.encode(service));
        });

        vertx.eventBus().consumer("service.services-get", msg -> {
            msg.reply(Json.encode(SERVICES));
        });
    }
}
