/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tix11.jme.totp;

/**
 *
 * @author jdamico
 */
public class Totp {

    public String getCurrentOtp() {
        byte[] seed = null; // ADD YOUR SEED (a shared secret) HERE ;
        long t0 = 0;
        long x = 30;
        long unixTime = System.currentTimeMillis() / 1000L;
        String steps = "0";
        int size = 6;

        long T = (unixTime - t0) / x;
        steps = fromLong(T).toUpperCase();
        while (steps.length() < 16) {
            steps = "0" + steps;
        }

        String sOtp = null;
        try {
            sOtp = TotpImpl.getInstance().generateTOTP(seed, steps, size);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return sOtp;
    }
    private final static char[] HEX = {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private static String fromLong(long value) {
        char[] hexs;
        int i;
        int c;

        hexs = new char[16];
        for (i = 0; i < 16; i++) {
            c = (int) (value & 0xf);
            hexs[16 - i - 1] = HEX[c];
            value = value >> 4;
        }
        return new String(hexs);
    }
}
