import java.util.Collections;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        // По переданному набору идентификаторов OBJECTID,
        // вывести описание адресов (тип + название) на переданную дату

        List<ObjectElement> firstTaskResult = FirstTask.getInfoByDate("2010-01-01", "1422396, 1450759, 1449192, 1451562");
        printFirstTaskResult(firstTaskResult);

        // Вывести актуальные полные адреса (строку с цепочкой адресов по иерархии),
        // в которых встречается тип адреса = «проезд»

        List<List<ObjectElement>> secondTaskResult = SecondTask.getInfoFullAddress();
        printSecondTaskResult(secondTaskResult);
    }

    private static void printFirstTaskResult(List<ObjectElement> result) {
        for (ObjectElement obj : result) {
            String objStr = String.format("%s: %s %s", obj.getObjectId(), obj.getTypeName(), obj.getName());
            System.out.println(objStr);
        }
    }

    private static void printSecondTaskResult(List<List<ObjectElement>> result) {
        for (List<ObjectElement> fullAddress: result ) {
            Collections.reverse(fullAddress);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < fullAddress.size(); i++) {
                sb.append(fullAddress.get(i).getTypeName()).append( " " ).append(fullAddress.get(i).getName());
                if (i < fullAddress.size() - 1) {
                    sb.append(", ");
                }
            }
            System.out.println(sb);
        }
    }
}
