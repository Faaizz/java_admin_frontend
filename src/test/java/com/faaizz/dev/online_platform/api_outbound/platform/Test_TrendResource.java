package com.faaizz.dev.online_platform.api_outbound.platform;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;

import com.faaizz.dev.online_platform.api_inbound.model.Trend;
import com.faaizz.dev.online_platform.api_inbound.model.collection.ProductCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.TrendCollection;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.model.UploadableTrend;

import org.apiguardian.api.API;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Test_TrendResource {

    private TrendResource trendResource;
    private StaffResource staffResource;

    @BeforeEach
    public void initialize(){

        // CREATE TREND RESOURCE FOR EACH TEST
        trendResource= new TrendResource("127.0.0.1:8000", "/api", "a");

        //CREATE STAFF RESOURCE FOR EACH TEST
        staffResource= new StaffResource("127.0.0.1:8000", "/api", "a");

        try {

            //LOGIN STAFF FOR EACH TEST
            staffResource.login("clementine.reilly@satterfield.biz", "a", "yes");

        } catch (Exception e){

            System.err.println(e.getMessage());

        }

    }

    @AfterEach
    public void deInitialize(){

        // DESTROY TRENDS RESOURCE
        trendResource= null;

    }

    @Test
    public void test_all(){

        assertDoesNotThrow( ()->{

            // PULL ALL TRENDS
            String trends_response= trendResource.all();

            // PARSE RESPONSE
            TrendCollection trends_colection= APIParser.getInstance().parseMultiTrendResponse(trends_response);

            // GRAB NUMBER OF PAGES
            int no_of_pages= trends_colection.getMeta().getLast_page();

            // PRINT 1ST PAGE RESULTS
            System.out.println(trends_colection.getTrends());

            // PRINT RESULTS FOR MULTIPLE PAGES
            if(no_of_pages>2){

                for(int idx= 2; idx < no_of_pages; idx++){

                    // SET NEW PAGE
                    trendResource.setPage_number(idx);

                    // PERFORM NEW PULL
                    trends_response= trendResource.all();

                    // PARSE RESPONSE
                    trends_colection= APIParser.getInstance().parseMultiTrendResponse(trends_response);

                    System.out.println(trends_colection.getTrends());

                }

            }

        } );

    }

    @Test
    public void test_gender(){

        assertDoesNotThrow( ()->{

            // PULL ALL TRENDS
            String trends_response= trendResource.all("male");

            // PARSE RESPONSE
            TrendCollection trends_colection= APIParser.getInstance().parseMultiTrendResponse(trends_response);

            // GRAB NUMBER OF PAGES
            int no_of_pages= trends_colection.getMeta().getLast_page();

            // PRINT 1ST PAGE RESULTS
            System.out.println(trends_colection.getTrends());

            // PRINT RESULTS FOR MULTIPLE PAGES
            if(no_of_pages>2){

                for(int idx= 2; idx <= no_of_pages; idx++){

                    // SET NEW PAGE
                    trendResource.setPage_number(idx);

                    // PERFORM NEW PULL
                    trends_response= trendResource.all();

                    // PARSE RESPONSE
                    trends_colection= APIParser.getInstance().parseMultiTrendResponse(trends_response);

                    System.out.println(trends_colection.getTrends());

                }

            }

        } );

    }

    @Test
    public void test_products(){

        assertDoesNotThrow( ()->{

            // PULL ALL TRENDS
            TrendCollection trends_collection= APIParser.getInstance().parseMultiTrendResponse(trendResource.all());
            // PULL FIRST TREND
            Trend trend= trends_collection.getTrends().get(0);
            // PULL PRODUCTS
            ProductCollection products= APIParser.getInstance().parseMultiProductResponse(trendResource.products(trend.getId()));
            // PRINT PRODUCTS
            System.out.println(products.getProducts());

        } );

    }

    @Test
    public void test_add_update_delete(){

        assertDoesNotThrow( ()->{

            // CREATE NEW TREND
            File image_one= new File("img/one.png");

            UploadableTrend upload_trend= new UploadableTrend("Test Trend", "Blank Description", "male", image_one);

            // UPLOAD TREND
            String add_response= trendResource.add(upload_trend);
            // PARSE RESPONSE
            Trend added_trend= APIParser.getInstance().parseSIngleTrendResponse(add_response);

            // PERFORM SEARCH
            String matched_trends_string= trendResource.all(upload_trend.getGender());
            // PARSE RESPONSE
            TrendCollection matched_trends= APIParser.getInstance().parseMultiTrendResponse(matched_trends_string);
            // GRAB NUMBER OF PAGES
            int no_of_pages= matched_trends.getMeta().getLast_page();

            // IF RESULT IS A SINGLE PAGE
            if(no_of_pages == 1){

                // SEARCH FOR UPLOADED TREND
                assertFalse( !matched_trends.getTrends().contains(added_trend));

            }

            // IF RESULT SPANS MULTIPLE PAGES
            else{

                boolean is_found= false;

                // CHECK FIRST PAGE
                if( !(matched_trends.getTrends().contains(added_trend))){

                    // NOT FOUND ON FIRST PAGE
                        for(int page_no=2; page_no <= no_of_pages; page_no++){

                            // SET NEW PAGE
                            trendResource.setPage_number(page_no);
                            // MAKE AND PARSENEW REQUEST
                            matched_trends= APIParser.getInstance().parseMultiTrendResponse(trendResource.all(upload_trend.getGender()));

                            if(matched_trends.getTrends().contains(added_trend)){
                                is_found= true;
                                break;
                            }

                        }

                }
                else{
                    // FOUND ON FIRST PAGE
                    is_found= true;
                }

                // ASSERTION
                assertFalse(!is_found);

                // TEST UPDATE
                if(is_found){

                    // NEW IMAGE
                    image_one= new File("img/two.png");

                    // UPDATE TREND
                    UploadableTrend update_trend= new UploadableTrend("Updated name", "Still Blank", "female", image_one);

                    // PERFORM UPDATE
                    trendResource.update(added_trend.getId(), update_trend);

                    // VERIFY UPDATE
                    // RELOAD TREND
                    added_trend= APIParser.getInstance().parseSIngleTrendResponse(trendResource.single(added_trend.getId()));
                    
                    // ASSERTION
                    assertFalse( !(added_trend.getName().contentEquals(update_trend.getName())) );

                }


            }

            // DELETE PRODUCT
            trendResource.delete(added_trend.getId());


        } );


    }

}