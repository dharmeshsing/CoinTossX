/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum MassCancelRequestTypeEnum
{
    AllFirmOrdersForInstr((short)3),
    AllFirmOrdersForSeg((short)4),
    AllOrdersForClient((short)7),
    AllOrdersForFirm((short)8),
    ClientFirmOrdersForInstr((short)9),
    ClientFirmOrdersForSeg((short)15),
    NULL_VAL((short)255);

    private final short value;

    MassCancelRequestTypeEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static MassCancelRequestTypeEnum get(final short value)
    {
        switch (value)
        {
            case 3: return AllFirmOrdersForInstr;
            case 4: return AllFirmOrdersForSeg;
            case 7: return AllOrdersForClient;
            case 8: return AllOrdersForFirm;
            case 9: return ClientFirmOrdersForInstr;
            case 15: return ClientFirmOrdersForSeg;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
