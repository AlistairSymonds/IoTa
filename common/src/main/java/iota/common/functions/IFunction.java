package iota.common.functions;


import iota.common.definitions.IStateItem;

import java.util.List;


public interface IFunction {
    String getDisplayName();

    short getFuncId();

    int handleReceivedData(List<Byte> receivedData);

    List<IStateItem> getStateItems();


}
