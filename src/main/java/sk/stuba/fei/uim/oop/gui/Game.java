package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.GameLogic;

import javax.swing.*;
import java.awt.*;

import static sk.stuba.fei.uim.oop.controls.GameLogic.CHECK;
import static sk.stuba.fei.uim.oop.controls.GameLogic.RESTART;

public class Game {

    public Game() {
        JFrame frame = new JFrame("Pipes!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,800);
        frame.setResizable(false);

        frame.setLayout(new BorderLayout());
        GameLogic logic = new GameLogic(frame);
        frame.addMouseMotionListener(logic);
        frame.addMouseListener(logic);
        frame.addKeyListener(logic);
        frame.add(logic.getGraphicsHandler());

        frame.setFocusable(true);
        frame.requestFocus();

        JPanel sideMenu = new JPanel();
        sideMenu.setBackground(Color.white);
        JButton buttonRestart = new JButton(RESTART);
        JButton check = new JButton(CHECK);
        buttonRestart.addActionListener(logic);
        buttonRestart.setFocusable(false);
        check.addActionListener(logic);
        check.setFocusable(false);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 8, 16, 8);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(2);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(logic);

        sideMenu.setLayout(new GridLayout(2, 2));
        sideMenu.add(logic.getLabel());
        sideMenu.add(buttonRestart);
        sideMenu.add(check);
        sideMenu.add(logic.getBoardSizeLabel());
        sideMenu.add(slider);

        frame.add(sideMenu, BorderLayout.PAGE_END);
        frame.setVisible(true);
    }
}
