/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.agrona.MutableDirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.sbe.codec.java.GroupOrder;

@GroupOrder({ExecutionReportEncoder.FillsGroupEncoder.class})
@SuppressWarnings("all")
public class ExecutionReportEncoder
{
    public static final int BLOCK_LENGTH = 103;
    public static final int TEMPLATE_ID = 13;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final ExecutionReportEncoder parentMessage = this;
    private MutableDirectBuffer buffer;
    protected int offset;
    protected int limit;
    protected int actingBlockLength;
    protected int actingVersion;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "";
    }

    public int offset()
    {
        return offset;
    }

    public ExecutionReportEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);
        return this;
    }

    public int encodedLength()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        buffer.checkLimit(limit);
        this.limit = limit;
    }

    public static short partitionIdNullValue()
    {
        return (short)255;
    }

    public static short partitionIdMinValue()
    {
        return (short)0;
    }

    public static short partitionIdMaxValue()
    {
        return (short)254;
    }
    public ExecutionReportEncoder partitionId(final short value)
    {
        CodecUtil.uint8Put(buffer, offset + 0, value);
        return this;
    }

    public static int sequenceNumberNullValue()
    {
        return -2147483648;
    }

    public static int sequenceNumberMinValue()
    {
        return -2147483647;
    }

    public static int sequenceNumberMaxValue()
    {
        return 2147483647;
    }
    public ExecutionReportEncoder sequenceNumber(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 1, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static byte executionIDNullValue()
    {
        return (byte)0;
    }

    public static byte executionIDMinValue()
    {
        return (byte)32;
    }

    public static byte executionIDMaxValue()
    {
        return (byte)126;
    }

    public static int executionIDLength()
    {
        return 21;
    }

    public void executionID(final int index, final byte value)
    {
        if (index < 0 || index >= 21)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 5 + (index * 1), value);
    }

    public static String executionIDCharacterEncoding()
    {
        return "UTF-8";
    }
    public ExecutionReportEncoder putExecutionID(final byte[] src, final int srcOffset)
    {
        final int length = 21;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 5, src, srcOffset, length);
        return this;
    }

    public static byte clientOrderIdNullValue()
    {
        return (byte)0;
    }

    public static byte clientOrderIdMinValue()
    {
        return (byte)32;
    }

    public static byte clientOrderIdMaxValue()
    {
        return (byte)126;
    }

    public static int clientOrderIdLength()
    {
        return 20;
    }

    public void clientOrderId(final int index, final byte value)
    {
        if (index < 0 || index >= 20)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 26 + (index * 1), value);
    }

    public static String clientOrderIdCharacterEncoding()
    {
        return "UTF-8";
    }
    public ExecutionReportEncoder putClientOrderId(final byte[] src, final int srcOffset)
    {
        final int length = 20;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 26, src, srcOffset, length);
        return this;
    }

    public static int orderIdNullValue()
    {
        return -2147483648;
    }

    public static int orderIdMinValue()
    {
        return -2147483647;
    }

    public static int orderIdMaxValue()
    {
        return 2147483647;
    }
    public ExecutionReportEncoder orderId(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 46, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
    public ExecutionReportEncoder executionType(final ExecutionTypeEnum value)
    {
        CodecUtil.charPut(buffer, offset + 50, value.value());
        return this;
    }
    public ExecutionReportEncoder orderStatus(final OrderStatusEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 51, value.value());
        return this;
    }
    public ExecutionReportEncoder rejectCode(final RejectCode value)
    {
        CodecUtil.int32Put(buffer, offset + 52, value.value(), java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int leavesQuantityNullValue()
    {
        return -2147483648;
    }

    public static int leavesQuantityMinValue()
    {
        return -2147483647;
    }

    public static int leavesQuantityMaxValue()
    {
        return 2147483647;
    }
    public ExecutionReportEncoder leavesQuantity(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 56, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
    public ExecutionReportEncoder container(final ContainerEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 60, value.value());
        return this;
    }

    public static int securityIdNullValue()
    {
        return -2147483648;
    }

    public static int securityIdMinValue()
    {
        return -2147483647;
    }

    public static int securityIdMaxValue()
    {
        return 2147483647;
    }
    public ExecutionReportEncoder securityId(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 61, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
    public ExecutionReportEncoder side(final SideEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 65, value.value());
        return this;
    }

    public static byte traderMnemonicNullValue()
    {
        return (byte)0;
    }

    public static byte traderMnemonicMinValue()
    {
        return (byte)32;
    }

    public static byte traderMnemonicMaxValue()
    {
        return (byte)126;
    }

    public static int traderMnemonicLength()
    {
        return 17;
    }

    public void traderMnemonic(final int index, final byte value)
    {
        if (index < 0 || index >= 17)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 66 + (index * 1), value);
    }

    public static String traderMnemonicCharacterEncoding()
    {
        return "UTF-8";
    }
    public ExecutionReportEncoder putTraderMnemonic(final byte[] src, final int srcOffset)
    {
        final int length = 17;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 66, src, srcOffset, length);
        return this;
    }

    public static byte accountNullValue()
    {
        return (byte)0;
    }

    public static byte accountMinValue()
    {
        return (byte)32;
    }

    public static byte accountMaxValue()
    {
        return (byte)126;
    }

    public static int accountLength()
    {
        return 10;
    }

    public void account(final int index, final byte value)
    {
        if (index < 0 || index >= 10)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 83 + (index * 1), value);
    }

    public static String accountCharacterEncoding()
    {
        return "UTF-8";
    }
    public ExecutionReportEncoder putAccount(final byte[] src, final int srcOffset)
    {
        final int length = 10;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 83, src, srcOffset, length);
        return this;
    }
    public ExecutionReportEncoder isMarketOpsRequest(final IsMarketOpsRequestEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 93, value.value());
        return this;
    }

    public static long transactTimeNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long transactTimeMinValue()
    {
        return 0x0L;
    }

    public static long transactTimeMaxValue()
    {
        return 0xfffffffffffffffeL;
    }
    public ExecutionReportEncoder transactTime(final long value)
    {
        CodecUtil.uint64Put(buffer, offset + 94, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
    public ExecutionReportEncoder orderBook(final OrderBookEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 102, value.value());
        return this;
    }

    private final FillsGroupEncoder fillsGroup = new FillsGroupEncoder();

    public static long fillsGroupId()
    {
        return 20;
    }

    public FillsGroupEncoder fillsGroupCount(final int count)
    {
        fillsGroup.wrap(parentMessage, buffer, count);
        return fillsGroup;
    }

    public static class FillsGroupEncoder
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncodingEncoder dimensions = new GroupSizeEncodingEncoder();
        private ExecutionReportEncoder parentMessage;
        private MutableDirectBuffer buffer;
        private int blockLength;
        private int actingVersion;
        private int count;
        private int index;
        private int offset;

        public void wrap(final ExecutionReportEncoder parentMessage, final MutableDirectBuffer buffer, final int count)
        {
            this.parentMessage = parentMessage;
            this.buffer = buffer;
            actingVersion = SCHEMA_VERSION;
            dimensions.wrap(buffer, parentMessage.limit());
            dimensions.blockLength((int)12);
            dimensions.numInGroup((short)count);
            index = -1;
            this.count = count;
            blockLength = 12;
            parentMessage.limit(parentMessage.limit() + HEADER_SIZE);
        }

        public static int sbeHeaderSize()
        {
            return HEADER_SIZE;
        }

        public static int sbeBlockLength()
        {
            return 12;
        }

        public FillsGroupEncoder next()
        {
            if (index + 1 >= count)
            {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + blockLength);
            ++index;

            return this;
        }

        private final PriceEncoder fillPrice = new PriceEncoder();

        public PriceEncoder fillPrice()
        {
            fillPrice.wrap(buffer, offset + 0);
            return fillPrice;
        }

        public static int fillQtyNullValue()
        {
            return -2147483648;
        }

        public static int fillQtyMinValue()
        {
            return -2147483647;
        }

        public static int fillQtyMaxValue()
        {
            return 2147483647;
        }
        public FillsGroupEncoder fillQty(final int value)
        {
            CodecUtil.int32Put(buffer, offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
            return this;
        }
    }
}
