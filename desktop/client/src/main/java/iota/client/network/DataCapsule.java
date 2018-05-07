package iota.client.network;


import iota.common.IoTaUtil;
import iota.common.functions.IFunction;

import java.util.List;

public class DataCapsule {

    private long sourceId;
    private long destinationId;
    private short funcId;
    private List<Byte> data;


    public DataCapsule(short funcId, List<Byte> data) {


        this.funcId = funcId;
        this.data = data;

    }

    public long getSourceId() {
        return IoTaUtil.getHostId();
    }


    public List<Byte> getData() {
        return data;
    }

    public short getFuncId() {
        return funcId;
    }

}
