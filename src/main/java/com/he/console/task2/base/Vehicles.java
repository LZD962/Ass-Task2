package com.he.console.task2.base;

import com.he.console.task2.DataContainer;

import java.awt.*;
import java.util.ArrayList;

public class Vehicles {
    private   Road road; // road that the car is on
    protected int  yPos; // current position on map
    protected int  xPos; // current position on map
    protected int  height;
    protected int  width;

    /**
     * 航向添加机动车
     */
    public void paintMeHorizontal(Graphics g) {
    }

    /**
     * 纵向添加机动车
     */
    public void paintMeVertical(Graphics g) {
    }

    public Vehicles(Road road) {
        this.road = road;
        yPos = getRoadCarIsOn().getRoadYPos();
        xPos = getRoadCarIsOn().getRoadXPos();
    }

    public Road getRoadCarIsOn() {
        return road;
    }

    public int getCarXPosition() {
        return xPos;
    }

    public void setCarXPosition(int x) {
        xPos = x;
    }

    public int getCarYPosition() {
        return yPos;
    }

    public void setCarYPosition(int y) {
        yPos = y;
    }

    public int getCarWidth() {
        return width;
    }

    private void setCurrentRoad(Road road) {
        this.road = road;
    }

    /**
     * 是否在道路尽头
     */
    private boolean checkIfAtEndOfRoad() {
        if (getRoadCarIsOn().getTrafficDirection().equals(Constant.EAST) || getRoadCarIsOn().getTrafficDirection().equals(Constant.SOUTH)) {
            return (xPos + width >= getRoadCarIsOn().getEndRoadXPos());
        } else if (getRoadCarIsOn().getTrafficDirection().equals(Constant.WEST) || getRoadCarIsOn().getTrafficDirection().equals(Constant.NORTH)) {
            return (xPos <= road.getRoadXPos());
        } else {
            return true;
        }
    }

