package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.shapewars.config.Launcher;
import com.mygdx.shapewars.config.Role;
import com.mygdx.shapewars.controller.Joystick;
import com.mygdx.shapewars.network.client.ClientConnector;
import java.util.ArrayList;
import java.util.List;

public class SystemFactory {
    public static List<EntitySystem> generateSystems(Role role, Launcher launcher, Joystick joystick, ClientConnector clientConnector, String clientId) {
        List<EntitySystem> systems = new ArrayList<>();

        systems.add(SpriteSystem.getInstance());
        systems.add(launcher == Launcher.Desktop ?
                InputSystemDesktop.getInstance(role, clientConnector, clientId) :
                InputSystemMobile.getInstance(role, clientConnector, clientId, joystick));

        if (role == Role.Server) {
            systems.add(MovementSystem.getInstance());
            systems.add(RicochetSystem.getInstance());
            systems.add(DeathSystem.getInstance());
        }
        return systems;
    }
}
