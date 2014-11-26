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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

/**
 *
 * @author drupalex
 */
public class NutrixNutrientTransferRs27ToNutrixDB {
    
    private static Log log = LogFactory.getLog(NutrixNutrientTransferRs27ToNutrixDB.class);

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
            System.out.println(MessageFormat.format(
                    "Error on initializing JdbcOdbcDriver class. Exception {0}",
                    new Object[] {e.getMessage()}
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
    
    @Test
    public void transfer() {}
    
    //@Test
    public void transfer_nutrient_records_from_rs27db_to_nutrixdb() {
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
                                Integer.valueOf(rs.getString("Nutr_No")));
                
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
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Error!");
        }
    }
}
