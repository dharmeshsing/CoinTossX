package sbe.builder;

import org.junit.Assert;
import org.junit.Test;
import uk.co.real_logic.agrona.DirectBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class LogonBuilderTest{

    @Test
    public void testLogonBuilder(){
        LogonBuilder logonBuilder = new LogonBuilder();
        DirectBuffer buffer = logonBuilder.compID(1)
                .password("password12".getBytes())
                .newPassword("1234567890".getBytes())
                .build();

        Assert.assertNotNull(buffer);
    }
}