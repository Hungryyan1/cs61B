package hashmap;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class TestForIterator {
    @Test
    public void test() {
        MyHashMap<String, Integer> b = new MyHashMap<>();
        for (int i = 0; i < 10; i++) {
            b.put("hi" + i, 1);
            //make sure put is working via containsKey and get
            assertTrue(null != b.get("hi" + i)
                    && b.containsKey("hi" + i));
        }
        for (String key : b) {
            System.out.println(key);
        }
    }
}
