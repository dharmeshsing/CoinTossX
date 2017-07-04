/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

public enum HaltReasonEnum
{
    MatchingPartitionSuspended(9998),
    SystemSuspended(9999),
    ReasonNotAvailable(0),
    SystemIssuesBeingExperienced(1),
    CompanyAnnouncementExpected(2),
    CompanyRequestedHalt(3),
    CompanyRequestedSuspension(4),
    JSEInitiatedHalt_Suspension(5),
    NULL_VAL(-2147483648);

    private final int value;

    HaltReasonEnum(final int value)
    {
        this.value = value;
    }

    public int value()
    {
        return value;
    }

    public static HaltReasonEnum get(final int value)
    {
        switch (value)
        {
            case 9998: return MatchingPartitionSuspended;
            case 9999: return SystemSuspended;
            case 0: return ReasonNotAvailable;
            case 1: return SystemIssuesBeingExperienced;
            case 2: return CompanyAnnouncementExpected;
            case 3: return CompanyRequestedHalt;
            case 4: return CompanyRequestedSuspension;
            case 5: return JSEInitiatedHalt_Suspension;
        }

        if (-2147483648 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
