
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
import com.jwowserver.login.utils.ShaInstance;
import com.jwowserver.login.utils.Utils;

public class CMD_AUTH_LOGON_CHALLENGE extends GenericWowPacketHandler {

    private static final String N_VALUE = "894B645E89E1535BBDAD5B8B290650530801B18EBFBF5E8FAB3C82872A3E9BB7";
    VerificationData data;

    @Override
    public void init(ClientSocket socket) {
        super.init(socket);
        data = socket.getVerificationData();
    }

    @Override
    public void handlePacket(CPacket pck) {

        // uint8 error, uint16 fullSize
        pck.skip(3);

        int c = 4;
        char name[] = new char[4];
        for (int i = 0; i < c; i++) {
            name[i] = (char) pck.getUInt8();
        }

        // Some unused variables.
        pck.skip(3);

        int buildVersion = pck.getInt16();

        if (buildVersion != 5875) {
            sendAuthResult(AuthResults.WOW_FAIL_VERSION_INVALID);
            return;
        }

        pck.skip(16);

        // Skip IP since it can be modified.
        pck.skip(4);

        int length = pck.getUInt8();

        char loginName[] = new char[length];
        for (int i = 0; i < length; i++) {
            loginName[i] = (char) pck.getInt8();
        }

        String loginStr = String.copyValueOf(loginName);

        AccountStorage accountStorage = Storage.getInstance().getAccountStorage();
        Account acc = accountStorage.getAccountFromDB(loginStr.toUpperCase());

        if (acc == null)
        {
            sendAuthResult(AuthResults.WOW_FAIL_UNKNOWN_ACCOUNT);
            return;
        }

        if (accountStorage.isBannedIp(socket.getIp()))
        {
            sendAuthResult(AuthResults.WOW_FAIL_BANNED);
            return;
        }

        if (acc.isLocked() && !acc.getLastIp().equalsIgnoreCase(socket.getIp()))
        {
            sendAuthResult(AuthResults.WOW_FAIL_BANNED);
            return;
        }

        AuthResults status = accountStorage.getBanStatus(acc.getId());

        if (status != AuthResults.WOW_SUCCESS)
        {
            sendAuthResult(status);
            return;
        }

        socket.setAccount(acc);

        data.randomInt = new BigNumber(Utils.generateRandomHexString(64), 16);

        byte[] shaPasswordBytes = Utils.hexStringToByteArray(acc.getShaPassword());
        String secretAccountHash = new ShaInstance().update(data.randomInt.toByteArray())
                .updateLittleEndian(shaPasswordBytes).getDigestStr();

        data.base = new BigNumber("7");
        data.N = new BigNumber(N_VALUE, 16);
        BigNumber r = new BigNumber(secretAccountHash, 16);

        data.v = data.base.modPow(r, data.N);

        data.b = new BigNumber(Utils.generateRandomHexString(38), 16);
        BigNumber gmod = data.base.modPow(data.b, data.N);

        data.B = (BigNumber) data.v.multiply(new BigNumber("3")).add(gmod).mod(data.N);

        send();
    }

    public void send() {
        BigNumber unk = new BigNumber(Utils.generateRandomHexString(32), 16);

        SPacket outPkt = new SPacket(Opcodes.CMD_AUTH_LOGON_CHALLENGE, 1 + 1 + 1 + 32 + 1 + 1 + 1 + 32 + 32 + 16 + 1);
        outPkt.write((byte) 0);
        outPkt.write((byte) 0);
        outPkt.write(data.B.toByteArray(32));
        outPkt.write((byte) 1);
        outPkt.write(data.base.toByteArray()[0]);
        outPkt.write((byte) 32);
        outPkt.write(data.N.toByteArray(32));
        outPkt.write(data.randomInt.toByteArray(32));
        outPkt.write(unk.toByteArray(16));
        outPkt.write((byte) 0);

        sendPacket(outPkt);
    }
}
