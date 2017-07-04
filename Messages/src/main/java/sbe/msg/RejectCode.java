/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum RejectCode
{
    LoginSuccessful(0),
    InvalidCompIDOrPassword(1),
    NotLoggedIntoRealTimeChannel(100),
    ConcurrentLoginLimitReached(9903),
    NULL_VAL(-2147483648);

    private final int value;

    RejectCode(final int value)
    {
        this.value = value;
    }

    public int value()
    {
        return value;
    }

    public static RejectCode get(final int value)
    {
        switch (value)
        {
            case 0: return LoginSuccessful;
            case 1: return InvalidCompIDOrPassword;
            case 100: return NotLoggedIntoRealTimeChannel;
            case 9903: return ConcurrentLoginLimitReached;
        }

        if (-2147483648 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