    /**
     * 机动车冲突
     */
    public boolean collision(int x, Vehicles car) {
        String direction = getRoadCarIsOn().getTrafficDirection();
        for (int i = 0; i < DataContainer.cars.size(); i++) {
            Vehicles c = DataContainer.cars.get(i);
            if (c.getRoadCarIsOn() == getRoadCarIsOn() && car.getCarYPosition() == c.getCarYPosition()) {
                int otherCarXPosition = c.getCarXPosition();
                int otherCarWidth = c.getCarWidth();
                if (!car.equals(c)) { // if not checking current car
                    if (x < otherCarXPosition + otherCarWidth && //left side is left  of cars right side
                            x + otherCarWidth > otherCarXPosition && (direction.equals(Constant.EAST) || direction.equals(Constant.SOUTH))) { // right side right of his left side
                        return true;
                    } else if (x < otherCarXPosition + otherCarWidth * 2 - 15 && x + car.getCarWidth() > otherCarXPosition &&
                            (direction.equals(Constant.WEST) || direction.equals(Constant.NORTH))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *判断是否能前进
     */
    private boolean canMoveForward() {
        String direction = getRoadCarIsOn().getTrafficDirection();
        if (xPos + width >= getRoadCarIsOn().getRoadLength() * 25 - 25 + getRoadCarIsOn().getRoadXPos() && (direction.equals(Constant.EAST) || direction.equals(Constant.SOUTH))
                || xPos <= getRoadCarIsOn().getRoadXPos() + 25 && (direction.equals(Constant.WEST) || direction.equals(Constant.NORTH))) {
            if (getRoadCarIsOn().getTrafficLight() == null) {
                return true;
            } else {
                TrafficLight light = getRoadCarIsOn().getTrafficLight();
                return light.getCurrentColor().equals("green");
            }
        }
        return true;
    }

    private int getIndexOfCurrentRoad() {
        return DataContainer.roads.indexOf(road);
    }

    /**
     * 进入下一路段
     */
    private Road nextRoad() {
        int otherRoadXPos;
        int otherRoadYPos;
        int otherRoadEndXPos;
        int otherRoadEndYPos;
        int currentRoadXPos;
        int currentRoadYPos;
        int currentRoadEndXPos;
        int currentRoadEndYPos;
        Road currentRoad = DataContainer.roads.get(getIndexOfCurrentRoad());
        Road nextRoad = DataContainer.roads.get(0);
        ArrayList<Integer> xPositions = new ArrayList<Integer>();
        ArrayList<Integer> yPositions = new ArrayList<Integer>();
        if (currentRoad.getOrientation().equals(Constant.VERTICAL)) {
            currentRoadXPos = currentRoad.getRoadYPos();
            currentRoadYPos = currentRoad.getRoadXPos();
            currentRoadEndXPos = currentRoad.getEndRoadYPos();
            currentRoadEndYPos = currentRoad.getEndRoadXPos();
        } else {
            currentRoadXPos = currentRoad.getRoadXPos();
            currentRoadYPos = currentRoad.getRoadYPos();
            currentRoadEndXPos = currentRoad.getEndRoadXPos();
            currentRoadEndYPos = currentRoad.getEndRoadYPos();
        }
        for (int i = 0; i < DataContainer.roads.size(); i++) {
            Road r = DataContainer.roads.get(i);
            if (r != currentRoad) {

                if (r.getOrientation().equals(Constant.HORIZONTAL)) {
                    otherRoadXPos = r.getRoadXPos();
                    otherRoadYPos = r.getRoadYPos();
                    otherRoadEndXPos = r.getEndRoadXPos();
                    otherRoadEndYPos = r.getEndRoadYPos();
                } else {
                    otherRoadXPos = r.getRoadYPos();
                    otherRoadYPos = r.getRoadXPos();
                    otherRoadEndXPos = r.getEndRoadYPos();
                    otherRoadEndYPos = r.getEndRoadXPos();
                }
                if (currentRoad.getTrafficDirection().equals(Constant.EAST) && otherRoadXPos > currentRoadEndXPos) {
                    xPositions.add(otherRoadXPos);
                } else if (currentRoad.getTrafficDirection().equals(Constant.WEST) && otherRoadEndXPos < currentRoadXPos) {
                    xPositions.add(otherRoadEndXPos);
                } else if (currentRoad.getTrafficDirection().equals(Constant.NORTH) && otherRoadEndYPos < currentRoadYPos) {
                    yPositions.add(otherRoadEndYPos);
                } else if (currentRoad.getTrafficDirection().equals(Constant.SOUTH) && otherRoadYPos > currentRoadEndYPos) {
                    yPositions.add(otherRoadYPos);
                }
            }
        }
        int num;
        int num2;
        num = getCarXPosition(); //trying to find road with x position closest to this x position
        num2 = getCarYPosition(); // trying to find road with y position closest to this y position
        int index = 0;
        int index2 = 0;
        int difference_1 = 10000;
        int difference_2 = 10000;
        if (currentRoad.getTrafficDirection().equals(Constant.EAST) || currentRoad.getTrafficDirection().equals(Constant.WEST)) {
            for (int j = 0; j < xPositions.size(); j++) { // loops through every position
                int Difference_x = Math.abs(xPositions.get(j) - num);
                if (Difference_x < difference_1) { // checks if difference is getting smaller
                    index = j;
                    difference_1 = Difference_x;
                }
            }
        } else if (currentRoad.getTrafficDirection().equals(Constant.SOUTH) || currentRoad.getTrafficDirection().equals(Constant.NORTH)) {
            for (int j = 0; j < xPositions.size(); j++) { // loops through every position
                int Difference_y = Math.abs(yPositions.get(j) - num2);
                if (Difference_y < difference_2) { // checks if difference is getting smaller
                    index2 = j;
                    difference_2 = Difference_y;
                }
            }
        }
        int closestXPosition = 0;
        int closestYPosition = 0;
        if (currentRoad.getTrafficDirection().equals(Constant.EAST) || currentRoad.getTrafficDirection().equals(Constant.WEST)) {
            closestXPosition = xPositions.get(index);
        } else {
            closestYPosition = yPositions.get(index2);
        }
        System.out.println(closestXPosition);

        for (int z = 0; z < DataContainer.roads.size(); z++) {
            Road r = DataContainer.roads.get(z);
            if ((r.getRoadXPos() == closestXPosition || r.getEndRoadXPos() == closestXPosition) && r.getOrientation().equals(Constant.HORIZONTAL)) {
                nextRoad = r;
            } else if ((r.getRoadYPos() == closestXPosition || r.getEndRoadYPos() == closestXPosition) && r.getOrientation().equals(Constant.VERTICAL)) {
                nextRoad = r;
            }
            if ((r.getRoadYPos() == closestYPosition || r.getEndRoadXPos() == closestYPosition) && r.getOrientation().equals(Constant.HORIZONTAL)) {
                nextRoad = r;
            } else if ((r.getRoadXPos() == closestYPosition || r.getEndRoadXPos() == closestYPosition) && r.getOrientation().equals(Constant.VERTICAL)) {
                nextRoad = r;
            }
        }
        xPositions.clear();
        yPositions.clear();
        return nextRoad;
    }


    /**
     * 车子移动
     */
    public void move() {
        if (canMoveForward()) {
            if (road.getTrafficDirection().equals(Constant.EAST) || road.getTrafficDirection().equals(Constant.SOUTH)) {
                xPos += 25;
            } else if (road.getTrafficDirection().equals(Constant.WEST) || road.getTrafficDirection().equals(Constant.NORTH)) {
                xPos -= 25;
            }
            if (checkIfAtEndOfRoad()) {
                try {
                    Road r = nextRoad();
                    setCurrentRoad(r);
                    if (r.getOrientation().equals(Constant.HORIZONTAL) && r.getTrafficDirection().equals(Constant.EAST) || r.getOrientation().equals(Constant.VERTICAL) && r.getTrafficDirection().equals(Constant.SOUTH)) {
                        for (int x = r.getRoadXPos(); x + getCarWidth() < r.getRoadLength() * 25 + r.getEndRoadXPos(); x = x + 30) {
                            setCarXPosition(x);
                            setCarYPosition(getRoadCarIsOn().getRoadYPos() + 5);
                            if (!collision(x, this)) {
                                return;
                            }
                        }
                    } else if (r.getOrientation().equals(Constant.HORIZONTAL) && r.getTrafficDirection().equals(Constant.WEST) || r.getOrientation().equals(Constant.VERTICAL) && r.getTrafficDirection().equals(Constant.NORTH)) {
                        for (int x = r.getRoadXPos() + r.getRoadLength() * 25 - getCarWidth(); x > r.getRoadXPos(); x = x - 30) {
                            setCarXPosition(x);
                            setCarYPosition(getRoadCarIsOn().getRoadYPos() + 5);
                            if (!collision(x, this)) {
                                return;
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    setCurrentRoad(road);
                    xPos = road.getRoadXPos();
                    yPos = road.getRoadYPos() + 5;
                }
            }
        }

    }

}
