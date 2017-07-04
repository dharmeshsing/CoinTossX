package sbe.reader;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.NewsDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class NewsMessageReader {
    private StringBuilder sb;
    private int bufferIndex;
    private NewsDecoder news;
    private MessageHeaderDecoder messageHeader;
    private byte[] origTime;
    private byte[] headLine;
    private byte[] text;
    private byte[] instruments;

    public NewsMessageReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        news = new NewsDecoder();
        messageHeader = new MessageHeaderDecoder();

        origTime = new byte[NewsDecoder.origTimeLength()];
        headLine = new byte[NewsDecoder.headLineLength()];
        text = new byte[NewsDecoder.textLength()];
        instruments = new byte[NewsDecoder.instrumentsLength()];
    }


    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        news.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("PartitionId=" + news.partitionId());
        sb.append("SequenceNumber=" + news.sequenceNumber());
        sb.append("OrigTime=" + new String(origTime, 0, news.getOrigTime(origTime, 0), NewsDecoder.origTimeCharacterEncoding()));
        sb.append("Urgency=" + news.urgency());
        sb.append("HeadLine=" + new String(headLine, 0, news.getHeadLine(headLine, 0), NewsDecoder.headLineCharacterEncoding()));
        sb.append("Text=" + new String(text, 0, news.getText(text, 0), NewsDecoder.textCharacterEncoding()));
        sb.append("Instruments=" + new String(instruments, 0, news.getInstruments(instruments, 0), NewsDecoder.instrumentsCharacterEncoding()));

        return sb;
    }
}
