package edu.tpu.dar.ruban;

public interface ComPortListener {
    void onPayload(long timeStart, long timeEnd, String msg);
    void onError(String msg);
    void onReady(String msg);
    void onRestarting(String msg);
    void onTargetsSet(StringBuilder sizeBuf, StringBuilder capacityBuff, StringBuilder addrBuf);
    void onSlavesNumber(String msg);
}
