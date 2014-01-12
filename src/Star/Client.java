package Star;

import AmusingRectangles.Entity;
import Common.AuthResponse;
import Common.Console;
import Common.Disconnected;
import Common.Listener;
import Common.NetworkEntity;
import Common.Packet;
import Common.Update;
import com.esotericsoftware.kryonet.Connection;

import java.io.IOException;

/**
 * Created by podko_000
 * At 18:32 on 12.01.14
 */

public class Client extends NetworkEntity {
    private com.esotericsoftware.kryonet.Client client;
    private Listener listener;

    public Listener getListener() {
        return listener;
    }

    public Client(String protocol) {
        setProtocol(protocol);
        client = new com.esotericsoftware.kryonet.Client();
        client.addListener(this);
        register(client.getKryo());
    }

    public void connect(String ip) throws IOException {
        startOperation();
        client.start();
        client.connect(5000, ip, TCP_PORT, UDP_PORT);
        finishOperation();
    }

    public void send(int x, int y, int w, int h, int id) {
        startOperation();
        Packet p = new Packet();
        p.x = x;
        p.y = y;
        p.width = w;
        p.height = h;
        p.id = id;
        if (getProtocol().equals("UDP")) { client.sendUDP(p); } else { client.sendTCP(p); }
        finishOperation();
    }

    public void send(Entity e) {
        send(e.getX(), e.getY(), e.getWidth(), e.getHeight(), e.getId());
    }

    public void update(Entity e) {
        startOperation();
        Update u = new Update();
        u.x = e.getX();
        u.y = e.getY();
        u.id = e.getId();
        if (getProtocol().equals("UDP")) { client.sendUDP(u); } else { client.sendTCP(u); }
        finishOperation();
    }

    public void addEventListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void received(Connection connection, Object o) {
        if (o instanceof Packet) {
            //Console.writeLine(o);
            if (listener != null) { listener.onReceived((Packet) o); }
        }
        else if(o instanceof AuthResponse) {
            Console.writeLine("Start iterate IDs from "+ o);
            if(listener!=null) {
                listener.onConnected((AuthResponse) o);
            }
        }
        else if(o instanceof Update) {
            if(listener!=null)
                listener.onUpdate((Update)o);
        }
        else if(o instanceof Disconnected)
        {
            if(listener!=null)
                listener.onDisconnect(((Disconnected)o).userId);
        }
        else if(o instanceof Integer) {
            int command = (Integer) o;
            if(listener==null) {
                Console.writeLine("You must add listener to Star.Client earlier!");
                System.exit(-1);
            }
            else if(command == NetworkEntity.CLIENT_CONNECTED) {
                listener.onClientAdded();
            }
        }
    }
}
