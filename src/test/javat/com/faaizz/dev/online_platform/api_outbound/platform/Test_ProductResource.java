package com.faaizz.dev.online_platform.api_outbound.platform;

import com.faaizz.dev.online_platform.api_inbound.model.Product;
import com.faaizz.dev.online_platform.api_inbound.model.collection.ProductCollection;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.model.UploadableProduct;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the main APISeeker class
 */
public class Test_ProductResource {


    private ProductResource productResource;
    private StaffResource staffResource;

    @BeforeEach
    public void initialize(){

        //CREATE PRODUCT RESOURCE FOR EACH TEST
        productResource = new ProductResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

        //CREATE STAFF RESOURCE FOR EACH TEST
        staffResource= new StaffResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

        try {

            //LOGIN STAFF FOR EACH TEST
            staffResource.login("barton.enid@bogan.com", "Qui tempore dolores qui excepturi corrupti magni.", "yes");

        } catch (Exception e){

            System.err.println(e.getMessage());

        }

    }

    @AfterEach
    public void deInitialize(){

        //DESTROY PRODUCT RESOURCE
        productResource= null;

        //DESTROY STAFF RESOURCE
        staffResource= null;

    }


    @Test
    public void test_all(){

        assertDoesNotThrow( ()->{

            //ADD PRODUCT TO SEARCH FOR
            Map<String, String> optionM= new HashMap<>();
            optionM.put("size", "41" );
            optionM.put("quantity", "2");

            Map<String, String> optionLg= new HashMap<>();
            optionLg.put("size", "6");
            optionLg.put("quantity", "7");

            List<Map<String, String>> options= new ArrayList<>();
            options.add(optionM);
            options.add(optionLg);

            File image_one= new File("img/one.png");
            File image_two= new File("img/two.png");
            File image_three= new File("img/three.jpg");

            UploadableProduct product_to_add= new UploadableProduct(
                    "Test Search Product", "Firma", "This is a test product from Firma",
                    "clothing", "male", "Trousers", "Red", "2000",
                    "Leather", options, image_one, image_two, image_three
            );

            //ADD PRODUCT
            String added_product_string= productResource.add(product_to_add);
            //PARSE PRODUCT
            Product added_product= APIParser.getInstance().parseSingleProductResponse(added_product_string);

            //PERFORM SEARCH
            String matched_products_string= productResource.all();

            //PARSE RESPONSE
            ProductCollection matched_products= APIParser.getInstance().parseMultiProductResponse(matched_products_string);

            //GRAB NUMBER OF PAGES
            int no_of_pages= matched_products.getMeta().getLast_page();

            //IF RESULT IS A SINGLE PAGE

            if(no_of_pages == 1) {

                //CHECK MATCHED PRODUCTS
                assertFalse( !(matched_products.getProducts().contains(added_product)) );

            }
            //OTHERWISE IF RESULT SPANS MULTIPLE PAGES, LOOP THROUGH PAGES
            else {

                boolean is_found = false;

                //CHECK MATCHED PRODUCTS ON FIRST PAGE
                if (!(matched_products.getProducts().contains(added_product))) {

                    //IF PRODUCT IS NOT FOUND ON FIRST PAGE, LOOP THROUGH OTHER PAGES
                    for(int page_no= 2; page_no <= no_of_pages; page_no++){

                        //SET NEW PAGE
                        productResource.setPage_number(page_no);

                        //PERFORM SEARCH
                        matched_products_string= productResource.all();

                        //PARSE RESPONSE
                        matched_products= APIParser.getInstance().parseMultiProductResponse(matched_products_string);



                        if (matched_products.getProducts().contains(added_product)){
                            //IF FOUND, SET is_found TO true AND BREAK LOOP
                            is_found= true;
                            break;
                        }

                    }

                }
                //OTHERWISE IF FOUND ON THE FIRST PAGE, SET is_found TO true
                else {
                    is_found = true;
                }

                //PERFORM ASSERTION
                assertFalse( !(is_found) );


            }


            //REMOVE PRODUCT
            productResource.delete(added_product.getId());

        });

    }

