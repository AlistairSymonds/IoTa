package iota.common.definitions;

public class IFuncFactory {
    public static IFuncDef getInstanceById(short funcId){


        if(funcId == 1){
            return new Heartbeat();
        }

        return new UnknownFunction(funcId);
    }
}
