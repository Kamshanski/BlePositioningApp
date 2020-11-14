package edu.tpu.dar.ruban.logic;

public class MacAddress {
    public String macString;

    public MacAddress(String macString) {
        this.macString = macString.replaceAll(":", "");
    }

    byte[] toByteArray() {
        byte[] b = new byte[6];

        for (int j = 0; j < 6; j++) {
            String hex = macString.substring(j*3, (j*3)+1);
            b[j] = Byte.parseByte(hex, 16);
        }

        return b;
    }
}
