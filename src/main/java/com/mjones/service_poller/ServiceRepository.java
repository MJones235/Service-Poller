package com.mjones.service_poller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;

public class ServiceRepository extends AbstractVerticle {
    private final Database database;

    public ServiceRepository(Database database) {
        this.database = database;
    }

	private static List<Service> SERVICES = new ArrayList<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    public void start() throws Exception {          

        vertx.eventBus().consumer("service.service-add", msg -> {
            Date date = new Date();
            Service service = Json.decodeValue((String)msg.body(), Service.class);
            service.setId(UUID.randomUUID().toString());
            service.setCreated(dateFormat.format(date));
            service.setLastUpdated(dateFormat.format(date));
            service.setStatus(Status.PENDING);

            database.query("INSERT INTO SERVICES (id, name, url, created, lastUpdated) VALUES (?, ?, ?, ?, ?);", serviceToJsonArray(service)).onSuccess(res -> {
                System.out.println("Entry made");
            }).onFailure(res -> {
                System.out.println(res);
            });
            msg.reply(Json.encode(service));
        });

        vertx.eventBus().consumer("service.services-get", msg -> {
            database.query("SELECT * FROM SERVICES").onSuccess(res -> {
                SERVICES = new ArrayList<>();
                res.getRows().forEach(row -> {
                    SERVICES.add(Json.decodeValue(row.toString(), Service.class));
                });
                msg.reply(Json.encode(SERVICES));
            });
        });
    }

    public JsonArray serviceToJsonArray(Service service) {
        return new JsonArray()
            .add(service.getId())
            .add(service.getName())
            .add(service.getUrl())
            .add(service.getCreated())
            .add(service.getLastUpdated());
    }
}
