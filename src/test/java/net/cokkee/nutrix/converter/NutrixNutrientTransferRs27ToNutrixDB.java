package net.cokkee.nutrix.converter;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import net.cokkee.nutrix.model.dto.NutrixNutrientDTO;
import net.cokkee.nutrix.util.NutrixDataUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import net.cokkee.nutrix.model.dto.NutrixIngredientDTO;
import net.cokkee.nutrix.model.dto.NutrixNutrientContentDTO;
import net.cokkee.nutrix.util.NutrixConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author drupalex
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NutrixNutrientTransferRs27ToNutrixDB {
    
    private static Log log = LogFactory.getLog(NutrixNutrientTransferRs27ToNutrixDB.class);

    private int TRANSFERRING_MODE = 0;
    
    static Connection connection;
    static Statement statement;
    
    private String serviceUrl = "http://localhost:8080/nutrix-app/ws/nutrix/api";

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String serviceUrl(String path) {
        StringBuilder url = new StringBuilder();
        if(serviceUrl != null) {
            url.append(serviceUrl);
            if (!serviceUrl.endsWith("/")) {
                url.append("/");
            }
        }
        url.append(path);
        return url.toString();
    }
    
    @BeforeClass
    public static void initClass() {
        try {
             Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            
            String database = 
                "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};" +
                "DBQ=D:/2015/sr27/sr27.mdb";
            
            connection = DriverManager.getConnection( database , "", "");
        } catch(Exception e) {
            log.error(MessageFormat.format(
                    "Error on initializing JdbcOdbcDriver class. Exception {0}",
                    new Object[]{e.getMessage()}
            ));
        
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(MessageFormat.format(
                        "Error on closing connection. Exception {0}",
                        new Object[] {e.getMessage()}
                ));
            }
        }
    }
    
    @Before
    public void init() {
    }
    
    //@Test
    public void update_1_nutrient_records_from_rs27db_to_nutrixdb() {
        if (TRANSFERRING_MODE == 0) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("==@ updating nutrient records start...");
                }

                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM NUTR_DEF");

                if (log.isDebugEnabled()) {
                    log.debug("==@ looping on RecordSet...");
                }
                int count = 0;
                while(rs.next()) {
                    count++;
                    NutrixNutrientDTO item = 
                            new NutrixNutrientDTO(rs.getString("Tagname"), 
                                    rs.getString("NutrDesc"), 
                                    1, 
                                    rs.getString("Units"), 
                                    Byte.valueOf(rs.getString("Num_Dec")), 
                                    rs.getString("Nutr_No"), 
                                    NutrixConstants.USNNDB);

                    if (item.getCode() == null || item.getCode().length() == 0) {
                        item.setCode(item.getName());
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("==@ item#" + item.getCode());
                    }

                    // try to get the nutrient object from nutriWS...
                    Response res4Get = RestAssured.
                        given().
                        contentType("application/json").
                        expect().
                        when().
                        get(serviceUrl("nutrient/getbyref/" + item.getRefId() + "/" + item.getRefDb()));
                    
                    if (res4Get.getStatusCode() == 200) {
                        String responseBody = res4Get.getBody().asString();
                        JsonPath jsonPath = new JsonPath(responseBody);
                        String id = jsonPath.getString("id");
                        
                        if (log.isDebugEnabled()) {
                            log.debug("==@ item#" + item.getCode() + 
                                    " has already been available - id:" + id);
                        }
                        
                        item.setId(id);
                        Response response = RestAssured.
                            given().
                            contentType("application/json").
                            body(NutrixDataUtil.convertObjectToJson(item)).
                            when().
                            put(serviceUrl("nutrient/crud/" + item.getId()));
                        
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("==@ item#" + item.getCode() + 
                                    " has not been exist. I will be inserted");
                        }
                        
                        Response response = RestAssured.
                            given().
                            contentType("application/json").
                            body(NutrixDataUtil.convertObjectToJson(item)).
                            when().
                            post(serviceUrl("nutrient/crud"));
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("  -> item#" + item.getCode() + " has been transferred");
                    }
                }

                if (log.isDebugEnabled()) {
                    log.debug("==@ All thing done!");
                }
            } catch(Exception e) {
                if (log.isErrorEnabled()) {
                    log.error(MessageFormat.format(
                            "Error on updating data from rs27 to nutriws. Exception {0}",
                            new Object[] {e.getMessage()}
                    ));
                }
            }
        }
    }
    
    //@Test
    public void transfer_nutrient_records_from_rs27db_to_nutrixdb() {
        if (TRANSFERRING_MODE == 1) {
            Response response = RestAssured.
                    given().
                    contentType("application/json").
                    expect().
                    when().
                    get(serviceUrl("nutrient/find"));

            Assert.assertTrue(response.getStatusCode() == 200);

            String responseBody = response.getBody().asString();
            JsonPath jsonPath = new JsonPath(responseBody);

            int total = jsonPath.getInt("total");
            for(int i=0; i<total; i++) {
                String id = (jsonPath.getString("collection[" + i + "].id"));
                if (log.isDebugEnabled()) {
                    log.debug("Nutrient#" + id + " will be deleted");
                }

                RestAssured.
                    given().
                    contentType("application/json").
                    when().
                    delete(serviceUrl("nutrient/crud/" + id));
            }
        }
        
        if (TRANSFERRING_MODE == 1) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("==@ transfering nutrient records start...");
                }

                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM NUTR_DEF");

                if (log.isDebugEnabled()) {
                    log.debug("==@ looping on RecordSet...");
                }
                int count = 0;
                while(rs.next()) {
                    count++;
                    NutrixNutrientDTO item = 
                            new NutrixNutrientDTO(rs.getString("Tagname"), 
                                    rs.getString("NutrDesc"), 
                                    1, 
                                    rs.getString("Units"), 
                                    Byte.valueOf(rs.getString("Num_Dec")), 
                                    rs.getString("Nutr_No"), 
                                    NutrixConstants.USNNDB);

                    if (item.getCode() == null || item.getCode().length() == 0) {
                        item.setCode(item.getName());
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("==@ item#" + item.getCode());
                    }

                    Response response = RestAssured.
                        given().
                        contentType("application/json").
                        body(NutrixDataUtil.convertObjectToJson(item)).
                        when().
                        post(serviceUrl("nutrient/crud"));

                    if (log.isDebugEnabled()) {
                        log.debug("  -> item#" + item.getCode() + " had been inserted");
                    }
                }

                if (log.isDebugEnabled()) {
                    log.debug("==@ All thing done!");
                }
            } catch(Exception e) {
                if (log.isErrorEnabled()) {
                    log.error(MessageFormat.format(
                            "Error on inserting data from rs27 to nutriws. Exception {0}",
                            new Object[] {e.getMessage()}
                    ));
                }
            }
        }
    }
    
    @Test
    public void update_2_ingredient_records_from_rs27db_to_nutrixdb() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("==@ updating ingredient records start...");
            }

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM FOOD_DES");

            if (log.isDebugEnabled()) {
                log.debug("==@ looping on RecordSet...");
            }
            int count = 0;
            while(rs.next()) {
                count++;
                NutrixIngredientDTO item = 
                        new NutrixIngredientDTO(
                                rs.getString("Shrt_Desc"), 
                                rs.getString("Long_Desc"), 
                                "", 
                                rs.getString("NDB_No"), 
                                NutrixConstants.USNNDB);

                if (log.isDebugEnabled()) {
                    log.debug("==@ item#" + item.getCode());
                }
                
                // list all of NutrixNutrientContentDTO objects
                if (log.isDebugEnabled()) {
                    log.debug("   -> get list of Nutrients");
                }
                
                List<NutrixNutrientContentDTO> list = new ArrayList<NutrixNutrientContentDTO>();
                
                Statement subst = connection.createStatement();
                ResultSet subrs = subst.executeQuery(
                        "SELECT * FROM NUT_DATA WHERE NDB_No = '" + item.getRefId() + "'");

                while(subrs.next()) {
                    Double nutrVal = subrs.getDouble("Nutr_Val");
                    if (subrs.wasNull()) nutrVal = null;
                    
                    Double nutrMin = subrs.getDouble("Min");
                    if (subrs.wasNull()) nutrMin = null;
                    
                    Double nutrMax = subrs.getDouble("Max");
                    if (subrs.wasNull()) nutrMax = null;
                    
                    Integer numDataPts = subrs.getInt("Num_Data_Pts");
                    if (subrs.wasNull()) numDataPts = null;
                    
                    Double stdError = subrs.getDouble("Std_Error");
                    if (subrs.wasNull()) stdError = null;
                    
                    NutrixNutrientContentDTO content = 
                            new NutrixNutrientContentDTO(
                                    nutrVal,
                                    nutrMin,
                                    nutrMax,
                                    numDataPts,
                                    stdError,
                                    "",
                                    ""
                             );
                    
                    String nutrientRefId = subrs.getString("Nutr_No");
                    
                    if (log.isDebugEnabled()) {
                        log.debug("      * " + nutrientRefId);
                    }
                    
                    String nutrientId = RestAssured.
                        given().
                        contentType("application/json").
                        expect().
                        when().
                        get(serviceUrl("nutrient/getbyref/" + nutrientRefId + "/" + NutrixConstants.USNNDB)).
                        then().
                        extract().path("id");
                    
                    if (log.isDebugEnabled()) {
                        log.debug("      > " + nutrientId);
                    }
                    
                    if (nutrientId != null) {
                        content.setId(nutrientId);
                        list.add(content);
                    }
                }
                
                item.setNutrientContentList(list);
                
                // try to get the ingredient object from nutriWS...
                Response res4Get = RestAssured.
                    given().
                    contentType("application/json").
                    expect().
                    when().
                    get(serviceUrl("ingredient/getbyref/" + item.getRefId() + "/" + item.getRefDb()));

                if (res4Get.getStatusCode() == 200) {
                    String responseBody = res4Get.getBody().asString();
                    JsonPath jsonPath = new JsonPath(responseBody);
                    String id = jsonPath.getString("id");

                    if (log.isDebugEnabled()) {
                        log.debug("==@ item#" + item.getCode() + 
                                " has already been available - id:" + id);
                    }

                    item.setId(id);
                    Response response = RestAssured.
                        given().
                        contentType("application/json").
                        body(NutrixDataUtil.convertObjectToJson(item)).
                        when().
                        put(serviceUrl("ingredient/crud/" + item.getId()));

                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("==@ item#" + item.getCode() + 
                                " has not been exist. I will be inserted");
                    }

                    Response response = RestAssured.
                        given().
                        contentType("application/json").
                        body(NutrixDataUtil.convertObjectToJson(item)).
                        when().
                        post(serviceUrl("ingredient/crud"));
                }

                if (log.isDebugEnabled()) {
                    log.debug("  -> item#" + item.getCode() + " has been transferred");
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("==@ All thing done!");
            }
        } catch(Exception e){
            if (log.isDebugEnabled()){
                log.debug(MessageFormat.format(
                        "Error on updating the Ingredient list, exception: {0} - Message: {1}",
                        new Object[] {e.getClass().getSimpleName(), e.getMessage()}));
            }
        }
    }
}
