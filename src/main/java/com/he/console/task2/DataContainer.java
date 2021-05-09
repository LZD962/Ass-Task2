package com.he.console.task2;

import com.he.console.task2.base.Vehicles;
import com.he.console.task2.base.Road;
import com.he.console.task2.base.TrafficLight;

import java.util.ArrayList;

public class DataContainer {
    public static ArrayList<Road>         roads         = new ArrayList<>();
    public static ArrayList<Vehicles>     cars          = new ArrayList<>();
    public static ArrayList<TrafficLight> trafficLights = new ArrayList<>();
    public DataContainer(){
    }

    public void addRoad(Road road){
        roads.add(road);
    }
    public void addCar(Vehicles car){
        cars.add(car);
    }
    public void addTrafficLight(TrafficLight light) {
        trafficLights.add(light);
    }

}
