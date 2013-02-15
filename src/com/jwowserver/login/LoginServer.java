
package com.jwowserver.login;

import java.io.IOException;
import java.io.InputStream;

import com.jwowserver.login.network.CPacket;
import com.jwowserver.login.network.ClientSocket;
import com.jwowserver.login.network.TcpServer;
import com.jwowserver.login.opcodes.GenericWowPacketHandler;
import com.jwowserver.login.opcodes.enums.Opcodes;
import com.jwowserver.login.storage.Storage;
import com.jwowserver.login.utils.Config;

public class LoginServer {

    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {

        Storage.getInstance().init();

        TcpServer ts = new TcpServer(); // Create the server
        ts.setPort(Config.getInstance().getRealmServerPort()); // Set the port
        ts.addTcpServerListener(new TcpServer.Listener() { // Add listener

            @Override
            public void socketReceived(TcpServer.Event evt) { // New stream

                try {
                    InputStream in = evt.getSocket().getInputStream(); // Input
                    ClientSocket clientSocket = new ClientSocket(evt.getSocket());

                    byte[] buff = new byte[BUFFER_SIZE]; // Buffer
                    while ((in.read(buff)) >= 0) {

                        CPacket pck = new CPacket(buff);
                        byte cmd = pck.getCmd();

                        Opcodes opcode = Opcodes.get(cmd);
                        if (opcode == null) {
                            continue;
                        }

                        GenericWowPacketHandler handler = opcode.getHandler();
                        if (handler == null) {
                            continue;
                        }

                        handler.init(clientSocket);
                        handler.handlePacket(pck);
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            } // end socketReceived
        }); // end Listener
        ts.start();
        System.out.println("Server started on port " + Config.getInstance().getRealmServerPort());

    }

}
