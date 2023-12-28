import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<Integer, String> m = new HashMap<>();
        m.put(12, "432");
        m.put(11, "aaaa");
        SimpleHashMap<Integer, String> map1 = new SimpleHashMap<>(16);

        map1.put(16, "tr");
        map1.put(52, "dfv");
        map1.putAll(m);
        System.out.println(map1.values());
        System.out.println(map1.remove(16));
    }
}