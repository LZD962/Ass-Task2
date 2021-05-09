package com.he.console.task2;

import com.he.console.task2.base.Vehicles;
import com.he.console.task2.base.Constant;
import com.he.console.task2.base.Road;
import com.he.console.task2.base.TrafficLight;
import com.he.console.task2.base.vehicles.Bus;
import com.he.console.task2.base.vehicles.Sedan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Simulator implements ActionListener, Runnable, MouseListener {
    private int x, y;
    private boolean      running = false;
    private JFrame       frame = new JFrame("traffic sim");
    private TrafficLight light = new TrafficLight();
    Road roadStart = new Road(6, Constant.HORIZONTAL, 0, 270, Constant.EAST, light); // fixed starting road on map

    private int getX() {
        return x;
    }

    private int getY() {
        return y;
    }

    //north container
    private JLabel     info           = new JLabel("click on screen to select x,y position");
    private JLabel     labelXPosField = new JLabel("Road x position");
    private JTextField xPosField      = new JTextField("0");
    private JLabel     labelYPosField = new JLabel("Road y position");
    private JTextField yPosField      = new JTextField("0");
    private Container  north          = new Container();

    //south container
    private JButton   startSim   = new JButton("start");
    private JButton   exitSim    = new JButton("exit");
    private JButton   removeRoad = new JButton("remove last road");
    private Container south      = new Container();

    //west container
    private Container    west           = new Container();
    private JButton      addSedan       = new JButton("add sedan");
    private JButton      addBus         = new JButton("add bus");
    private JButton      addRoad        = new JButton("add road");
    //road orientation selection
    private ButtonGroup  selections     = new ButtonGroup();
    private JRadioButton horizontal     = new JRadioButton(Constant.HORIZONTAL);
    private JRadioButton vertical       = new JRadioButton(Constant.VERTICAL);
    //has traffic light selection
    private ButtonGroup  selections2    = new ButtonGroup();
    private JRadioButton hasLight       = new JRadioButton("traffic light(true)");
    private JRadioButton noLight        = new JRadioButton("traffic light(false)");
    //road length
    private JLabel       label          = new JLabel("Enter road length");
    private JTextField   length         = new JTextField("5");
    //traffic direction
    private ButtonGroup  selections3    = new ButtonGroup();
    private JRadioButton northDirection = new JRadioButton(Constant.NORTH);
    private JRadioButton southDirection = new JRadioButton(Constant.SOUTH);
    private JRadioButton westDirection  = new JRadioButton(Constant.WEST);
    private JRadioButton eastDirection  = new JRadioButton(Constant.EAST);

    public Simulator() {

        DataContainer.roads.add(roadStart);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());
        frame.add(roadStart, BorderLayout.CENTER);
        roadStart.addMouseListener(this);
        //north side info
        north.setLayout(new GridLayout(1, 5));
        north.add(info);
        north.add(labelXPosField);
        north.add(xPosField);
        north.add(labelYPosField);
        north.add(yPosField);
        frame.add(north, BorderLayout.NORTH);

        //buttons on the south side
        south.setLayout(new GridLayout(1, 3));
        south.add(startSim);
        startSim.addActionListener(this);
        south.add(exitSim);
        exitSim.addActionListener(this);
        south.add(removeRoad);
        removeRoad.addActionListener(this);
        frame.add(south, BorderLayout.SOUTH);

        //buttons on west side
        west.setLayout(new GridLayout(13, 1));
        west.add(addSedan);
        addSedan.addActionListener(this);
        west.add(addBus);
        addBus.addActionListener(this);
        west.add(addRoad);
        addRoad.addActionListener(this);
        west.add(label);
        west.add(length);
        length.addActionListener(this);

        //radio buttons on west side
        selections.add(vertical);
        selections.add(horizontal);
        west.add(vertical);
        vertical.addActionListener(this);
        horizontal.setSelected(true);
        west.add(horizontal);
        horizontal.addActionListener(this);

        selections2.add(hasLight);
        selections2.add(noLight);
        west.add(hasLight);
        hasLight.addActionListener(this);
        west.add(noLight);
        noLight.addActionListener(this);
        noLight.setSelected(true);

        selections3.add(northDirection);
        selections3.add(southDirection);
        selections3.add(eastDirection);
        selections3.add(westDirection);
        west.add(northDirection);
        northDirection.addActionListener(this);
        northDirection.setEnabled(false);
        west.add(southDirection);
        southDirection.addActionListener(this);
        southDirection.setEnabled(false);
        west.add(eastDirection);
        eastDirection.addActionListener(this);
        eastDirection.setSelected(true);
        west.add(westDirection);
        westDirection.addActionListener(this);

        frame.add(west, BorderLayout.WEST);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DataContainer.trafficLights.add(light);
        frame.repaint();

    }


    /**
     * 按钮点击事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        //判断哪些单选按钮可用
        if (horizontal.isSelected()) {
            northDirection.setEnabled(false);
            southDirection.setEnabled(false);
            eastDirection.setEnabled(true);
            westDirection.setEnabled(true);
        } else if (vertical.isSelected()) {
            eastDirection.setEnabled(false);
            westDirection.setEnabled(false);
            northDirection.setEnabled(true);
            southDirection.setEnabled(true);
        }
        //开始
        if (source == startSim) {
            if (!running) {
                running = true;
                Thread t = new Thread(this);
                t.start();
            }
        }
        //移出道路
        if (source == removeRoad) {
            if (DataContainer.roads.size() > 1) {
                DataContainer.roads.remove(DataContainer.roads.size() - 1);
                frame.repaint();
            }
        }
        //添加公共汽车
        if (source == addBus) {
            Bus bus = new Bus(roadStart);
            DataContainer.cars.add(bus);
            for (int x = roadStart.getRoadXPos(); x < bus.getRoadCarIsOn().getRoadLength() * 50; x = x + 30) {
                bus.setCarXPosition(x);
                bus.setCarYPosition(bus.getRoadCarIsOn().getRoadYPos() + 5);
                if (!bus.collision(x, bus)) {
                    frame.repaint();
                    return;
                }
            }
        }
        //添加轿车
        if (source == addSedan) {
            Sedan sedan = new Sedan(roadStart);
            DataContainer.cars.add(sedan);
            sedan.setCarYPosition(sedan.getRoadCarIsOn().getRoadYPos() + 5);
            for (int x = roadStart.getRoadXPos(); x < sedan.getRoadCarIsOn().getRoadLength() * 50; x = x + 40) {
                sedan.setCarXPosition(x);
                if (!sedan.collision(x, sedan)) {
                    frame.repaint();
                    return;
                }

            }
        }
        //添加道路
        if (source == addRoad) {
            int roadLength = 5;
            String orientation = Constant.HORIZONTAL;
            String direction = Constant.EAST;
            int xPos = 0;
            int yPos = 0;
            Boolean lightOnRoad = false;
            if (vertical.isSelected()) {
                orientation = Constant.VERTICAL;
            } else if (horizontal.isSelected()) {
                orientation = Constant.HORIZONTAL;
            }
            if (hasLight.isSelected()) {
                lightOnRoad = true;
            } else if (noLight.isSelected()) {
                lightOnRoad = false;
            }
            if (eastDirection.isSelected()) {
                direction = Constant.EAST;
            } else if (westDirection.isSelected()) {
                direction = Constant.WEST;
            } else if (northDirection.isSelected()) {
                direction = Constant.NORTH;
            } else if (southDirection.isSelected()) {
                direction = Constant.SOUTH;
            }

            if (orientation.equals(Constant.HORIZONTAL)) {
                yPos = Integer.parseInt(yPosField.getText());
                xPos = Integer.parseInt(xPosField.getText());
            } else if (orientation.equals(Constant.VERTICAL)) {
                xPos = Integer.parseInt(yPosField.getText());
                yPos = Integer.parseInt(xPosField.getText());
            }
            try {
                roadLength = Integer.parseInt(length.getText());
            } catch (Exception error) {
                JOptionPane.showMessageDialog(null, "road length needs an integer");
                length.setText("5");
            }
            if (lightOnRoad) {
                Road road = new Road(roadLength, orientation, xPos, yPos, direction, new TrafficLight());
                DataContainer.roads.add(road);
            } else {
                Road road = new Road(roadLength, orientation, xPos, yPos, direction);
                DataContainer.roads.add(road);
            }
            frame.repaint();

        }
        //退出
        if (source == exitSim) {
            System.exit(0);
        }
    }

    /**
     *鼠标点击事件，获取点击的x,y坐标
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        xPosField.setText(Integer.toString(getX()));
        yPosField.setText(Integer.toString(getY()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void run() {
        boolean carCollision = false;
        ArrayList<Boolean> trueCases = new ArrayList<Boolean>();
        while (running) {
            try {
                Thread.sleep(300);
            } catch (Exception ignored) {
            }
            for (int j = 0; j < DataContainer.roads.size(); j++) {
                Road r = DataContainer.roads.get(j);
                TrafficLight l = r.getTrafficLight();
                if (l != null) {
                    l.operate();
                    if (l.getCurrentColor().equals("red")) {
                        r.setLightColor(Color.red);
                    } else {
                        r.setLightColor(Color.GREEN);
                    }
                }

            }
            for (int i = 0; i < DataContainer.cars.size(); i++) {
                Vehicles currentCar = DataContainer.cars.get(i);
                String direction = currentCar.getRoadCarIsOn().getTrafficDirection();
                if (!currentCar.collision(currentCar.getCarXPosition() + 30, currentCar) && (direction.equals(Constant.EAST) || direction.equals(Constant.SOUTH))
                        || !currentCar.collision(currentCar.getCarXPosition(), currentCar) && (direction.equals(Constant.WEST) || direction.equals(Constant.NORTH))) {
                    currentCar.move();
                } else {
                    for (int z = 0; z < DataContainer.cars.size(); z++) {
                        Vehicles otherCar = DataContainer.cars.get(z);
                        if (otherCar.getCarYPosition() != currentCar.getCarYPosition()) {
                            if (currentCar.getCarXPosition() + currentCar.getCarWidth() < otherCar.getCarXPosition()) {
                                trueCases.add(true); // safe to switch lane
                            } else {
                                trueCases.add(false); // not safe to switch lane
                            }
                        }
                    }
                    for (int l = 0; l < trueCases.size(); l++) {
                        if (!trueCases.get(l)) {
                            carCollision = true;
                            break;
                        }
                    }
                    if (!carCollision) {
                        currentCar.setCarYPosition(currentCar.getRoadCarIsOn().getRoadYPos() + 30);
                    }
                    for (int m = 0; m < trueCases.size(); m++) {
                        trueCases.remove(m);
                    }
                    carCollision = false;
                }

            }
            frame.repaint();

        }
    }
}
