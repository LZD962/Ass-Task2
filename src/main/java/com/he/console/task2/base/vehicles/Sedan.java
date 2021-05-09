package com.he.console.task2.base.vehicles;

import com.he.console.task2.base.Road;
import com.he.console.task2.base.Vehicles;

import java.awt.*;

public class Sedan extends Vehicles {
    public Sedan(Road road){
        super(road);
        width = 30;
        height = 12;
    }
    @Override
    public void paintMeHorizontal(Graphics g){
        g.setColor(Color.CYAN);
        g.fillRect(xPos, yPos, width, height);
    }
    @Override
    public void paintMeVertical(Graphics g){
        g.setColor(Color.CYAN);
        g.fillRect(yPos, xPos, height, width);
    }
}
