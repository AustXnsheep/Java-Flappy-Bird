package Screen;

import Objects.RectangleObj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class GameScreen {
    private JFrame frame;
    private JPanel panel;
    private JScrollPane scrollPane;

    private int yPosition = 300;
    private final int xPosition = 75;
    private final int width = 100;
    private final int height = 100;
    private boolean isDead = false;
    private int angleInDegrees = 0;
    private Color playerColor = Color.RED;

    private final ImageIcon flappy_bird = new ImageIcon(Objects.requireNonNull(getClass().getResource("/bin/Assets/Flappy_Bird.png")));
    private final Image final_flappy_bird_image = flappy_bird.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    private final ImageIcon bgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/bin/Assets/BackGround.png")));
    private final Image bgImage = bgIcon.getImage().getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);

    private final int speed = 10;
    private double velocity = 0;
    private final double gravity = 0.5;
    private final double jumpStrength = -10;
    private java.util.ArrayList<RectangleObj> rectangleList = new ArrayList<>();


    //Every time I code a project it reminds me of how little I've come over the years
    public GameScreen() {
        frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
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
                for (RectangleObj rect : rectangleList) {
                    g.setColor(Color.GREEN);
                    g.fillRect(rect.x-10, rect.height-5, rect.width + 20, 20);
                    g.fillRect(rect.x-10, rect.height+250, rect.width + 20, 20);
                    g.fillRect(rect.x, rect.y, rect.width, rect.height);
                }
                for (int x = 0; x < getWidth(); x += frame.getWidth()) {
                    for (int y = 0; y < getHeight(); y += frame.getHeight()) {
                        g.drawImage(bgImage, x, y, this);
                    }
                }
                g.setColor(playerColor);
                Graphics2D g2d = (Graphics2D) g;
                double rotationPointX = xPosition + final_flappy_bird_image.getWidth(this) / 2.0;
                double rotationPointY = yPosition + final_flappy_bird_image.getHeight(this) / 2.0;
                g2d.rotate(Math.toRadians(angleInDegrees), rotationPointX, rotationPointY);
                g.setColor(playerColor);
                g.drawImage(final_flappy_bird_image, xPosition, yPosition, playerColor, this);
                g2d.setTransform(new AffineTransform());
            }
        };
        panel.setPreferredSize(new Dimension(800, 600));

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "jumpAction");
        panel.getActionMap().put("jumpAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isDead) {
                    velocity = jumpStrength;
                }
            }
        });

        scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Timer timer = new Timer(16, e -> {
            if (isDead) {
                playerColor = Color.BLACK;
            }
            velocity += gravity;
            yPosition += velocity;
            if (!isDead) {
                angleInDegrees = (int) Math.max(-10, Math.min(10, velocity));
            }
            if (yPosition > frame.getHeight() - 150) {
                yPosition = frame.getHeight() - 150;
                velocity = 0;
            }

            for (RectangleObj rectangles : rectangleList) {
                if (!isDead) {
                    rectangles.setX(rectangles.x - speed);

                    if (xPosition < rectangles.x + rectangles.width &&
                            xPosition + width > rectangles.x &&
                            yPosition < rectangles.y + rectangles.height &&
                            yPosition + height > rectangles.y) {
                        isDead = true;
                        angleInDegrees = 90;
                        System.out.println("Hit detected!");
                    }
                }
            }
            panel.repaint();
        });
        Timer createRectangle = new Timer(2500, e -> {
            if (!isDead) {
                Random random = new Random();
                int height = 50 + random.nextInt(400);
                RectangleObj toprect = new RectangleObj(frame.getWidth() - 100, 0, 50, height);
                RectangleObj bottomrect = new RectangleObj(frame.getWidth() - 100, height + 250, 50, 1000);
                rectangleList.add(toprect);
                rectangleList.add(bottomrect);
            }
        });
        createRectangle.start();
        timer.start();
    }
}
