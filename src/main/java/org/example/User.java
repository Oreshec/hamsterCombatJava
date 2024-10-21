package org.example;

class User {
    private static String[] authorizationKeys;
    private static String upgrades;
    private static float balanceDiamonds;
    private static String id;
    private static float earnPassivePerSec;

    public static void setId(String id) {
        User.id = id;
    }

    public static String getId() {
        return id;
    }

    public static float getBalanceDiamonds() {
        return balanceDiamonds;
    }

    public static void setBalanceDiamonds(float balanceDiamonds) {
        User.balanceDiamonds = balanceDiamonds;
    }

    public static void setAuthorization(String[] authorization) {
        authorizationKeys = authorization;
    }

    public static String[] getAuthorization() {
        return authorizationKeys;
    }

    public static void setEarnPassivePerSec(float earnPassivePerSec) {
        User.earnPassivePerSec = earnPassivePerSec;
    }

    public static float getEarnPassivePerSec() {
        return earnPassivePerSec;
    }
}
