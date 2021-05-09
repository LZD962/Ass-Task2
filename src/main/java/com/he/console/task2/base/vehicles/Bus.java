package com.he.console.task2.base.vehicles;

import com.he.console.task2.base.Road;
import com.he.console.task2.base.Vehicles;

import java.awt.*;

public class Bus extends Vehicles {
    public Bus(Road road){
        super(road);
        width = 45;
        height = 15;
    }
    @Override
    public void paintMeHorizontal(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(xPos, yPos, width, height);
    }
    @Override
    public void paintMeVertical(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(yPos, xPos, height, width);
    }}
