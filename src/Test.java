import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.SocketHandler;

public class Test {

    public static void main(String[] args) {
        Hashtable<String, String> testTable = new Hashtable<>();
        testTable.put("apple","appleSocket");
        testTable.put("Microsoft","MicrosoftSocket");
        testTable.put("Sony","SonySocket");
        StringBuilder userList = new StringBuilder("9");
        Iterator<String> users = testTable.keySet().iterator();
        while (users.hasNext()){
            userList.append(users.next());
            userList.append("@");
        }
        userList.append("*");
        System.out.println(userList);

    }
}
