import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecondTask {

    private static final String OBJECT_ELEMENT_NAME = "OBJECT";
    private static final String ITEM_ELEMENT_NAME = "ITEM";
    private static final String PASSAGE = "проезд";

    public static List<List<ObjectElement>> getInfoFullAddress() {
        List<ObjectElement> allPassages = parseXMLPassages();
        List<List<ObjectElement>> fullAddresses = new ArrayList<>();
        for (ObjectElement passage : allPassages) {
            List<ObjectElement> currentFullAddress = new ArrayList<>();
            currentFullAddress.add(passage);
            Integer parentObjId = findParentObjId(passage.getObjectId());
            while (parentObjId != null) {

                ObjectElement parentObject = getObjectElementById(parentObjId);
                currentFullAddress.add(parentObject);

                Integer nextParentObjId = findParentObjId(parentObjId);
                ObjectElement nextParentObject = getObjectElementById(nextParentObjId);

                parentObjId = nextParentObject.getObjectId();
            }
            fullAddresses.add(currentFullAddress);
        }
        return fullAddresses;
    }


    private static ObjectElement getObjectElementById(Integer objectId) {
        ObjectElement objectElement = new ObjectElement();
            try {
                XMLEventReader xmlEventReader = createXmlEventReader(Utils.asAddrObjXml);
                while (xmlEventReader.hasNext()) {
                    XMLEvent nextEvent = xmlEventReader.nextEvent();
                    if (nextEvent.isStartElement()) {
                        StartElement startElement = nextEvent.asStartElement();
                        if (startElement.getName().getLocalPart().equals(OBJECT_ELEMENT_NAME)) {

                            String objectIdAttrValue = startElement.getAttributeByName(
                                    AttributeName.OBJECTID.getValue()).getValue();

                            String nameAttrValue = startElement.getAttributeByName(
                                    AttributeName.NAME.getValue()).getValue();

                            String typeNameAttrValue = startElement.getAttributeByName(
                                    AttributeName.TYPENAME.getValue()).getValue();

                            if (Integer.valueOf(objectIdAttrValue).equals(objectId)) {
                                objectElement.setObjectId(Integer.valueOf(objectIdAttrValue));
                                objectElement.setName(nameAttrValue);
                                objectElement.setTypeName(typeNameAttrValue);
                            }
                        }
                    }
                }
            } catch (XMLStreamException | IOException e) {
                throw new RuntimeException("Error parsing XML", e);
            }
        return objectElement;
    }

    private static Integer findParentObjId(Integer objectId) {
        Integer parentObjId = null;
            try {
                XMLEventReader xmlEventReader = createXmlEventReader(Utils.asAdmHierarchyXml);
                while (xmlEventReader.hasNext()) {
                    XMLEvent nextEvent = xmlEventReader.nextEvent();
                    if (nextEvent.isStartElement()) {
                        StartElement startElement = nextEvent.asStartElement();
                        if (startElement.getName().getLocalPart().equals(ITEM_ELEMENT_NAME)) {

                            String objectIdAttrValue = startElement.getAttributeByName(
                                    AttributeName.OBJECTID.getValue()).getValue();

                            String parentObjIdAttrValue = startElement.getAttributeByName(
                                    AttributeName.PARENTOBJID.getValue()).getValue();

                            String isActiveAttrValue = startElement.getAttributeByName(
                                    AttributeName.ISACTIVE.getValue()).getValue();

                            int isActive = Integer.parseInt(isActiveAttrValue);
                            if (Integer.valueOf(objectIdAttrValue).equals(objectId) && isActive == 1) {
                                parentObjId = Integer.valueOf(parentObjIdAttrValue);
                            }
                        }
                    }
                }
            } catch (XMLStreamException | IOException e) {
                throw new RuntimeException("Error parsing XML", e);
            }
        return parentObjId;
    }


    private static List<ObjectElement> parseXMLPassages() {

//        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
//        try (FileInputStream fis = new FileInputStream(Utils.asAddrObjXml)) {

        List<ObjectElement> resultList = new ArrayList<>();

            try {
//                XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(fis);
                XMLEventReader xmlEventReader = createXmlEventReader(Utils.asAddrObjXml);
                while (xmlEventReader.hasNext()) {
                    XMLEvent nextEvent = xmlEventReader.nextEvent();
                    if (nextEvent.isStartElement()) {
                        StartElement startElement = nextEvent.asStartElement();
                        if (startElement.getName().getLocalPart().equals(OBJECT_ELEMENT_NAME)) {
                            ObjectElement objectElement = Utils.makeObject(startElement);
                            if (objectElement.getTypeName().equals(PASSAGE) && isActualityValid(objectElement)) {
                                resultList.add(objectElement);
                            }
                        }
                    }
                }
            } catch (XMLStreamException | IOException e) {
                throw new RuntimeException("Error parsing XML", e);
            }

        return resultList;
    }

    private static XMLEventReader createXmlEventReader(String filePath) throws IOException, XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        FileInputStream fis = new FileInputStream(filePath);
        return xmlInputFactory.createXMLEventReader(fis);
    }

    private static boolean isActualityValid(ObjectElement obj) {
        return obj.getIsActive() == 1 && obj.getIsActual() == 1;
    }

}
