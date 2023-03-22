package com.mygdx.shapewars.network.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.network.data.GameResponse;
import com.mygdx.shapewars.network.data.InputRequest;

import java.io.IOException;
import java.util.UUID;

public class ServerConnector {

    private Server server;
    private Kryo kryo;
    private ShapeWarsModel model;

    public ServerConnector(ShapeWarsModel model) {
        this.server = new Server();
        this.server.start();
        this.kryo = kryo;

        try {
            server.bind(25444, 25666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.kryo = server.getKryo();
        this.kryo.register(InputRequest.class);
        this.kryo.register(GameResponse.class);
        kryo.register(UUID.class);

        this.server.addListener(new ServerListener(model));

    }
}