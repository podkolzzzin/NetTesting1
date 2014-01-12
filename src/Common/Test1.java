package Common;

import AmusingRectangles.Entity;
import Star.Client;
import Star.Server;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by podko_000
 * At 19:02 on 12.01.14
 */

public class Test1 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server("UDP");
        for (int i = 0; i < 30; i++) {
            Client c = new Client("TCP");
            new ForThread(c);
        }

    }

    static class ForThread implements Runnable {
        int x, y, w, h;
        private Client client;

        public ForThread(Client c) throws IOException, InterruptedException {
            client = c;
            client.connect("localhost");
            Thread.currentThread().sleep(100);
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
                for (int i = 0; i < 25; i++) {
                    x += Math.random() * 10 - 5;
                    y += Math.random() * 10 - 5;
                    w += Math.random() * 5 - 2;
                    h += Math.random() * 5 - 2;
                    client.send(x, y, w, h, i);
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }
    }
}

