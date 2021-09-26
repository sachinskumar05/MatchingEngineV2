package ds;

import com.sk.matchingenginev2.ds.BPlusTree;
import com.sk.matchingenginev2.leafNode.OrderEntry;
import com.sk.matchingenginev2.leafNode.OrderList;
import com.sk.matchingenginev2.leafNode.OrderListImpl;
import leafNode.TestOrderEntryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Testable
public class BPlusTreeTest {

    @Test
    void testIterator(){
        BPlusTree<Integer,String> tree = new BPlusTree(10);
        tree.put(1,"One");
        tree.put(2,"Two");
        tree.put(3,"Three");
        tree.put(4,"Four");
        tree.put(5, "Five");

        BPlusTree.BPlusTreeIterator iterator = (BPlusTree.BPlusTreeIterator)tree.iterator();

        while(iterator.hasNext()){
            Map.Entry<Integer, String> entry = iterator.next();
            System.out.println(entry.getKey());
            if(entry.getKey() == 3){
                tree.remove(3);
                iterator.remove();
            }
        }
    }

    @Test
    void testAddMillionOrders() throws Exception{
        BPlusTree<Long, OrderList> tree = new BPlusTree(10);

        OrderEntry orderEntry = TestOrderEntryFactory.getOrderEntryInstance();

        for(int i=0; i<1_000_000; i++){
            OrderList orderList = tree.get(0L);
            if(orderList == null){
                orderList = new OrderListImpl();
                tree.put(0L,orderList);
            }
            TestOrderEntryFactory.createOrderEntry(orderEntry, System.nanoTime());
            orderList.add(orderEntry);
        }

        Assertions.assertEquals(1000000000, tree.get(0L).total());

    }

    @Test
    void testAddMillionItems() throws Exception{
        BPlusTree<Long, List<Long>> tree = new BPlusTree(10);

        for(int i=0; i<1_000_000; i++){
            List<Long> values = tree.get(0L);
            if(values == null){
                values = new ArrayList<>();
                tree.put(0L,values);
            }
            values.add((long) i);
        }
    }

}