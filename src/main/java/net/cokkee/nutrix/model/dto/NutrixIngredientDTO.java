package net.cokkee.nutrix.model.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class NutrixIngredientDTO extends NutrixAbstractDTO {
    
    private String id;
    private String code;
    private String label;
    private String description;
    
    private String refId;
    private String refDb;
    
    private List<NutrixNutrientContentDTO> nutrientContentList;

    public NutrixIngredientDTO() {
        super();
    }

    public NutrixIngredientDTO(String code, String label, String description) {
        this();
        this.code = code;
        this.label = label;
        this.description = description;
    }
    
    public NutrixIngredientDTO(String code, String label, String description,
            String refId, String refDb) {
        this();
        this.code = code;
        this.label = label;
        this.description = description;
        this.refId = refId;
        this.refDb = refDb;
    }
    
    /**
     * This constructor is used for testing setup
     * @param id
     * @param code (~Short_Desc@rs27)
     * @param label (~Long_Desc@rs27)
     * @param description
     */
    public NutrixIngredientDTO(String id, String code, String label, String description) {
        this(code, label, description);
        this.id = id;
    }
    
    public NutrixIngredientDTO(String id, String code, String label, String description,
            String refId, String refDb) {
        this(code, label, description, refId, refDb);
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getRefDb() {
        return refDb;
    }

    public void setRefDb(String refDb) {
        this.refDb = refDb;
    }

    public List<NutrixNutrientContentDTO> getNutrientContentList() {
        return nutrientContentList;
    }

    public void setNutrientContentList(List<NutrixNutrientContentDTO> nutrientContentList) {
        this.nutrientContentList = nutrientContentList;
    }

    @XmlRootElement
    public static class Pack {

        private Integer total = null;

        private List<NutrixIngredientDTO> collection = null;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<NutrixIngredientDTO> getCollection() {
            return collection;
        }

        public void setCollection(List<NutrixIngredientDTO> collection) {
            this.collection = collection;
        }
    }
}
