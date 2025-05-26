package hexcells;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MenuWindow extends JFrame {
    public MenuWindow() {
        setTitle("Hexcells");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton level1Button = new JButton("Уровень 1");
        JButton level2Button = new JButton("Уровень 2");
        JButton level3Button = new JButton("Уровень 3");
        JButton exitButton = new JButton("Выход");

        level1Button.addActionListener(e -> {
            dispose();
            LevelConfig config = createLevel1Config();
            new GameWindow(new Board(config)).setVisible(true);
        });

        level2Button.addActionListener(e -> {
            dispose();
            LevelConfig config = createLevel2Config();
            new GameWindow(new Board(config)).setVisible(true);
        });

        level3Button.addActionListener(e -> {
            dispose();
            LevelConfig config = createLevel3Config();
            new GameWindow(new Board(config)).setVisible(true);
        });

        exitButton.addActionListener(e -> System.exit(0));

        add(level1Button);
        add(level2Button);
        add(level3Button);
        add(exitButton);

        pack();
        setLocationRelativeTo(null);
    }

    private LevelConfig createLevel1Config() {
        LevelConfig config = new LevelConfig(10, 10);
        config.addMine(new HexCoord(5, 5));
        config.addMine(new HexCoord(6, 3));
        config.addRule(new EdgeRule(new HexCoord(5, 4), 2));
        return config;
    }

    private LevelConfig createLevel2Config() {
        LevelConfig config = new LevelConfig(15, 15);
        config.addMine(new HexCoord(7, 7));
        config.addMine(new HexCoord(8, 8));
        config.addRule(new GroupRule(Arrays.asList(new HexCoord(6, 6), new HexCoord(6, 7), new HexCoord(7, 6)), 1));
        return config;
    }

    private LevelConfig createLevel3Config() {
        LevelConfig config = new LevelConfig(20, 20);
        config.addMine(new HexCoord(10, 10));
        config.addMine(new HexCoord(12, 12));
        config.addRule(new SequenceRule(Arrays.asList(new HexCoord(10, 10), new HexCoord(10, 11), new HexCoord(10, 12)), 2));
        return config;
    }
}