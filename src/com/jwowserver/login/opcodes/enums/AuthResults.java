
package com.jwowserver.login.opcodes.enums;

public enum AuthResults {
    WOW_SUCCESS(0),
    WOW_FAIL_UNKNOWN0(1),
    WOW_FAIL_UNKNOWN1(2),
    WOW_FAIL_BANNED(3),
    WOW_FAIL_UNKNOWN_ACCOUNT(4),
    WOW_FAIL_INCORRECT_PASSWORD(5),
    WOW_FAIL_ALREADY_ONLINE(6),
    WOW_FAIL_NO_TIME(7),
    WOW_FAIL_DB_BUSY(8),
    WOW_FAIL_VERSION_INVALID(9),
    WOW_FAIL_VERSION_UPDATE(10),
    WOW_FAIL_INVALID_SERVER(11),
    WOW_FAIL_SUSPENDED(12),
    WOW_FAIL_FAIL_NOACCESS(13),
    WOW_SUCCESS_SURVEY(14),
    WOW_FAIL_PARENTCONTROL(15),
    WOW_FAIL_LOCKED_ENFORCED(16);

    private int intVal;

    AuthResults(int intValIn) {
        intVal = intValIn;
    }

    public int getIntVal() {
        return intVal;
    }

}
