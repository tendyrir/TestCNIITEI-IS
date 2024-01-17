import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Utils {

    final static String asAddrObjXml = "src/main/resources/AS_ADDR_OBJ.XML";
    final static String asAdmHierarchyXml = "src/main/resources/AS_ADM_HIERARCHY.XML";

    public static ObjectElement makeObject(StartElement startElement) {

        String objectIdAttrValue = startElement.getAttributeByName(new QName("OBJECTID")).getValue();
        String nameAttrValue = startElement.getAttributeByName(new QName("NAME")).getValue();
        String typeNameAttrValue = startElement.getAttributeByName(new QName("TYPENAME")).getValue();
        String startdateAttrValue = startElement.getAttributeByName(new QName("STARTDATE")).getValue();
        String enddateAttrValue = startElement.getAttributeByName(new QName("ENDDATE")).getValue();
        String isActualAttrValue = startElement.getAttributeByName(new QName("ISACTUAL")).getValue();
        String isActiveAttrValue = startElement.getAttributeByName(new QName("ISACTUAL")).getValue();
        Integer isActive = Integer.valueOf(isActiveAttrValue);
        Integer isActual = Integer.valueOf(isActualAttrValue);

        ObjectElement objectElement = new ObjectElement();

        objectElement.setObjectId(Integer.valueOf(objectIdAttrValue));
        objectElement.setName(nameAttrValue);
        objectElement.setTypeName(typeNameAttrValue);
        objectElement.setStartDate(parseDateFromString(startdateAttrValue));
        objectElement.setEndDate(parseDateFromString(enddateAttrValue));
        objectElement.setIsActual(isActive);
        objectElement.setIsActive(isActual);

        return objectElement;
    }

    public static List<Integer> getIdListFromString(String inputIdList) {
        return Arrays.stream(inputIdList.replace(" ", "").split(","))
                .map(Integer::parseInt)
                .toList();
    }

    public static Date parseDateFromString(String inputDate) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