    @Test
    public void test_newIn_of_section_clothing_sub_section_male_1week_old(){

        assertDoesNotThrow( ()->{

            //ADD PRODUCT TO SEARCH FOR
            Map<String, String> optionM= new HashMap<>();
            optionM.put("size", "41" );
            optionM.put("quantity", "2");

            Map<String, String> optionLg= new HashMap<>();
            optionLg.put("size", "6");
            optionLg.put("quantity", "7");

            List<Map<String, String>> options= new ArrayList<>();
            options.add(optionM);
            options.add(optionLg);

            File image_one= new File("img/one.png");
            File image_two= new File("img/two.png");
            File image_three= new File("img/three.jpg");

            UploadableProduct product_to_add= new UploadableProduct(
                    "Test Search Product", "Firma", "This is a test product from Firma",
                    "shoes", "male", "Loafers", "Red", "2000",
                    "Leather", options, image_one, image_two, image_three
            );

            //ADD PRODUCT
            String added_product_string= productResource.add(product_to_add);
            //PARSE PRODUCT
            Product added_product= APIParser.getInstance().parseSingleProductResponse(added_product_string);

            //PERFORM SEARCH
            String matched_products_string= productResource.newIn(1, added_product.getSection(), added_product.getSub_section());

            //PARSE RESPONSE
            ProductCollection matched_products= APIParser.getInstance().parseMultiProductResponse(matched_products_string);

            //GRAB NUMBER OF PAGES
            int no_of_pages= matched_products.getMeta().getLast_page();

            //IF RESULT IS A SINGLE PAGE

            if(no_of_pages == 1) {

                //CHECK MATCHED PRODUCTS
                assertFalse( !(matched_products.getProducts().contains(added_product)) );

            }
            //OTHERWISE IF RESULT SPANS MULTIPLE PAGES, LOOP THROUGH PAGES
            else {

                boolean is_found = false;

                //CHECK MATCHED PRODUCTS ON FIRST PAGE
                if (!(matched_products.getProducts().contains(added_product))) {

                    //IF PRODUCT IS NOT FOUND ON FIRST PAGE, LOOP THROUGH OTHER PAGES
                    for(int page_no= 2; page_no <= no_of_pages; page_no++){

                        //SET NEW PAGE
                        productResource.setPage_number(page_no);

                        //PERFORM SEARCH
                        matched_products_string= productResource.all();

                        //PARSE RESPONSE
                        matched_products= APIParser.getInstance().parseMultiProductResponse(matched_products_string);



                        if (matched_products.getProducts().contains(added_product)){
                            //IF FOUND, SET is_found TO true AND BREAK LOOP
                            is_found= true;
                            break;
                        }

                    }

                }
                //OTHERWISE IF FOUND ON THE FIRST PAGE, SET is_found TO true
                else {
                    is_found = true;
                }

                //PERFORM ASSERTION
                assertFalse( !(is_found) );


            }


            //REMOVE PRODUCT
            productResource.delete(added_product.getId());


        });

    }

    @Test
    public void test_single_with_id_15(){

        assertDoesNotThrow( ()->{

            //ADD PRODUCT TO SEARCH FOR
            Map<String, String> optionM= new HashMap<>();
            optionM.put("size", "41" );
            optionM.put("quantity", "2");

            Map<String, String> optionLg= new HashMap<>();
            optionLg.put("size", "6");
            optionLg.put("quantity", "7");

            List<Map<String, String>> options= new ArrayList<>();
            options.add(optionM);
            options.add(optionLg);

            File image_one= new File("img/one.png");
            File image_two= new File("img/two.png");
            File image_three= new File("img/three.jpg");

            UploadableProduct product_to_add= new UploadableProduct(
                    "Test Search Product", "Firma", "This is a test product from Firma",
                    "shoes", "male", "Loafers", "Red", "2000",
                    "Leather", options, image_one, image_two, image_three
            );

            //ADD PRODUCT
            String added_product_string= productResource.add(product_to_add);
            //PARSE PRODUCT
            Product added_product= APIParser.getInstance().parseSingleProductResponse(added_product_string);

            //GET PRODUCTS
            String matched_product_string= productResource.single(added_product.getId());
            //PARSE PRODUCT
            Product matched_product= APIParser.getInstance().parseSingleProductResponse(matched_product_string);


            //COMPARE FIELDS
            assertEquals(added_product.getName(), matched_product.getName());
            assertEquals(added_product.getBrand(), matched_product.getBrand());
            assertEquals(added_product.getDescription(), matched_product.getDescription());
            assertEquals(added_product.getSection(), matched_product.getSection());
            assertEquals(added_product.getSub_section(), matched_product.getSub_section());
            assertEquals(added_product.getCategory(), matched_product.getCategory());
            assertEquals(added_product.getColor(), matched_product.getColor());
            assertEquals(Double.valueOf(added_product.getPrice()), Double.valueOf(matched_product.getPrice()));
            assertEquals(added_product.getMaterial(), matched_product.getMaterial());


            //REMOVE PRODUCT
            productResource.delete(added_product.getId());

        });

    }

