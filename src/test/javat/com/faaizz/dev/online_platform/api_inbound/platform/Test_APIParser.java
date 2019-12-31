package com.faaizz.dev.online_platform.api_inbound.platform;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class Test_APIParser {

    private static String singleProductResponseString;
    private static String multiProductResponseString;
    private static String singleStaffResponseString;
    private static String multiStaffResponseString;
    private static String singleCustomerResponseString;
    private static String multiCustomerResponseString;
    private static String singleOrderResponseString;
    private static String multiOrderResponseString;


    //RESPONSE STRING LOADER
    public static String reponseStringLoader(String filename){

        StringBuilder temp= new StringBuilder();

        try(BufferedReader br= new BufferedReader(new InputStreamReader(Test_APIParser.class.getResourceAsStream(filename + ".json")))){

            String line;

            while( (line= br.readLine()) != null ){
                temp.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp.toString();

    }

    @BeforeAll
    public static void singleProductResponseStringLoader() throws FileNotFoundException {

        singleProductResponseString= Test_APIParser.reponseStringLoader("singleProductResponse");

    }

    @BeforeAll
    public static void multiProductResponseStringLoader() throws FileNotFoundException {

        multiProductResponseString= Test_APIParser.reponseStringLoader("multiProductResponse");

    }

    @BeforeAll
    public static void singleStaffResponseStringLoader(){

        singleStaffResponseString= Test_APIParser.reponseStringLoader("singleStaffResponse");

    }

    @BeforeAll
    public static void multiStaffResponseStringLoader(){

        multiStaffResponseString= Test_APIParser.reponseStringLoader("multiStaffResponse");

    }

    @BeforeAll
    public static void singleCustomerResponseStringLoader(){

        singleCustomerResponseString= Test_APIParser.reponseStringLoader("singleCustomerResponse");

    }

    @BeforeAll
    public static void multiCustomerResponseStringLoader(){

        multiCustomerResponseString= Test_APIParser.reponseStringLoader("multiCustomerResponse");

    }

    @BeforeAll
    public static void singleOrderResponseStringLoader(){

        singleOrderResponseString= Test_APIParser.reponseStringLoader("singleOrderResponse");

    }

    @BeforeAll
    public static void multiOrderResponseStringLoader(){

        multiOrderResponseString= Test_APIParser.reponseStringLoader("multiOrderResponse");

    }

    /*========================================================================================*/
    /*  T   E   S   T   S   */

    @Test
    public void test_parseSingleProductResponse(){

        assertDoesNotThrow(()->{

            APIParser.getInstance().parseSingleProductResponse(singleProductResponseString);

        } );
    }

    @Test
    public void test_parseMultiProductResponse(){

        assertDoesNotThrow( ()->{

            APIParser.getInstance().parseMultiProductResponse(multiProductResponseString);

        });

    }

    @Test
    public void test_parseSingleStaffResponse(){

        assertDoesNotThrow( ()->{

            APIParser.getInstance().parseSingleStaffResponse(singleStaffResponseString);

        });

    }

    @Test
    public void test_parseMultiStaffResponse(){

        assertDoesNotThrow( ()->{

            APIParser.getInstance().parseMultiStaffResponse(multiStaffResponseString);

        });

    }

    @Test
    public void test_parseSingleCustomerResponse(){

        assertDoesNotThrow( ()->{

            APIParser.getInstance().parseSingleCustomerResponse(singleCustomerResponseString);

        });

    }

    @Test
    public void test_parseMultiCustomerResponse(){

        assertDoesNotThrow( ()->{

            APIParser.getInstance().parseMultiCustomerResponse(multiCustomerResponseString);

        });

    }

    @Test
    public void test_parseSingleOrderResponse(){

        assertDoesNotThrow( ()->{

            APIParser.getInstance().parseSingleOrderResponse(singleOrderResponseString);

        });

    }

    @Test
    public void test_parseMultiOrderResponse(){

        assertDoesNotThrow( ()->{

            APIParser.getInstance().parseMultiOrderResponse(multiOrderResponseString);

        });

    }

}
