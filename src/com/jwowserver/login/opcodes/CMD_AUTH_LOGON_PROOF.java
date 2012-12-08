package com.jwowserver.login.opcodes;

import com.jwowserver.login.network.CPacket;
import com.jwowserver.login.network.ClientSocket;
import com.jwowserver.login.network.SPacket;
import com.jwowserver.login.network.VerificationData;
import com.jwowserver.login.opcodes.enums.AuthResults;
import com.jwowserver.login.opcodes.enums.Opcodes;
import com.jwowserver.login.storage.AccountStorage;
import com.jwowserver.login.storage.Storage;
import com.jwowserver.login.storage.objects.Account;
import com.jwowserver.login.utils.BigNumber;
import com.jwowserver.login.utils.Config;
import com.jwowserver.login.utils.ShaInstance;
import com.jwowserver.login.utils.Utils;

public class CMD_AUTH_LOGON_PROOF extends GenericWowPacketHandler {
	
	VerificationData data;
	
	@Override
	public void init(ClientSocket socket) {
		super.init(socket);
		data = socket.getVerificationData();
	}
	
	@Override
	public void handlePacket(CPacket pck) {
		
		Account acc = socket.getAccount();
		if(acc == null) {
			sendAuthResult(AuthResults.WOW_FAIL_VERSION_INVALID); //We should never get this opcode if we do not have an account
		}
		
		byte[] A = pck.getByteArray(32);
		BigNumber bigA = new BigNumber(A);
		
		ShaInstance instance = new ShaInstance();
		instance.update(bigA.toByteArray()).update(data.B.toByteArray());
		String hex = instance.getDigestStr();
		
		BigNumber u = new BigNumber(hex,16);
		BigNumber S = bigA.multiply(data.v.modPow(u, data.N)).modPow(data.b, data.N);
		
		byte[] t = S.toLittleEndianBytes(32);
		byte[] t1 = new byte[16];
		byte[] vK = new byte[40];
		for (int i = 0; i < 16; i++)
		{
			t1[i] = t[i*2];
		}
		
		byte[] shaArray = new ShaInstance().updateLittleEndian(t1).getDigest();
		for (int i = 0; i < 20; i++)
		{
			vK[i*2+1] = shaArray[i];
		}
		for (int i = 0; i < 16; i++)
		{
			t1[i] = t[i*2+1];
		}
		
		shaArray = new ShaInstance().updateLittleEndian(t1).getDigest();
		for (int i = 0; i < 20; i++)
		{
			vK[i*2] = shaArray[i];
		}
		
		byte[] hash = new ShaInstance().update(data.N.toByteArray()).getDigest();
		shaArray = new ShaInstance().update(data.base.toByteArray()).getDigest();
		for (int i = 0; i < 20; i++)
		{
			hash[i] ^= shaArray[i];
		}
		
		shaArray = new ShaInstance().update(acc.getUsername()).getDigest();

		instance = new ShaInstance();
		instance.update(hash);
		instance.update(shaArray);
		instance.update(data.randomInt.toByteArray());
		instance.update(bigA.toByteArray());
		instance.update(data.B.toByteArray());
		instance.update(vK);
		String sha1 = instance.getDigestStr();

		byte M1[] = pck.getByteArray(20);
		BigNumber M1Value = new BigNumber(M1);
		
		AccountStorage accountStorage = Storage.getInstance().getAccountStorage();
		
		if(Utils.byteArrayToHexString(M1Value.toByteArray()).equalsIgnoreCase(sha1) )
		{
			acc.setSessionKey(Utils.byteArrayToHexString(vK));
			acc.setLastIp(getSocket().getIp());
			socket.setVerified(true);
			
			accountStorage.setAccountAuthenticated(acc);
			
			send(A, M1, vK);
		}else
		{
			accountStorage.incrementFailedLogins(acc);
			int failedLogins = accountStorage.getFailedLoginCount(acc);
			
			Config config = Config.getInstance();
			if(failedLogins == config.getMaxWrongPasswords())
			{
				int banTime = config.getWrongPassBanTime();
                boolean banType = config.getWrongPassBanType();
                String bannerName = "MaNGOS realmd";
                String reason = "Failed login autoban";
                
                if(banType) {
                	accountStorage.banAccount(acc, banTime, bannerName, reason);
                }else {
                	accountStorage.banIp(getSocket().getIp(), banTime, bannerName, reason);
                }
			}
			
			sendAuthResult(AuthResults.WOW_FAIL_INCORRECT_PASSWORD);
		}
	}
	
	public void send(byte[] a, byte[] m1, byte[] sessionKey) {
		
		ShaInstance instance = new ShaInstance();
		instance.update(a);
		instance.update(m1);
		instance.update(sessionKey);
		
		SPacket outPkt = new SPacket(Opcodes.CMD_AUTH_LOGON_PROOF, 1+1+1+32+1+1+1+32+32+16+1);
		outPkt.write((byte) 0); //Error
		outPkt.write(instance.getDigest());
		outPkt.write(0); //Unknown
		
		sendPacket(outPkt);
	}
}
