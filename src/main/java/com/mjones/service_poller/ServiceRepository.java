package com.mjones.service_poller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

public class ServiceRepository extends AbstractVerticle {
	private static final List<Service> SERVICES = new ArrayList<>();

    @Override
    public void start() throws Exception {

        vertx.eventBus().consumer("service.service-add", msg -> {
            Service service = Json.decodeValue((String)msg.body(), Service.class);
            service.setId(UUID.randomUUID());
            SERVICES.add(service);
            msg.reply(Json.encode(service));
        });

        vertx.eventBus().consumer("service.services-get", msg -> {
            msg.reply(Json.encode(SERVICES));
        });
    }
}
