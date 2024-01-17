import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ObjectElement {

    private Integer objectId;
    private String name;
    private String typeName;
    private Date startDate;
    private Date endDate;
    private Integer isActual;
    private Integer isActive;

}
