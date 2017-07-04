package sbe.reader;

import org.junit.Test;
import sbe.builder.AdminBuilder;
import sbe.msg.AdminTypeEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 17/04/16.
 */
public class AdminReaderTest {

    @Test
    public void testRead() throws Exception {
        AdminReader adminReader = new AdminReader();
        DirectBuffer buffer = build();

        adminReader.read(buffer);
        assertEquals(AdminTypeEnum.SimulationComplete,adminReader.getAdminTypeEnum());
        assertEquals(1,adminReader.getCompId());
    }

    private DirectBuffer build(){
        AdminBuilder adminBuilder = new AdminBuilder();
        adminBuilder.compID(1);
        adminBuilder.adminMessage(AdminTypeEnum.SimulationComplete);

        return adminBuilder.build();

    }

    @Test
    public void testStartLOB(){
        AdminBuilder adminBuilder = new AdminBuilder();
        adminBuilder.compID(1);
        adminBuilder.adminMessage(AdminTypeEnum.StartLOB);

        AdminReader adminReader = new AdminReader();
        DirectBuffer buffer = adminBuilder.build();

        adminReader.read(buffer);
        assertEquals(AdminTypeEnum.StartLOB,adminReader.getAdminTypeEnum());
        assertEquals(1,adminReader.getCompId());

    }
    @Test
    public void testEndLOB(){
        AdminBuilder adminBuilder = new AdminBuilder();
        adminBuilder.compID(1);
        adminBuilder.adminMessage(AdminTypeEnum.EndLOB);

        AdminReader adminReader = new AdminReader();
        DirectBuffer buffer = adminBuilder.build();

        adminReader.read(buffer);
        assertEquals(AdminTypeEnum.EndLOB,adminReader.getAdminTypeEnum());
        assertEquals(1,adminReader.getCompId());

    }

}