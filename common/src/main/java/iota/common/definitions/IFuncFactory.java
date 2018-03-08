package iota.common.definitions;

public class IFuncFactory {
    public static IFuncDef getInstanceById(short funcId){


        if(funcId == 1){
            return new Heartbeat();
        } else if (funcId == 2) {
            return new Lighting();
        }

        return new UnknownFunction(funcId);
    }
}
