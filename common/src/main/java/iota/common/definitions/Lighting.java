package iota.common.definitions;

import iota.common.db.DbCol;

import java.util.ArrayList;
import java.util.List;

class Lighting implements IFuncDef {
    private List<ByteState> stateItems;

    protected Lighting() {
        stateItems = new ArrayList<>();
        //these values are from the c enum "LedDefines.h"
        //in the future having these generated by either gradle or some other
        //class would be nice
        stateItems.add(new ByteState("Pid"));
        stateItems.add(new ByteState("fps"));
        stateItems.add(new ByteState("anim hz"));
        stateItems.add(new ByteState("brightness"));
        stateItems.add(new ByteState("hue"));
        stateItems.add(new ByteState("sat"));
        stateItems.add(new ByteState("val"));
        stateItems.add(new ByteState("delta hue"));
        stateItems.add(new ByteState("delta sat"));
        stateItems.add(new ByteState("delta val"));
    }

    @Override
    public List<DbCol> getCols() {
        return null;
    }

    @Override
    public String getTableName() {
        return "Lighting";
    }

    @Override
    public short getFuncId() {
        return 2;
    }

    @Override
    public int submitMessage(byte[] message) {

        return message.length;
    }

    @Override
    public int updateStateBuffer(byte[] recievedState) {
        stateItems.clear();
        for (int i = 0; i < recievedState.length; i++) {
            ByteState bs = new ByteState("State " + i);
            bs.setVal(recievedState[i]);
            stateItems.add(bs);


        }

        return 0;
    }

    @Override
    public List<IStateItem> getStateItems() {
        return new ArrayList<>(stateItems);
    }

    ;

    private class ByteState implements IStateItem {
        private String name;
        private byte val;

        public ByteState(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getVal() {
            return String.format("%02X", val);
        }

        private void setVal(byte b) {
            val = b;
        }
    }

}