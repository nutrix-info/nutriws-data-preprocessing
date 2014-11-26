package net.cokkee.nutrix.model.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class NutrixNutrientDTO extends NutrixAbstractDTO {
    
    private String id;
    private String code;
    private String name;
    private String description;
    
    private Integer originFlag;
    private String unitType;
    private Byte numDec;
    private Integer usnndbNo;

    public NutrixNutrientDTO() {
        super();
    }

    public NutrixNutrientDTO(String code, String name, String description) {
        this();
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    public NutrixNutrientDTO(String code, String name, 
            Integer originFlag, String unitType, Byte numDec, Integer usnndbNo) {
        this(code, name, null);
        this.originFlag = originFlag;
        this.unitType = unitType;
        this.numDec = numDec;
        this.usnndbNo = usnndbNo;
    }

    /**
     * This constructor is used for testing setup
     * @param name
     * @param description
     */
    public NutrixNutrientDTO(String id, String code, String name, String description) {
        this(code, name, description);
        this.id = id;
    }

    public NutrixNutrientDTO(String id, String code, String name, 
            Integer originFlag, String unitType, Byte numDec, Integer usnndbNo) {
        this(code, name, originFlag, unitType, numDec, usnndbNo);
        this.id = id;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOriginFlag() {
        return originFlag;
    }

    public void setOriginFlag(Integer originFlag) {
        this.originFlag = originFlag;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public Byte getNumDec() {
        return numDec;
    }

    public void setNumDec(Byte numDec) {
        this.numDec = numDec;
    }

    public Integer getUsnndbNo() {
        return usnndbNo;
    }

    public void setUsnndbNo(Integer usnndbNo) {
        this.usnndbNo = usnndbNo;
    }
    
    @XmlRootElement
    public static class Pack {

        private Integer total = null;

        private List<NutrixNutrientDTO> collection = null;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<NutrixNutrientDTO> getCollection() {
            return collection;
        }

        public void setCollection(List<NutrixNutrientDTO> collection) {
            this.collection = collection;
        }
    }
}
