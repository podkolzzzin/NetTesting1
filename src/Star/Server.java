package Star;

import AmusingRectangles.Entity;
import Common.AuthResponse;
import Common.Console;
import Common.NetworkEntity;
import Common.Packet;
import Common.Update;
import com.esotericsoftware.kryonet.Connection;


import java.io.IOException;
import java.util.ArrayList;

public class Server extends NetworkEntity {
    private com.esotericsoftware.kryonet.Server server;
    private Client connectedClient;

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

    public void setClient(Client client)
    {
        connectedClient = client;
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
        else if(o instanceof Update) {
            Update p = (Update) o;
            if (getProtocol().equals("UDP")) {
                server.sendToAllUDP(o);
            } else {
                server.sendToAllTCP(o);
            }
        }
    }

    @Override
    public void connected(Connection c) {
        Console.writeLine("One more user connected. The are " + server.getConnections().length + " connections now");

        if(getProtocol().equals("UDP"))
            server.sendToAllExceptUDP(c.getID(), NetworkEntity.CLIENT_CONNECTED);
        else
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
        return connectedClient.getListener().onAskForEntities();
    }
}
