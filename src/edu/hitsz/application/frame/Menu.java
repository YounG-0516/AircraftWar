package edu.hitsz.application.frame;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.game.EasyGame;
import edu.hitsz.application.game.Game;
import edu.hitsz.application.game.HardGame;
import edu.hitsz.application.game.MediumGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Menu {
    private JButton easyButton;
    private JButton hardButton;
    private JButton mediumButton;
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JComboBox musicCombox;
    private JLabel musicLabel;
    private JLayeredPane layeredPane;
    Game game = null;

    public Menu() {

        /*
        JLabel winJlabel = new JLabel(new ImageIcon("src/images/hangtian1.jpg"));
        winJlabel.setBounds(0,0,512,768);
        mainPanel.getLayeredPane().add(winJlabel, Integer.MIN_VALUE);
        ((JPanel)frame.getContentPane()).setOpaque(false);

        try {
            Image backgroundImage = ImageIO.read(new File("src/images/hangtian1.jpg"));
            mainPanel = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(backgroundImage, 0, 0, null);
                }
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        easyButton.setBounds(100,1000,200,200);
        mainPanel.add(easyButton);
        mainPanel.add(mediumButton);
        mainPanel.add(hardButton);
        mainPanel.add(musicCombox);
         */

        /*
        //添加选项
        musicCombox.addItem("开");
        musicCombox.addItem("关");
        musicCombox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String item = e.getItem().toString();
                switch (item){
                    case "开":
                        System.out.println("音效开启！");
                        break;
                    case "关":
                        System.out.println("音效关闭！");
                        break;
                    default:
                }
            }
        });
        */

        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("选择了简单模式");
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg.jpg"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Game.setGameDifficulty(1);
                Game.setMusic(musicCombox.getSelectedIndex());
                game = new EasyGame();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
            }
        });

        mediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("选择了普通模式");
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg2.jpg"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Game.setGameDifficulty(2);
                Game.setMusic(musicCombox.getSelectedIndex());
                game = new MediumGame();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
            }
        });

        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("选择了困难模式");
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg3.jpg"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Game.setGameDifficulty(3);
                Game.setMusic(musicCombox.getSelectedIndex());
                game = new HardGame();
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
            }
        });

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}