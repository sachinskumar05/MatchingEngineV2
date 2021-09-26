package leafNode;

import com.sk.matchingenginev2.common.OrderType;
import com.sk.matchingenginev2.leafNode.OrderEntry;
import sbe.msg.OrderStatusEnum;
import com.sk.matchingenginev2.unsafe.UnsafeUtil;

import java.util.Calendar;
import java.util.Date;


public class TestOrderEntryFactory {
    private static Date currentDate = Calendar.getInstance().getTime();
    private OrderEntry orderEntry;

    public TestOrderEntryFactory(){
        long address = UnsafeUtil.getUnsafe().allocateMemory(OrderEntry.getObjectSize());
        orderEntry = new OrderEntry();
        orderEntry.setObjectOffset(address);
        orderEntry.init();
    }

    public static OrderEntry createOrderEntry(String time){
        long address = UnsafeUtil.getUnsafe().allocateMemory(OrderEntry.getObjectSize());
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setObjectOffset(address);
        orderEntry.init();

        orderEntry.setSide((byte) 1);
        orderEntry.setOrderId(1);
        orderEntry.setType(OrderType.LIMIT.getOrderType());

        String[] submittedTime = time.split(":");
        currentDate.setHours(Integer.parseInt(submittedTime[0]));
        currentDate.setMinutes(Integer.parseInt(submittedTime[1]));
        orderEntry.setSubmittedTime(currentDate.getTime());

        orderEntry.setQuantity(1000);
        orderEntry.setMinExecutionSize(0);
        orderEntry.setPrice(100);
        orderEntry.setStopPrice(0);
        orderEntry.setTimeInForce((byte) 0);
        orderEntry.setDisplayQuantity(1000);
        orderEntry.setTrader(1);
        orderEntry.setExpireTime(currentDate.getTime());
        orderEntry.setOrderStatus((byte)OrderStatusEnum.New.value());

        return orderEntry;
    }

    public static OrderEntry createHOOrderEntry(String time){
        long address = UnsafeUtil.getUnsafe().allocateMemory(OrderEntry.getObjectSize());
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setObjectOffset(address);
        orderEntry.init();

        orderEntry.setSide((byte)1);
        orderEntry.setOrderId(1);
        orderEntry.setType(OrderType.HIDDEN_LIMIT.getOrderType());

        String[] submittedTime = time.split(":");
        currentDate.setHours(Integer.parseInt(submittedTime[0]));
        currentDate.setMinutes(Integer.parseInt(submittedTime[1]));
        orderEntry.setSubmittedTime(currentDate.getTime());

        orderEntry.setQuantity(1000);
        orderEntry.setMinExecutionSize(1000);
        orderEntry.setPrice(100);
        orderEntry.setStopPrice(0);
        orderEntry.setTimeInForce((byte) 0);
        orderEntry.setDisplayQuantity(1000);
        orderEntry.setTrader(1);
        orderEntry.setExpireTime(currentDate.getTime());
        orderEntry.setOrderStatus((byte) OrderStatusEnum.New.value());

        return orderEntry;
    }

    public static OrderEntry createOrderEntry(OrderEntry orderEntry,long time){
        orderEntry.init();

        orderEntry.setSide((byte) 1);
        orderEntry.setOrderId(1);
        orderEntry.setType(OrderType.LIMIT.getOrderType());

        orderEntry.setSubmittedTime(time);

        orderEntry.setQuantity(1000);
        orderEntry.setMinExecutionSize(0);
        orderEntry.setPrice(100);
        orderEntry.setStopPrice(0);
        orderEntry.setTimeInForce((byte) 0);
        orderEntry.setDisplayQuantity(1000);
        orderEntry.setTrader(1);
        orderEntry.setExpireTime(currentDate.getTime());
        orderEntry.setOrderStatus((byte)OrderStatusEnum.New.value());

        return orderEntry;
    }

    public static OrderEntry createOrderEntry(long time){
        long address = UnsafeUtil.getUnsafe().allocateMemory(OrderEntry.getObjectSize());
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setObjectOffset(address);
        orderEntry.init();

        orderEntry.setSide((byte) 1);
        orderEntry.setOrderId(1);
        orderEntry.setType(OrderType.LIMIT.getOrderType());

        orderEntry.setSubmittedTime(time);

        orderEntry.setQuantity(1000);
        orderEntry.setMinExecutionSize(0);
        orderEntry.setPrice(100);
        orderEntry.setStopPrice(0);
        orderEntry.setTimeInForce((byte) 0);
        orderEntry.setDisplayQuantity(1000);
        orderEntry.setTrader(1);
        orderEntry.setExpireTime(currentDate.getTime());
        orderEntry.setOrderStatus((byte)OrderStatusEnum.New.value());

        return orderEntry;
    }

    public static OrderEntry getOrderEntryInstance(){
        long address = UnsafeUtil.getUnsafe().allocateMemory(OrderEntry.getObjectSize());
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setObjectOffset(address);
        orderEntry.init();

        return orderEntry;
    }
}
