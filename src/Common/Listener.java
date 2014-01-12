package Common;

import AmusingRectangles.Entity;

import java.util.ArrayList;

public interface Listener {
    public void onReceived(Packet p);
    public void onConnected(AuthResponse response);
    public void onClientAdded();
    public Entity[] onAskForEntities();
    void onUpdate(Update o);
}