    @Test
    public void test_search(){

        assertDoesNotThrow( ()->{

            //ADD PRODUCT TO SEARCH FOR
            Map<String, String> optionM= new HashMap<>();
            optionM.put("size", "41" );
            optionM.put("quantity", "2");

            Map<String, String> optionLg= new HashMap<>();
            optionLg.put("size", "6");
            optionLg.put("quantity", "7");

            List<Map<String, String>> options= new ArrayList<>();
            options.add(optionM);
            options.add(optionLg);

            File image_one= new File("img/one.png");
            File image_two= new File("img/two.png");
            File image_three= new File("img/three.jpg");

            UploadableProduct product_to_add= new UploadableProduct(
                    "Test Search Product", "Firma", "This is a test product from Firma",
                    "shoes", "male", "Loafers", "Red", "2000",
                    "Leather", options, image_one, image_two, image_three
            );

            //ADD PRODUCT
            String added_product_string= productResource.add(product_to_add);
            //PARSE PRODUCT
            Product added_product= APIParser.getInstance().parseSingleProductResponse(added_product_string);


            //BUILD SEARCH PARAMETER
            Map<String, String> post_data= new HashMap<>();

            post_data.put("section", product_to_add.getSection());
            post_data.put("sub_section", product_to_add.getSub_section());
            post_data .put("category", product_to_add.getCategory());

            //PERFORM SEARCH
            String matched_products_string= productResource.search(post_data);

            //PARSE RESPONSE
            ProductCollection matched_products= APIParser.getInstance().parseMultiProductResponse(matched_products_string);

            //GRAB NUMBER OF PAGES
            int no_of_pages= matched_products.getMeta().getLast_page();

            //IF RESULT IS A SINGLE PAGE

            if(no_of_pages == 1) {

                //CHECK MATCHED PRODUCTS
                matched_products.getProducts().forEach(product -> {

                    assertEquals(product_to_add.getSection(), product.getSection());
                    assertEquals(product_to_add.getSub_section(), product.getSub_section());
                    assertEquals(product_to_add.getCategory(), product.getCategory());

                });

            }
            //OTHERWISE IF RESULT SPANS MULTIPLE PAGES, LOOP THROUGH PAGES
            else{

                //CHECK MATCHED PRODUCTS ON FIRST PAGE
                matched_products.getProducts().forEach(product -> {

                    assertEquals(product_to_add.getSection(), product.getSection());
                    assertEquals(product_to_add.getSub_section(), product.getSub_section());
                    assertEquals(product_to_add.getCategory(), product.getCategory());

                });

                //LOOP THROUGH OTHER PAGES
                for(int page_no= 2; page_no <= no_of_pages; page_no++){

                    productResource.setPage_number(page_no);

                    //PERFORM SEARCH
                    matched_products_string= productResource.search(post_data);

                    //PARSE RESPONSE
                    matched_products= APIParser.getInstance().parseMultiProductResponse(matched_products_string);

                    //CHECK MATCHED PRODUCTS
                    matched_products.getProducts().forEach(product -> {

                        assertEquals(product_to_add.getSection(), product.getSection());
                        assertEquals(product_to_add.getSub_section(), product.getSub_section());
                        assertEquals(product_to_add.getCategory(), product.getCategory());

                    });

                }

            }




            //REMOVE PRODUCT
            productResource.delete(added_product.getId());

        });

    }

    @Test
    public void test_add_and_test_delete(){

        assertDoesNotThrow( ()->{

            //ADD PRODUCT
            Map<String, String> optionM= new HashMap<>();
            optionM.put("size", "M" );
            optionM.put("quantity", "26");

            Map<String, String> optionLg= new HashMap<>();
            optionLg.put("size", "LG");
            optionLg.put("quantity", "17");

            List<Map<String, String>> options= new ArrayList<>();
            options.add(optionM);
            options.add(optionLg);

            File image_one= new File("img/one.png");
            File image_two= new File("img/two.png");
            File image_three= new File("img/three.jpg");

            UploadableProduct product_to_add= new UploadableProduct(
                            "Test Product", "Firma", "This is a test product from Firma",
                            "clothing", "male", "Trousers", "Red", "23000",
                            "Leather", options, image_one, image_two, image_three
                    );

            String add_product_string= productResource.add(product_to_add);

            //PARSE ADDED PRODUCT
            Product added_product= APIParser.getInstance().parseSingleProductResponse(add_product_string);

            //VERIFY PRODUCT DETAILS
            assertEquals(product_to_add.getName(), added_product.getName());
            assertEquals(product_to_add.getBrand(), added_product.getBrand());
            assertEquals(product_to_add.getDescription(), added_product.getDescription());
            assertEquals(product_to_add.getSection(), added_product.getSection());
            assertEquals(product_to_add.getSub_section(), added_product.getSub_section());
            assertEquals(product_to_add.getCategory(), added_product.getCategory());
            assertEquals(product_to_add.getColor(), added_product.getColor());
            assertEquals(Double.valueOf(product_to_add.getPrice()), Double.valueOf(added_product.getPrice()));
            assertEquals(product_to_add.getMaterial(), added_product.getMaterial());


            //DELETE ADDED PRODUCT
            productResource.delete(added_product.getId());

        });

    }

