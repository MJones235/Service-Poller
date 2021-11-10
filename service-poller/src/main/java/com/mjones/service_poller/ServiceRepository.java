package com.mjones.service_poller;

import java.util.ArrayList;
import java.util.List;

import com.mjones.service_poller.Utils.TypeConverter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;

public class ServiceRepository extends AbstractVerticle {

    //
    // Service data is stored in a SQLite db
    //
    private final Database database;

    public ServiceRepository(Database database) {
        this.database = database;
    }

    //
    // Data is also held in memory. This is used by the service poller to reduce db reads.
    //
	private static List<Service> services = new ArrayList<>();

    public List<Service> getServices() {
        return services;
    }

    private final TypeConverter typeConverter = new TypeConverter();

    @Override
    public void start() throws Exception {          

        //
        // Add new service
        //
        vertx.eventBus().consumer("service.service-add", msg -> {
            Service service = typeConverter.messageToNewService(msg);
            database.query("INSERT INTO SERVICES (id, name, url, created, lastUpdated, status) VALUES (?, ?, ?, ?, ?, ?);", typeConverter.serviceToJsonArray(service))
                .onSuccess(res -> {
                    services.add(service);
                    msg.reply(Json.encode(service));
                }).onFailure(res -> handleError(msg, res));
        });

        //
        // Retrieve all service data
        //
        vertx.eventBus().consumer("service.services-get", msg -> {
            database.query("SELECT * FROM SERVICES")
                .onSuccess(res -> {
                    services = typeConverter.resultSetToServiceList(res);
                    msg.reply(Json.encode(services));
                }).onFailure(res -> handleError(msg, res));

        });

        //
        // Delete a service
        //
        vertx.eventBus().consumer("services.services-delete", msg -> {
            Service service = Json.decodeValue((String)msg.body(), Service.class);
            database.query("DELETE FROM SERVICES WHERE id = ?;", 
                new JsonArray()
                    .add(service.getId())
                ).onSuccess(res -> {
                    services.removeIf(s -> s.getId() == service.getId());
                }).onFailure(res -> handleError(msg, res));

        });
    }

    private void handleError(Message<Object> msg, Throwable res) {
        System.out.println(res.getMessage());
        msg.reply(Json.encode("{\"error\": \"" + res.getMessage() + "\"}"));
    }
}
