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
    private String label;
    private String description;
    private List<NutrixNutrientContentDTO> nutrientContentList;

    public NutrixIngredientDTO() {
        super();
    }

    public NutrixIngredientDTO(String label, String description) {
        this();
        this.label = label;
        this.description = description;
    }

    /**
     * This constructor is used for testing setup
     * @param label
     * @param description
     */
    public NutrixIngredientDTO(String id, String label, String description) {
        this(label, description);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
