//Name: Michael Felix
//Date: October 31, 2018
//EGR327-A - Software Construction
//Email: Michael.Felix@calbaptist.edu
//Project 1

package com.example.project1;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class MyTask {

    RestTemplate restTemplate = new RestTemplate();
    private int vehicleID;

    //Add Vehicle
    @Scheduled(cron = "*/5 * * * * *")
    public void addNewVehicle() {
        String makeModel = RandomStringUtils.randomAlphabetic(10);
        int year = RandomUtils.nextInt(1986,2016);
        double retailPrice = RandomUtils.nextInt(15000,45000);
        Vehicle vehicle = new Vehicle(vehicleID++, makeModel, year, retailPrice);
        String url = "http://localhost:8080/addVehicle";
        restTemplate.postForObject(url, vehicle, Vehicle.class);
    }

    //Update Vehicle
    @Scheduled(cron = "*/1 * * * * *")
    public void updateRandomVehicle() {
        int updateID = RandomUtils.nextInt(0,100);
        Vehicle newVehicle = new Vehicle (updateID, "111111", 1111, 111111);
        String getUrl = "http://localhost:8080/getVehicle/" + updateID;
        String updateUrl = "http://localhost:8080/updateVehicle";
        Vehicle v = restTemplate.getForObject(getUrl, Vehicle.class);
        if (v != null) {
            restTemplate.put(updateUrl, newVehicle, Vehicle.class);
            System.out.println("Updated: " + v);
        }
    }

    //Delete Vehicle
    @Scheduled(cron = "*/1 * * * * *")
    public void deleteRandomVehicle() {
        int deleteID = RandomUtils.nextInt(0,100);
        String getUrl = "http://localhost:8080/getVehicle/" + deleteID;
        String deleteUrl = "http://localhost:8080/deleteVehicle/" + deleteID;
        Vehicle v = restTemplate.getForObject (getUrl, Vehicle.class);
        if (v != null) {
            restTemplate.delete(deleteUrl);
            System.out.println("Deleted: " + v);
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void latestVehicleReport() {
        String getUrl = "http://localhost:8080/getLatestVehicles";
        List<Vehicle> latestVehicle = restTemplate.getForObject(getUrl, List.class);
        System.out.println("----Latest Vehicles----");
        for (int i = 0; i < latestVehicle.size();i++) {
            System.out.println(latestVehicle.get(i));
        }
        System.out.println("----------------------");
    }
}