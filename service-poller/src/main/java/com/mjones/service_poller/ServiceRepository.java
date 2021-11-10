package com.mjones.service_poller;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;

public class ServiceRepository extends AbstractVerticle {
    private final Database database;
    private final TypeConverter typeConverter = new TypeConverter();

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
            Service service = typeConverter.messageToNewService(msg);
            database.query("INSERT INTO SERVICES (id, name, url, created, lastUpdated, status) VALUES (?, ?, ?, ?, ?, ?);", typeConverter.serviceToJsonArray(service))
            .onSuccess(res -> {
                services.add(service);
            }).onFailure(res -> {
                System.out.println(res.getMessage());
            });
            msg.reply(Json.encode(service));
        });

        vertx.eventBus().consumer("service.services-get", msg -> {
            database.query("SELECT * FROM SERVICES").onSuccess(res -> {
                services = typeConverter.resultSetToServiceList(res);
                msg.reply(Json.encode(services));
            });
        });

        vertx.eventBus().consumer("services.services-delete", msg -> {
            Service service = Json.decodeValue((String)msg.body(), Service.class);
            database.query("DELETE FROM SERVICES WHERE id = ?;", 
                new JsonArray()
                    .add(service.getId())
            ).onSuccess(res -> {
                services.removeIf(s -> s.getId() == service.getId());
            });
            msg.reply(Json.encode(service));
        });
    }
}
