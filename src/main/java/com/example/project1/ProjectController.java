//Name: Michael Felix
//Date: October 31, 2018
//EGR327-A - Software Construction
//Email: Michael.Felix@calbaptist.edu
//Project 1

package com.example.project1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.CharEncoding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
@RestController
public class ProjectController {

    //POST Request
    @RequestMapping(value = "/addVehicle", method = RequestMethod.POST)
    public Vehicle addVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FileWriter output = new FileWriter("./inventory.txt", true);
        mapper.writeValue(output, newVehicle);
        FileUtils.writeStringToFile(new File("./inventory.txt"), System.lineSeparator(), CharEncoding.UTF_8, true);
        return newVehicle;
    }

    //GET Request
    @RequestMapping(value = "/getVehicle/{id}", method = RequestMethod.GET)
    public Vehicle getVehicle(@PathVariable("id") int id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Vehicle foundVehicle = null;
        LineIterator it = FileUtils.lineIterator(new File("./inventory.txt"), "UTF-8");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                Vehicle v = mapper.readValue(line, Vehicle.class);
                if (v.getId() == id) {
                    return v;
                }
            }
        } finally {
            it.close();
        }
        return foundVehicle;
    }

    //PUT Request
    @RequestMapping(value = "/updateVehicle", method = RequestMethod.PUT)
    public Vehicle updateVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> lines = FileUtils.readLines(new File("./inventory.txt"), "UTF-8");
        for (int i = 0; i < lines.size(); i++) {
            Vehicle v = mapper.readValue(lines.get(i), Vehicle.class);
            if (v.getId() == newVehicle.getId()) {
                lines.set(i, mapper.writeValueAsString(newVehicle));
                break;
            }
        }

        FileUtils.writeLines(new File("./inventory.txt"), lines);
        return newVehicle;
    }

    //DELETE Request
    @RequestMapping(value = "/deleteVehicle/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteVehicle(@PathVariable("id") int id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> lines = FileUtils.readLines(new File("./inventory.txt"), "UTF-8");
        for (int i = 0; i < lines.size(); i++) {
            Vehicle v = mapper.readValue(lines.get(i), Vehicle.class);
            if (v.getId() == id) {
                lines.remove(i);
                break;
            }
        }

        FileUtils.writeLines(new File("./inventory.txt"), lines);
        return new ResponseEntity(HttpStatus.OK);
    }

    //GET Request
    @RequestMapping(value = "/getLatestVehicles", method = RequestMethod.GET)
    public List<Vehicle> getLatestVehicles() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> lines = FileUtils.readLines(new File("./inventory.txt"), "UTF-8");
        int totalVehicleCount = 10;
        if (lines.size() < 10) {
            totalVehicleCount = lines.size();
        }

        List<Vehicle> latestVehicles = new ArrayList<>();
        for (int i = lines.size () - 1; i > lines.size()-1-totalVehicleCount; i--){
            Vehicle v = mapper.readValue(lines.get(i), Vehicle.class);
            latestVehicles.add(v);
        }
        return latestVehicles;
    }
}