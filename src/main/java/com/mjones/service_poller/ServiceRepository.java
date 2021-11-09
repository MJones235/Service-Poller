package com.mjones.service_poller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;

public class ServiceRepository extends AbstractVerticle {
    private final Database database;

    public ServiceRepository(Database database) {
        this.database = database;
    }

	private static List<Service> services = new ArrayList<>();

    public List<Service> getServices() {
        return services;
    }

    @Override
    public void start() throws Exception {          

        vertx.eventBus().consumer("service.service-add", msg -> {
            Service service = getService(msg);
            database.query("INSERT INTO SERVICES (id, name, url, created, lastUpdated, status) VALUES (?, ?, ?, ?, ?, ?);", serviceToJsonArray(service)).onSuccess(res -> {
                System.out.println("Entry made");
            }).onFailure(res -> {
                System.out.println(res);
            });
            msg.reply(Json.encode(service));
        });

        vertx.eventBus().consumer("service.services-get", msg -> {
            database.query("SELECT * FROM SERVICES").onSuccess(res -> {
                services = updateServices(res);
                msg.reply(Json.encode(services));

            });
        });
    }

    private List<Service> updateServices(ResultSet res) {
        List<Service> services = new ArrayList<>();
        res.getRows().forEach(row -> {
            services.add(Json.decodeValue(row.toString(), Service.class));
        });
        return services;
    }

    private Service getService(Message<Object> msg) {
        Service service = Json.decodeValue((String)msg.body(), Service.class);
        service.setId(UUID.randomUUID().toString());
        service.setCreated(new CurrentTimestamp().getDate());
        service.setLastUpdated(new CurrentTimestamp().getDate());
        service.setStatus(Status.PENDING);
        return service;
    }

    private JsonArray serviceToJsonArray(Service service) {
        return new JsonArray()
            .add(service.getId())
            .add(service.getName())
            .add(service.getUrl())
            .add(service.getCreated())
            .add(service.getLastUpdated())
            .add(service.getStatus().toString());
    }
}
