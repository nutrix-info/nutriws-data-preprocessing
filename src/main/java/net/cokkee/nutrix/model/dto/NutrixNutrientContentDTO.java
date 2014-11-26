package net.cokkee.nutrix.model.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class NutrixNutrientContentDTO extends NutrixAbstractDTO {
    
    private String id;
    private Double amount;

    private String code;
    private String name;
    
    public NutrixNutrientContentDTO() {
        super();
    }

    public NutrixNutrientContentDTO(Double amount, String code, String name) {
        this();
        this.amount = amount;
        this.code = code;
        this.name = name;
    }

    /**
     * This constructor is used for testing setup
     * @param name
     * @param description
     */
    public NutrixNutrientContentDTO(String id, Double amount, String code, String name) {
        this(amount, code, name);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
