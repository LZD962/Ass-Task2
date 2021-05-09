package com.he.console.task2.base;

import com.he.console.task2.DataContainer;

import javax.swing.*;
import java.awt.*;

public class Road extends JPanel {
    private       TrafficLight light;
    private       int          numOfSegments; //length of road
    private final int          roadWidth  = 50;
    final         int          roadYPos;
    final         int          endRoadYPos;
    final         int          roadXPos;
    final         int          endRoadXPos;
    private       Color        lightColor = Color.green;
    private       String       orientation;
    String trafficDirection;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, 1200, 1200);

        for (int z = 0; z < DataContainer.roads.size(); z++) {
            Road r = DataContainer.roads.get(z);
            r.paintRoad(g);
            if (r.getOrientation().equals(Constant.VERTICAL)) {
                for (int c = 0; c < DataContainer.cars.size(); c++) {
                    Vehicles car = DataContainer.cars.get(c);
                    if (car.getRoadCarIsOn().equals(r)) {
                        car.paintMeVertical(g);
                    }
                }
                if (r.getTrafficLight() != null) {
                    r.paintLight(g);
                }
            } else {
                for (int c = 0; c < DataContainer.cars.size(); c++) {
                    Vehicles car = DataContainer.cars.get(c);
                    if (car.getRoadCarIsOn().equals(r)) {
                        car.paintMeHorizontal(g);
                    }
                }
                if (r.getTrafficLight() != null) {
                    r.paintLight(g);
                }
            }
        }
    }

    /**
     *渲染红绿灯
     */
    public void paintLight(Graphics g) {
        g.setColor(lightColor);
        if (getOrientation().equals(Constant.HORIZONTAL)) {
            if (getTrafficDirection().equals(Constant.EAST)) {
                g.fillRect(roadXPos + numOfSegments * 25 - 10, roadYPos - 20, 10, 20);
                g.setColor(Color.black);
                g.drawRect(roadXPos + numOfSegments * 25 - 10, roadYPos - 20, 10, 20);
            } else {
                g.fillRect(roadXPos, roadYPos - 20, 10, 20);
                g.setColor(Color.black);
                g.drawRect(roadXPos, roadYPos - 20, 10, 20);
            }
        } else {
            if (getTrafficDirection().equals(Constant.SOUTH)) {
                g.fillRect(roadYPos - 20, roadXPos + numOfSegments * 25 - 10, 20, 10);
                g.setColor(Color.black);
                g.drawRect(roadYPos - 20, roadXPos + numOfSegments * 25 - 10, 20, 10);
            } else {
                g.fillRect(roadYPos - 20, roadXPos, 20, 10);
                g.setColor(Color.black);
                g.drawRect(roadYPos - 20, roadXPos, 20, 10);
            }
        }
    }


    /**
     * 渲染道路和中线
     */
    public void paintRoad(Graphics g) {
        if (orientation.equals(Constant.HORIZONTAL)) {
            g.setColor(Color.black);
            g.fillRect(roadXPos, roadYPos, numOfSegments * 25, roadWidth);
            g.setColor(Color.WHITE);
            for (int j = 0; j < numOfSegments * 25; j = j + 50) { // line being drawn
                g.fillRect(roadXPos + j+15, roadYPos + roadWidth / 2, 30, 2);
            }
        } else {
            g.setColor(Color.black);
            g.fillRect(roadYPos, roadXPos, roadWidth, numOfSegments * 25);
            g.setColor(Color.WHITE);
            for (int j = 0; j < numOfSegments * 25; j = j + 50) { // line being drawn
                g.fillRect(roadYPos + roadWidth / 2, roadXPos + j+15, 2, 30);
            }
        }
    }

    public Road(int numOfSegments, String orientation, int xPos, int yPos, String direction) {
        super();
        this.numOfSegments = numOfSegments * 2;
        this.orientation = orientation;
        this.roadXPos = xPos;
        this.roadYPos = yPos;
        this.endRoadXPos = xPos + numOfSegments * 50;
        this.endRoadYPos = yPos + numOfSegments * 50;
        this.trafficDirection = direction;
    }

    public  Road(int numOfSegments, String orientation, int xPos, int yPos, String direction, TrafficLight light) {
        super();
        this.numOfSegments = numOfSegments * 2;
        this.orientation = orientation;
        this.light = light;
        this.roadXPos = xPos;
        this.roadYPos = yPos;
        this.endRoadXPos = xPos + numOfSegments * 50;
        this.endRoadYPos = yPos + numOfSegments * 50;
        this.trafficDirection = direction;

    }

    public String getOrientation() {
        return orientation;
    }

    public TrafficLight getTrafficLight() {
        return light;
    }

    public int getRoadLength() {
        return numOfSegments;
    }

    public int getRoadYPos() {
        return roadYPos;
    }

    public int getRoadXPos() {
        return roadXPos;
    }

    public int getEndRoadYPos() {
        return endRoadYPos;
    }

    public int getEndRoadXPos() {
        return endRoadXPos;
    }

    public String getTrafficDirection() {
        return trafficDirection;
    }

    public void setLightColor(Color c) {
        lightColor = c;
    }

}

