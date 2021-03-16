package sbe.builder;

import com.carrotsearch.hppc.ObjectArrayList;
import sbe.msg.LOBEncoder;
import sbe.msg.MessageHeaderEncoder;
import sbe.msg.SideEnum;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class LOBBuilder {
    private int bufferIndex;
    private LOBEncoder lob;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private int securityId;
    private ObjectArrayList<Order> orders;

    public static int BUFFER_SIZE = 17000;

    public LOBBuilder(){
        lob = new LOBEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
        orders = new ObjectArrayList<>();
    }

    public void reset(){
        orders.clear();
        orders.trimToSize();
        compID = 0;
        securityId = 0;
        bufferIndex = 0;
    }

    public LOBBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public LOBBuilder securityId(int value){
        this.securityId = value;
        return this;
    }

    public LOBBuilder addOrder(int orderId, int orderQuantity, SideEnum side, long price){
        orders.add(new Order(orderId,orderQuantity,side,price));
        return this;
    }


    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(lob.sbeBlockLength())
                .templateId(lob.sbeTemplateId())
                .schemaId(lob.sbeSchemaId())
                .version(lob.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        int size = orders.size();
        LOBEncoder.OrdersEncoder ordersEncoder = lob.wrap(encodeBuffer, bufferIndex)
                .securityId(securityId)
                .ordersCount(size);

        for(int i=0; i<size; i++){
            Order order = orders.get(i);
            if(order != null) {
                LOBEncoder.OrdersEncoder oe = ordersEncoder.next();

                oe.orderId(order.getOrderId());
                oe.orderQuantity(order.getOrderQuantity());
                oe.side(order.getSide());
                oe.price().mantissa(order.getPrice());
            }
        }

        return encodeBuffer;
    }

    public static class Order implements Serializable{
        private int orderId;
        private int orderQuantity;
        private SideEnum side;
        private long price;

        public Order(){}

        public Order(int orderId, int orderQuantity, SideEnum side, long price){
            this.orderId = orderId;
            this.orderQuantity = orderQuantity;
            this.side = side;
            this.price = price;
        }


        public int getOrderId() {
            return orderId;
        }

        public int getOrderQuantity() {
            return orderQuantity;
        }

        public SideEnum getSide() {
            return side;
        }

        public long getPrice() {
            return price;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public void setOrderQuantity(int orderQuantity) {
            this.orderQuantity = orderQuantity;
        }

        public void setSide(SideEnum side) {
            this.side = side;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "orderId=" + orderId +
                    ", orderQuantity=" + orderQuantity +
                    ", side=" + side +
                    ", price=" + price +
                    '}';
        }
    }

}
