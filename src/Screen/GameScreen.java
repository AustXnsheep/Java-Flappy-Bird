package Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameScreen {
    private JFrame frame;
    private JPanel panel;
    private JScrollPane scrollPane;

    private int yPosition = 300;
    private int xPosition = 10;
    private int speed = 10;
    private double velocity = 0;
    private double gravity = 0.5;
    private double jumpStrength = -10;

    public GameScreen() {
        frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw checkered background
                for (int x = 0; x < getWidth(); x += 50) {
                    for (int y = 0; y < getHeight(); y += 50) {
                        if ((x / 50) % 2 == (y / 50) % 2) {
                            g.setColor(Color.WHITE);
                        } else {
                            g.setColor(Color.GRAY);
                        }
                        g.fillRect(x, y, 50, 50);
                    }
                }

                g.setColor(Color.RED);
                g.fillRect(xPosition, yPosition, 200, 200);
            }
        };
        panel.setPreferredSize(new Dimension(800, 600));

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "jumpAction");
        panel.getActionMap().put("jumpAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                velocity = jumpStrength;
            }
        });

        scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Timer timer = new Timer(16, e -> {
            velocity += gravity;
            yPosition += velocity;
            xPosition += speed;

            if (yPosition > 300) {
                yPosition = 300;
                velocity = 0;
            }

            JViewport viewport = scrollPane.getViewport();
            viewport.setViewPosition(new Point(xPosition, yPosition)); // Adjusted this line

            panel.repaint();
        });
        timer.start();
    }
}
