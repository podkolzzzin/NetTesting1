package Star;

import AmusingRectangles.Entity;
import Common.AuthResponse;
import Common.Console;
import Common.NetworkEntity;
import Common.Packet;
import com.esotericsoftware.kryonet.Connection;


import java.io.IOException;
import java.util.ArrayList;

public class Server extends NetworkEntity {
    private com.esotericsoftware.kryonet.Server server;
    private Boolean entitiesReceived = false;
    private Entity[] entities;

    public Server(String protocol) {
        setProtocol(protocol);
        server = new com.esotericsoftware.kryonet.Server();
        register(server.getKryo());


        server.start();
        try {
            server.bind(TCP_PORT, UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.addListener(this);
        Console.writeLine("server started, waiting for connections");
    }

    @Override
    public void received(Connection c, Object o) {
        if (o instanceof Packet) {
            Packet p = (Packet) o;
            if (getProtocol().equals("UDP")) {
                server.sendToAllUDP(o);
            } else {
                server.sendToAllTCP(o);
            }
        }
        else if(o instanceof ArrayList) {
            entities = (Entity[]) o;
        }
    }

    @Override
    public void connected(Connection c) {
        Console.writeLine("One more user connected. The are " + server.getConnections().length + " connections now");

        if(getProtocol().equals("UDP"))
            server.sendToAllExceptUDP(c.getID(), NetworkEntity.CLIENT_CONNECTED);

        AuthResponse response = new AuthResponse();
        response.startId = (server.getConnections().length - 1) * 10000;
        if(server.getConnections().length==1)
            response.entities = new Entity[0];
        else
        {
            response.entities = askForEntities();
        }
        if(getProtocol().equals("TCP"))
            c.sendTCP(response);
        else
            c.sendUDP(response);
    }

    private Entity[] askForEntities() {
        server.getConnections()[0].sendTCP(NetworkEntity.ASK_FOR_ENTITIES);
        while (!entitiesReceived) { }
        return entities;
    }
}
