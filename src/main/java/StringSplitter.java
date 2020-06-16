import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StringSplitter {

    public static List<String> split(String text) {
        List<String> result = new ArrayList<>();
        Scanner sc = new Scanner(text);
        StringBuilder sb = new StringBuilder();
        int count = 0;

        while (sc.hasNext()) {
            sb.append(sc.nextLine() + "\n");
            count++;

            if (count % 60 == 0) {
                result.add(sb.toString().trim());
                sb.setLength(0);
            }
        }

        result.add(sb.toString().trim());

        return result;
    }
}
