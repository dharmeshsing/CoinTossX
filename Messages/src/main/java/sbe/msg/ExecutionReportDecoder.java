/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.sbe.codec.java.GroupOrder;
import uk.co.real_logic.agrona.DirectBuffer;

@GroupOrder({ExecutionReportDecoder.FillsGroupDecoder.class})
@SuppressWarnings("all")
public class ExecutionReportDecoder
{
    public static final int BLOCK_LENGTH = 103;
    public static final int TEMPLATE_ID = 13;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final ExecutionReportDecoder parentMessage = this;
    private DirectBuffer buffer;
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

    public ExecutionReportDecoder wrap(
        final DirectBuffer buffer, final int offset, final int actingBlockLength, final int actingVersion)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

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

    public static int partitionIdId()
    {
        return 1;
    }

    public static String partitionIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public short partitionId()
    {
        return CodecUtil.uint8Get(buffer, offset + 0);
    }


    public static int sequenceNumberId()
    {
        return 2;
    }

    public static String sequenceNumberMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public int sequenceNumber()
    {
        return CodecUtil.int32Get(buffer, offset + 1, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int executionIDId()
    {
        return 3;
    }

    public static String executionIDMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public byte executionID(final int index)
    {
        if (index < 0 || index >= 21)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 5 + (index * 1));
    }


    public static String executionIDCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getExecutionID(final byte[] dst, final int dstOffset)
    {
        final int length = 21;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 5, dst, dstOffset, length);
        return length;
    }


    public static int clientOrderIdId()
    {
        return 4;
    }

    public static String clientOrderIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public byte clientOrderId(final int index)
    {
        if (index < 0 || index >= 20)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 26 + (index * 1));
    }


    public static String clientOrderIdCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getClientOrderId(final byte[] dst, final int dstOffset)
    {
        final int length = 20;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 26, dst, dstOffset, length);
        return length;
    }


    public static int orderIdId()
    {
        return 5;
    }

    public static String orderIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public int orderId()
    {
        return CodecUtil.int32Get(buffer, offset + 46, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int executionTypeId()
    {
        return 6;
    }

    public static String executionTypeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public ExecutionTypeEnum executionType()
    {
        return ExecutionTypeEnum.get(CodecUtil.charGet(buffer, offset + 50));
    }


    public static int orderStatusId()
    {
        return 7;
    }

    public static String orderStatusMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public OrderStatusEnum orderStatus()
    {
        return OrderStatusEnum.get(CodecUtil.uint8Get(buffer, offset + 51));
    }


    public static int rejectCodeId()
    {
        return 8;
    }

    public static String rejectCodeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public RejectCode rejectCode()
    {
        return RejectCode.get(CodecUtil.int32Get(buffer, offset + 52, java.nio.ByteOrder.LITTLE_ENDIAN));
    }


    public static int leavesQuantityId()
    {
        return 11;
    }

    public static String leavesQuantityMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public int leavesQuantity()
    {
        return CodecUtil.int32Get(buffer, offset + 56, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int containerId()
    {
        return 12;
    }

    public static String containerMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public ContainerEnum container()
    {
        return ContainerEnum.get(CodecUtil.uint8Get(buffer, offset + 60));
    }


    public static int securityIdId()
    {
        return 13;
    }

    public static String securityIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public int securityId()
    {
        return CodecUtil.int32Get(buffer, offset + 61, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int sideId()
    {
        return 14;
    }

    public static String sideMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public SideEnum side()
    {
        return SideEnum.get(CodecUtil.uint8Get(buffer, offset + 65));
    }


    public static int traderMnemonicId()
    {
        return 15;
    }

    public static String traderMnemonicMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public byte traderMnemonic(final int index)
    {
        if (index < 0 || index >= 17)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 66 + (index * 1));
    }


    public static String traderMnemonicCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getTraderMnemonic(final byte[] dst, final int dstOffset)
    {
        final int length = 17;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 66, dst, dstOffset, length);
        return length;
    }


    public static int accountId()
    {
        return 16;
    }

    public static String accountMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public byte account(final int index)
    {
        if (index < 0 || index >= 10)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 83 + (index * 1));
    }


    public static String accountCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getAccount(final byte[] dst, final int dstOffset)
    {
        final int length = 10;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 83, dst, dstOffset, length);
        return length;
    }


    public static int isMarketOpsRequestId()
    {
        return 17;
    }

    public static String isMarketOpsRequestMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public IsMarketOpsRequestEnum isMarketOpsRequest()
    {
        return IsMarketOpsRequestEnum.get(CodecUtil.uint8Get(buffer, offset + 93));
    }


    public static int transactTimeId()
    {
        return 18;
    }

    public static String transactTimeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public long transactTime()
    {
        return CodecUtil.uint64Get(buffer, offset + 94, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int orderBookId()
    {
        return 19;
    }

    public static String orderBookMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public OrderBookEnum orderBook()
    {
        return OrderBookEnum.get(CodecUtil.uint8Get(buffer, offset + 102));
    }


    private final FillsGroupDecoder fillsGroup = new FillsGroupDecoder();

    public static long fillsGroupDecoderId()
    {
        return 20;
    }

    public FillsGroupDecoder fillsGroup()
    {
        fillsGroup.wrap(parentMessage, buffer);
        return fillsGroup;
    }

    public static class FillsGroupDecoder
    implements Iterable<FillsGroupDecoder>, java.util.Iterator<FillsGroupDecoder>
    {
        private static final int HEADER_SIZE = 3;
        private final GroupSizeEncodingDecoder dimensions = new GroupSizeEncodingDecoder();
        private ExecutionReportDecoder parentMessage;
        private DirectBuffer buffer;
        private int blockLength;
        private int actingVersion;
        private int count;
        private int index;
        private int offset;

        public void wrap(
            final ExecutionReportDecoder parentMessage, final DirectBuffer buffer)
        {
            this.parentMessage = parentMessage;
            this.buffer = buffer;
            dimensions.wrap(buffer, parentMessage.limit());
            blockLength = dimensions.blockLength();
            count = dimensions.numInGroup();
            index = -1;
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

        public int actingBlockLength()
        {
            return blockLength;
        }

        public int count()
        {
            return count;
        }

        @Override
        public java.util.Iterator<FillsGroupDecoder> iterator()
        {
            return this;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext()
        {
            return (index + 1) < count;
        }

        @Override
        public FillsGroupDecoder next()
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

        public static int fillPriceId()
        {
            return 21;
        }

        public static String fillPriceMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
        }

        private final PriceDecoder fillPrice = new PriceDecoder();

        public PriceDecoder fillPrice()
        {
            fillPrice.wrap(buffer, offset + 0);
            return fillPrice;
        }

        public static int fillQtyId()
        {
            return 22;
        }

        public static String fillQtyMetaAttribute(final MetaAttribute metaAttribute)
        {
            switch (metaAttribute)
            {
                case EPOCH: return "unix";
                case TIME_UNIT: return "nanosecond";
                case SEMANTIC_TYPE: return "";
            }

            return "";
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

        public int fillQty()
        {
            return CodecUtil.int32Get(buffer, offset + 8, java.nio.ByteOrder.LITTLE_ENDIAN);
        }

    }
}
