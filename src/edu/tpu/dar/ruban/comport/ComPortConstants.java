package edu.tpu.dar.ruban.comport;

public class ComPortConstants {
    public static final int PAYLOAD_LENGTH = 858;   // "Payload:xxXXxxXXxxXX-SSID..." => 8 + 17*50
    public static final char[] PAYLOAD = "Payload:".toCharArray();
    public static final char[] TIME_START = "TS:".toCharArray();
    public static final char[] TIME_END = "TE:".toCharArray();
    public static final char[] RELOADING = "ets".toCharArray();
    public static final char[] ERROR = "Guru".toCharArray();
    public static final char[] READY = "Device started".toCharArray();
    public static final char[] TARGET_SET_START = "Targets{".toCharArray();
    public static final char[] TARGET_SET_END = "}Targets".toCharArray();
    public static final char[] SIZE = "Size".toCharArray();
    public static final char[] CAPACITY = "Capacity".toCharArray();
    public static final char[] DEVICE_MAC = "DeviceMAC".toCharArray();
    public static final char[] SLAVES = "Slaves connected: ".toCharArray();
}
