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
    private Double nutrMin;
    private Double nutrMax;
    
    private Integer numDataPts;
    private Double stdError;

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
    
    public NutrixNutrientContentDTO(Double amount, Double nutrMin, Double nutrMax, 
            Integer numDataPts, Double stdError, String code, String name) {
        this(amount, code, name);
        this.nutrMin = nutrMin;
        this.nutrMax = nutrMax;
        this.numDataPts = numDataPts;
        this.stdError = stdError;
    }

    /**
     * This constructor is used for testing setup
     * @param id
     * @param amount
     * @param code
     * @param name
     */
    public NutrixNutrientContentDTO(String id, Double amount, String code, String name) {
        this(amount, code, name);
        this.id = id;
    }
    
    public NutrixNutrientContentDTO(String id, 
            Double amount, Double nutrMin, Double nutrMax, 
            Integer numDataPts, Double stdError, String code, String name) {
        this(amount, nutrMin, nutrMax, numDataPts, stdError, code, name);
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

    public Double getNutrMin() {
        return nutrMin;
    }

    public void setNutrMin(Double nutrMin) {
        this.nutrMin = nutrMin;
    }

    public Double getNutrMax() {
        return nutrMax;
    }

    public void setNutrMax(Double nutrMax) {
        this.nutrMax = nutrMax;
    }

    public Integer getNumDataPts() {
        return numDataPts;
    }

    public void setNumDataPts(Integer numDataPts) {
        this.numDataPts = numDataPts;
    }

    public Double getStdError() {
        return stdError;
    }

    public void setStdError(Double stdError) {
        this.stdError = stdError;
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
