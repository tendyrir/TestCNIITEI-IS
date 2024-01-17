import javax.xml.namespace.QName;

public enum AttributeName {
    OBJECTID(new QName("OBJECTID")),
    NAME(new QName("NAME")),
    TYPENAME(new QName("TYPENAME")),
    PARENTOBJID(new QName("PARENTOBJID")),
    ISACTIVE(new QName("ISACTIVE"));

    private final QName value;

    AttributeName(QName value) {
        this.value = value;
    }

    public QName getValue() {
        return value;
    }

}
