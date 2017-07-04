package sbe.reader;

import org.junit.Test;
import sbe.builder.BuilderUtil;
import sbe.builder.NewsMessageBuilder;
import sbe.msg.NewsDecoder;
import sbe.msg.UrgencyEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 1/11/15.
 */
public class NewsOrderReaderTest {

    @Test
    public void testRead() throws Exception {
        NewsMessageReader newsMessageReader = new NewsMessageReader();
        DirectBuffer buffer = build();

        StringBuilder sb = newsMessageReader.read(buffer);
        assertEquals("PartitionId=1SequenceNumber=1OrigTime=10:30   Urgency=HighPriortyHeadLine=test                                                                                                Text=TextTest                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      Instruments=SAB",sb.toString().trim());

    }

    private DirectBuffer build(){
        NewsMessageBuilder newsMessageBuilder = new NewsMessageBuilder();
        String origTime = BuilderUtil.fill("10:30", NewsDecoder.origTimeLength());
        String headLine = BuilderUtil.fill("test", NewsDecoder.headLineLength());
        String text = BuilderUtil.fill("TextTest", NewsDecoder.textLength());
        String instruments = BuilderUtil.fill("SAB", NewsDecoder.instrumentsLength());

        return newsMessageBuilder.compID(1)
                .partitionId((short)1)
                .sequenceNumber(1)
                .urgency(UrgencyEnum.HighPriorty)
                .origTime(origTime.getBytes())
                .headLine(headLine.getBytes())
                .text(text.getBytes())
                .instruments(instruments.getBytes())
                .build();
    }

}