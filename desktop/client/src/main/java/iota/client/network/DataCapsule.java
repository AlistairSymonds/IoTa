package iota.client.network;


import iota.common.Constants;
import iota.common.IoTaUtil;
import iota.common.definitions.IFuncDef;

import java.util.List;

public class DataCapsule {

    private long sourceId;
    private long destinationId;
    private IFuncDef func;
    private List<Byte> data;


    public DataCapsule(IFuncDef func, List<Byte> data) {


        this.func = func;
        this.data = data;

    }

    public long getSourceId() {
        return IoTaUtil.getHostId();
    }


    public List<Byte> getData() {
        return data;
    }

    public IFuncDef getFuncId() {
        return func;
    }

}
