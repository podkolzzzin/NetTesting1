package AmusingRectangles;

import Common.AuthResponse;
import Common.Console;
import Common.Listener;
import Common.Packet;
import Common.Update;
import Star.Client;
import Star.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

public class Component extends Canvas implements Runnable {
    public static final int WIDTH = 720;
    public static final int HEIGHT = WIDTH * 3 / 4;

    private static int frameCount = 0;
    private static int fps = 0;

    private boolean isRunning = false;

    private BufferedImage buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    private InputHandler input = new InputHandler(this);

    private Field field;

    private Server server;
    private Client client;

    private int userId;

    public Component() {
        Dimension d = new Dimension(WIDTH, HEIGHT);
        setMinimumSize(d);
        setPreferredSize(d);
        setMaximumSize(d);
    }

    public void init() {
        createBufferStrategy(3);
        requestFocus();

        client = new Client("UDP");
        field = new Field(this);

        client.addEventListener(new Listener() {
            @Override
            public void onReceived(Packet p) {
                Entity.serialId = (int) p.id;
                field.addRect((int) p.id, p.x, p.y, p.owner, true);
            }

            @Override
            public void onConnected(AuthResponse response) {
                // I am a pure client and want to gain the current game state
                if (server == null) {
                    //Entity.serialId = response.startId;
                    userId = response.yourId;

                    for (int i = 0, len = response.entities.length; i < len; ++i) {
                        Entity entity = response.entities[i];
                        field.addRect(response.entities[i].getId(), entity.getX(), entity.getY(), entity.getOwner(), true);
                    }
                }

                field.init();
            }

            @Override
            public void onClientAdded() {
            }

            @Override
            public Entity[] onAskForEntities() {
                return field.getRects().values().toArray(new Entity[]{});
            }

            @Override
            public void onUpdate(Update o) {
                ARect rect = field.getRects().get(o.id);
                rect.setX(o.x);
                rect.setY(o.y);
            }

            @Override
            public void onDisconnect(int userId) {
                Console.writeLine("Disconnection: " + userId);
                field.removeAllByOwner(userId);
            }
        });

        String action = "";
        while (!action.equals("s") && !action.equals("c")) {
            action = readString("Do you want to a server or a client (s/c): ");
        }

        if (action.equals("s")) {
            server = new Server("UDP");

            try {
                client.connect("localhost");
                server.setClient(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                client.connect(readString("Server's IP: "));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        isRunning = true;
        (new Thread(this, "Amusing Rectangles Thread")).start();
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        init();

        final double GAME_HERTZ = 30.0;
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
        final int MAX_UPDATES_BEFORE_RENDER = 5;
        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();
        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (isRunning) {
            double now = System.nanoTime();
            int updateCount = 0;

            while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                update();
                lastUpdateTime += TIME_BETWEEN_UPDATES;
                updateCount++;
            }

            if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                lastUpdateTime = now - TIME_BETWEEN_UPDATES;
            }

            render();
            lastRenderTime = now;

            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if (thisSecond > lastSecondTime) {
                //System.out.println("NEW SECOND " + thisSecond + " " + fps);
                fps = frameCount;
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                Thread.yield();

                try {
                    Thread.sleep(1);
                } catch (Exception e) {}

                now = System.nanoTime();
            }
        }
    }

    private void update() {
        input.update();
        field.update();
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        g.fillRect(0, 0, getWidth(), getHeight());

        Graphics dg = buffer.getGraphics();

        Graphics2D g2d = (Graphics2D) dg;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        field.render(g);

        g.setColor(Color.white);
        g.setFont(new Font("Tahoma", Font.PLAIN, 14));
        g.drawString(server == null ? "Client" : "Server", 10, 20);

        g.drawImage(buffer, 0, 0, buffer.getWidth(), buffer.getHeight(), null);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Breakout");
        Component game = new Component();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        game.start();
    }

    // =========
    // Utilities

    public static String readString(String placeholder) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(placeholder);

        while (scanner.hasNextLine()) {
            return scanner.nextLine();
        }

        return "";
    }

    // =======
    // Getters

    public Client getClient() {
        return client;
    }

    public InputHandler getInput() {
        return input;
    }

    public int getUserId() {
        return userId;
    }
}