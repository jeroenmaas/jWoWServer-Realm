
package com.jwowserver.login.network;

import com.jwowserver.login.utils.BigNumber;

public class VerificationData {
    public BigNumber b; // Random generated hex size: 19 bytes
    public BigNumber randomInt = null; // Random generated hex size: 32 bytes
    public BigNumber N; // Build from N_VALUE

    public BigNumber B; // Created using v, N
    public BigNumber v; // Created from accountInfo and N

    public BigNumber base; // Has value 7
}
