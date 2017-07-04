package common;

/**
 * Created by dharmeshsing on 16/06/15.
 */
public enum TimeInForce {
    DAY((byte)0),
    GTC((byte)1),
    IOC((byte)3),
    FOK((byte)4),
    OPG((byte)5),
    GTD((byte)6),
    GTT((byte)8),
    GFA((byte)9),
    GFX((byte)51),
    ATC((byte)10),
    CPX((byte)12);

    private byte value;
    TimeInForce(byte value){this.value = value;}
    public byte getValue(){return this.value;}

    public static TimeInForce getTimeInForce(byte value){
        switch (value) {
            case 0: return DAY;
            case 1: return GTC;
            case 3: return IOC;
            case 4: return FOK;
            case 5: return OPG;
            case 6: return GTD;
            case 8: return GTT;
            case 9: return GFA;
            case 51: return GFX;
            case 10: return ATC;
            case 12: return CPX;
        }

        return null;
    }

}