    @Test
    public void test_update(){

        assertDoesNotThrow( ()->{

            //ADD PRODUCT TO UPDATE
            Map<String, String> optionM= new HashMap<>();
            optionM.put("size", "41" );
            optionM.put("quantity", "2");

            Map<String, String> optionLg= new HashMap<>();
            optionLg.put("size", "6");
            optionLg.put("quantity", "7");

            List<Map<String, String>> options= new ArrayList<>();
            options.add(optionM);
            options.add(optionLg);

            File image_one= new File("img/one.png");
            File image_two= new File("img/two.png");
            File image_three= new File("img/three.jpg");

            UploadableProduct product_to_add= new UploadableProduct(
                    "Test Update Product", "Firma", "This is a test product from Firma",
                    "clothing", "male", "Trousers", "Red", "2000",
                    "Leather", options, image_one, image_two, image_three
            );

            //ADD PRODUCT
            String added_product_string= productResource.add(product_to_add);
            //PARSE PRODUCT
            Product added_product= APIParser.getInstance().parseSingleProductResponse(added_product_string);

            //UPDATED PRODUCT
            UploadableProduct uploadable_updated_product= new UploadableProduct(
                    "Updated Test Product", "", "",
                    "clothing", "male", "", "Grren", "",
                    "Shiffon", options, image_one, null, null
            );

            //PERFORM UPDATE
            String updated_product_string= productResource.update(added_product.getId(), uploadable_updated_product);
            //PARSE PRODUCT
            Product updated_product= APIParser.getInstance().parseSingleProductResponse(updated_product_string);


            //CONFIRM UPDATED FIELD (BLANK STRINGS DO NOT COUNT AS UPDATES[CHECK uploadable_updated_product ABOVE])

            //NAME
            assertEquals(uploadable_updated_product.getName(), updated_product.getName());
            //SECTION
            assertEquals(uploadable_updated_product.getSection(), updated_product.getSection());
            //SUB-SECTION
            assertEquals(uploadable_updated_product.getSub_section(), updated_product.getSub_section());
            //COLOR
            assertEquals(uploadable_updated_product.getColor(), updated_product.getColor());
            //MATERIAL
            assertEquals(uploadable_updated_product.getMaterial(), updated_product.getMaterial());

            //DELETE ADDED PRODUCT
            productResource.delete(added_product.getId());

        });

    }

    @Test
    public void test_massDelete(){

        assertDoesNotThrow( ()->{

            //ADD PRODUCT TO UPDATE
            Map<String, String> optionM= new HashMap<>();
            optionM.put("size", "41" );
            optionM.put("quantity", "2");

            Map<String, String> optionLg= new HashMap<>();
            optionLg.put("size", "6");
            optionLg.put("quantity", "7");

            List<Map<String, String>> options= new ArrayList<>();
            options.add(optionM);
            options.add(optionLg);

            File image_one= new File("img/one.png");
            File image_two= new File("img/two.png");
            File image_three= new File("img/three.jpg");

            UploadableProduct product_to_add= new UploadableProduct(
                    "Test Update Product", "Firma", "This is a test product from Firma",
                    "clothing", "male", "Trousers", "Red", "2000",
                    "Leather", options, image_one, image_two, image_three
            );

            //ADD PRODUCT 1
            String added_product_one_string= productResource.add(product_to_add);
            //PARSE PRODUCT 1
            Product added_product_one= APIParser.getInstance().parseSingleProductResponse(added_product_one_string);

            //ADD PRODUCT 2
            String added_product_two_string= productResource.add(product_to_add);
            //PARSE PRODUCT 2
            Product added_product_two= APIParser.getInstance().parseSingleProductResponse(added_product_two_string);

            //ADD PRODUCT 3
            String added_product_three_string= productResource.add(product_to_add);
            //PARSE PRODUCT 3
            Product added_product_three= APIParser.getInstance().parseSingleProductResponse(added_product_three_string);


            //GET ALL PRODUCTS
            String all_products_string= productResource.all();

            //SETUP ARRAY OF PRODUCT IDs
            int[] ids_array= new int[]{ added_product_one.getId(), added_product_two.getId(), added_product_three.getId() };

            productResource.massDelete( ids_array );

        });

    }

}
