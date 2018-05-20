package iota.client.network;


import iota.common.IoTaUtil;

import java.util.List;

public class DataCapsule {

    private long sourceId;
    private long destinationId;
    private short funcId;
    private List<Byte> data;


    public DataCapsule(long sourceId, long destinationId, short funcId, List<Byte> data) {

        this.destinationId = destinationId;
        this.sourceId = sourceId;
        this.funcId = funcId;
        this.data = data;

    }

    public long getSourceId() {
        return sourceId;
    }

    public long getDestinationId() {
        return destinationId;
    }


    public List<Byte> getData() {
        return data;
    }

    public short getFuncId() {
        return funcId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total size: " + (this.getData().size() + 22));
        sb.append("Source: " + IoTaUtil.long2Bytes(this.getSourceId()));
        return sb.toString();
    }
}
