package com.jwowserver.login.opcodes;

import java.util.ArrayList;

import com.jwowserver.login.network.CPacket;
import com.jwowserver.login.network.SPacket;
import com.jwowserver.login.opcodes.enums.Opcodes;
import com.jwowserver.login.storage.Storage;
import com.jwowserver.login.storage.objects.Realm;

public class CMD_REALM_LIST extends GenericWowPacketHandler {
	
	@Override
	public void handlePacket(CPacket pck)  {
		if(getSocket().isVerified() == true) {
			this.send();
		}
	}
	
	public void send() {
		SPacket outPkt = new SPacket(Opcodes.CMD_REALM_LIST, 170);
		outPkt.write((short) 0); //Size placeholder
		outPkt.write(0); //Unknown
		
		ArrayList<Realm> realms = Storage.getInstance().getRealmListStorage().getRealms();
		
		outPkt.write((byte) realms.size()); //RealmCount
		
		for(Realm r : realms)
		{
			outPkt.write((int) r.getIcon());
			outPkt.write((byte) r.getRealmflags()); 
			
			outPkt.write(r.getName()); //Color
			outPkt.write(r.getAddress() + ":" + r.getPort()); //Color
			outPkt.write(r.getPopulation()); //Color
			
			int charCount = Storage.getInstance().getAccountStorage().getCharacterCount(r.getId(), getSocket().getAccount().getId());
			
			outPkt.write((byte) charCount); //Characters
			outPkt.write((byte) r.getTimezone()); //Timezone
			outPkt.write((byte) 1); //Online
		}
		
		outPkt.write((byte) 0x15);
		outPkt.write((byte) 0);
		
		outPkt.setPacketSize();
		
		sendPacket(outPkt);
	}
}
