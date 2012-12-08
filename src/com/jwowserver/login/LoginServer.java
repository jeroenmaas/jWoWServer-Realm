package com.jwowserver.login;

import java.io.IOException;
import java.io.InputStream;
import com.jwowserver.login.network.CPacket;
import com.jwowserver.login.network.ClientSocket;
import com.jwowserver.login.network.TcpServer;
import com.jwowserver.login.opcodes.CMD_AUTH_LOGON_CHALLENGE;
import com.jwowserver.login.opcodes.CMD_AUTH_LOGON_PROOF;
import com.jwowserver.login.opcodes.CMD_REALM_LIST;
import com.jwowserver.login.opcodes.GenericWowPacketHandler;
import com.jwowserver.login.storage.Storage;
import com.jwowserver.login.utils.Config;

public class LoginServer {

    public static void main(String[] args) throws Exception{

        Storage.getInstance().init();
     
        TcpServer ts = new TcpServer();                                     // Create the server
        ts.setPort( Config.getInstance().getRealmServerPort() );            // Set the port
        ts.addTcpServerListener( new TcpServer.Listener() {                 // Add listener

            @Override
            public void socketReceived( TcpServer.Event evt ) {             // New stream
            	
                try {
                    InputStream in = evt.getSocket().getInputStream();      // Input
                    ClientSocket clientSocket = new ClientSocket(evt.getSocket());
                    
                    byte[] buff = new byte[1024];                             // Buffer
                    while( (in.read(buff)) >= 0 ){
                    	
                    	CPacket pck = new CPacket(buff);
                    	byte cmd = pck.getCmd();
                    	
                    	GenericWowPacketHandler handler = null;
                    	switch(cmd)
                    	{
                    		case 0:
                    			handler = new CMD_AUTH_LOGON_CHALLENGE();
                    			break;
                    			
                    		case 1:
                    			handler = new CMD_AUTH_LOGON_PROOF();
                    			break;
                    		
                    		case 16:
                    			handler = new CMD_REALM_LIST();
                    			break;
                    	}
                    	
                    	if(handler != null)
                    	{
	                    	handler.init(clientSocket);
	                    	handler.handlePacket(pck);
                    	}
                    		
                    }
                } catch( IOException exc ) {
                    exc.printStackTrace();
                }
            }   // end socketReceived
        }); // end Listener
        ts.start();
        System.out.println("Server started on port " + Config.getInstance().getRealmServerPort() );
    }   // end main
}   // end class TcpEchoExample