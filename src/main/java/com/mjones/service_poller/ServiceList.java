package com.mjones.service_poller;

import java.util.ArrayList;
import java.util.List;

public class ServiceList {
    private List<Service> services = new ArrayList<>();  

    public ServiceList() {}
    
    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void addService(Service newService) {
        this.services.add(newService);
    }

    public List<Service> getServices() {
        return this.services;
    }
}
