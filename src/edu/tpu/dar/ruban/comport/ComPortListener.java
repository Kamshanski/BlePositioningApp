package edu.tpu.dar.ruban.comport;

public interface ComPortListener {
    void onTerminal(String msg);
    void onPayload(long timeStart, long timeEnd, long arrivalTime, String msg);
    void onError(String msg);
    void onReady(String msg);
    void onRestarting(String msg);
    void onTargetsSet(String sizeBuf, String capacityBuff, String addrBuf);
    void onSlavesNumber(String msg);
}
