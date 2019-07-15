
import java.util.Arrays;
import java.util.List;
import com.google.common.hash.Hashing;

public class Test {

    public static int choose(String ipaddress, int bucketNum){
        int model = Hashing.consistentHash(ipaddress.hashCode(),bucketNum);
        return model;
    }

    public static void main(String[] args) {
        String[] serverArr = {"123.124.12.2","51.312.123.2","123.123.123.5","5.21.123.124","15.123.12.3","231.123.5412.142","123.123.125.2"};
        List<String> serverList = Arrays.asList(serverArr);
        System.out.println("------ BUCKET NUMBER: 2 ------");
        serverList.forEach(server ->{
            System.out.println(choose(server,2));
        });
        System.out.println("------ BUCKET NUMBER: 3 ------");
        serverList.forEach(server ->{
            System.out.println(choose(server,3));
        });
//        System.out.println("------ BUCKET NUMBER: 3 AFTER DELETE ------");
//        serverList.forEach(server ->{
//            System.out.println(choose(server,3));
//        });
    }
}
