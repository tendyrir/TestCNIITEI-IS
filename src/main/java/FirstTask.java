import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirstTask {


    public static List<ObjectElement> getInfoByDate(String inputDate, String inputIdList) {
        Date queryDate = Utils.parseDateFromString(inputDate);
        List<Integer> queryIdList = Utils.getIdListFromString(inputIdList);
        return parseXMLFistTask(queryDate, queryIdList);
    }

    private static List<ObjectElement> parseXMLFistTask(Date queryDate, List<Integer> queryIdList) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        List<ObjectElement> resultList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(Utils.asAddrObjXml)) {

            try {
                XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(fis);
                while (xmlEventReader.hasNext()) {
                    XMLEvent nextEvent = xmlEventReader.nextEvent();
                    if (nextEvent.isStartElement()) {
                        StartElement startElement = nextEvent.asStartElement();
                        if (startElement.getName().getLocalPart().equals("OBJECT")) {

                            String objectIdAttrValue = startElement.getAttributeByName(
                                    AttributeName.OBJECTID.getValue()).getValue();

                            if (queryIdList.contains(Integer.valueOf(objectIdAttrValue))) {

                                ObjectElement objectElement = Utils.makeObject(startElement);

                                if (isDateValid(objectElement, queryDate)) {
                                    resultList.add(objectElement);
                                }
                            }
                        }
                    }
                }
            } catch (XMLStreamException e) {
                throw new RuntimeException("Error parsing XML", e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading XML file", e);
        }
        return resultList;
    }

    private static boolean isDateValid(ObjectElement obj, Date queryDate) {
        return queryDate.before(obj.getEndDate()) && queryDate.after(obj.getStartDate());
    }


}
