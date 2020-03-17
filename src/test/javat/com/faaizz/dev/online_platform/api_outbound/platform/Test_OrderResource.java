package com.faaizz.dev.online_platform.api_outbound.platform;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.api_inbound.model.Product;
import com.faaizz.dev.online_platform.api_inbound.model.Staff;
import com.faaizz.dev.online_platform.api_inbound.model.collection.OrderCollection;
import com.faaizz.dev.online_platform.api_inbound.model.Customer;
import com.faaizz.dev.online_platform.api_inbound.model.Order;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.model.UploadableOrder;
import com.faaizz.dev.online_platform.api_outbound.model.UploadablePerson;
import com.faaizz.dev.online_platform.api_outbound.model.UploadableProduct;

public class Test_OrderResource{

    private OrderResource orderResource;
    private StaffResource staffResource;
    private CustomerResource customerResource;
    private ProductResource productResource;


    @BeforeEach
    public void initialize(){

        // CREATE ORDER RESOURCE FOR EACH TEST  
        orderResource= new OrderResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

        //CREATE STAFF RESOURCE
        staffResource= new StaffResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

        // CREATE CUSTOMER RESOURCE
        customerResource= new CustomerResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

        // CREATE PRODUCT RESOURCE
        productResource= new ProductResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

    }

    private void loginAdmin(){

        try {


            //CREATE STAFF RESOURCE
            staffResource= new StaffResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

            //LOGIN STAFF
            staffResource.login("barton.enid@bogan.com", "Qui tempore dolores qui excepturi corrupti magni.", "yes");

        } catch (Exception e){

            System.err.println(e.getMessage());

        }

    }

    private void loginCustomer(){

        try {


            // CREATE CUSTOMER RESOURCE
            customerResource= new CustomerResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

            // LOGIN CUSTOMER
            customerResource.login("xhaag@example.net", "Voluptate quas impedit sapiente dicta.", "yes");

        } catch (Exception e){

            System.err.println(e.getMessage());

        }

    }


    @AfterEach
    public void deinitialize(){

        orderResource= null;
        staffResource= null;
        customerResource= null;
        productResource= null;

    }


    @Test
    public void test_add_and_update_and_pending_and_delivered_and_failed_and_delete(){

        assertDoesNotThrow( ()->{


            // LOGIN ADMIN TO CREATE A NEW PRODUCT
            loginAdmin();

            // CREATE A NEW PRODUCT
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

            //LOGOUT ADMIN
            staffResource.logout();


            /*========================================================================================*/

            // LOGIN CUSTOMER TO PLACE A NEW ORDER
            loginCustomer();

            // CREATE A NEW ORDER
            UploadableOrder new_order= new UploadableOrder(added_product.getId(), optionM.get("size"), 
                                                            Integer.valueOf(optionM.get("quantity")), "xhaag@example.net");

            // ADD ORDER
            String added_order_string= orderResource.add(new_order);

            Order added_order= APIParser.getInstance().parseSingleOrderResponse(added_order_string);

            // LOGOUT CUSTOMER, LOGIN STAFF
            customerResource.logout();

            loginAdmin();


            /*========================================================================================*/   

            // CHECK PENDING ORDERS FOR NEWLY CREATED ORDER
            String matched_orders_string= orderResource.pending();

            // PARSE RESPONSE
            OrderCollection matched_orders= APIParser.getInstance().parseMultiOrderResponse(matched_orders_string);

            // GET NUMBER OF PAGES RETURNED
            int no_of_pages= matched_orders.getMeta().getLast_page();

            // GET LIST OF RETURNED ORDERS
            List<Order> matched_orders_list= matched_orders.getOrders();

            // BOOLEAN TO SAVE THE STATE OF THE SEARCH
            boolean is_found= false;

            // IF RESULT IS A SINGLE PAGE
            if( no_of_pages == 1 ){

                // SEARCH LIST OF ORDERS FOR THE NEWLY CREATED ORDER
                is_found= matched_orders_list.contains(added_order);

            }
            // OTHERWISE IF RESULT SPANS MULTIPLE PAGES
            else{

                // loop through the pages and search till the newly added order is found
                for( int page= 1; page <= no_of_pages; page++){

                    // SET GET page PARAMETER
                    orderResource.setPage_number(page);

                    // GET NEW RESULT
                    matched_orders_string= orderResource.pending();

                    // GET NEW LIST
                    matched_orders_list= APIParser.getInstance().parseMultiOrderResponse(matched_orders_string).getOrders();

                    // SEARCH FOR THE ORDER
                    if( matched_orders_list.contains(added_order) ){

                        // IF FOUND, SET is_found TO TRUE AND BREAK THE LOOP
                        is_found= true;
                        break;

                    }

                }

            }

            // ASSERT THAT ADDED ORDER WAS FOUND IN LIST OF PENDING ORDERS
            assertFalse(

                //ASSERT TRUE
                !is_found

            );


            /*========================================================================================*/   
            // UPDATE ORDER STATUS TO DELIVERED AND SEARCH DELIVERED ORDERS FOR IT

            //Update Order Status
            new_order.setStatus("delivered");
            LocalDateTime now= LocalDateTime.now();
            String del_date= now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ");
            new_order.setDelivery_date(del_date);

            // PERFORM UPDATE
            orderResource.update(new_order, added_order.getId());

            // RETRIEVE DELIEVERED ORDERS
            //RESET PAGE TO 1
            orderResource.setPage_number(1);
            matched_orders_string= orderResource.delivered();

            // PARSE RESPONSE
            matched_orders= APIParser.getInstance().parseMultiOrderResponse(matched_orders_string);

            // GET NUMBER OF PAGES RETURNED
            no_of_pages= matched_orders.getMeta().getLast_page();

            // GET LIST OF RETURNED ORDERS
            matched_orders_list= matched_orders.getOrders();

            // BOOLEAN TO SAVE THE STATE OF THE SEARCH
            is_found= false;

            // IF RESULT IS A SINGLE PAGE
            if( no_of_pages == 1 ){

                // SEARCH LIST OF ORDERS FOR THE NEWLY UPDATED ORDER
                is_found= matched_orders_list.contains(added_order);

            }
            // OTHERWISE IF RESULT SPANS MULTIPLE PAGES
            else{

                // loop through the pages and search till the newly added order is found
                for( int page= 1; page <= no_of_pages; page++){

                    // SET GET page PARAMETER
                    orderResource.setPage_number(page);

                    // GET NEW RESULT
                    matched_orders_string= orderResource.delivered();

                    // GET NEW LIST
                    matched_orders_list= APIParser.getInstance().parseMultiOrderResponse(matched_orders_string).getOrders();

                    // SEARCH FOR THE ORDER
                    if( matched_orders_list.contains(added_order) ){

                        // IF FOUND, SET is_found TO TRUE AND BREAK THE LOOP
                        is_found= true;
                        break;

                    }

                }

            }

            // ASSERT THAT ADDED ORDER WAS FOUND IN LIST OF DELIVERED ORDERS
            assertFalse(

                //ASSERT TRUE
                !is_found

            );


            /*========================================================================================*/   
            // UPDATE ORDER STATUS TO DELIVERED AND SEARCH DELIVERED ORDERS FOR IT

            //Update Order Status
            new_order.setStatus("failed");
            new_order.setDelivery_date("");
            new_order.setFailure_cause("I messed Up!");
            new_order.setFailure_date(del_date);

            // PERFORM UPDATE
            orderResource.update(new_order, added_order.getId());

            // RETRIEVE FAILED ORDERS
            //RESET PAGE TO 1
            orderResource.setPage_number(1);
            matched_orders_string= orderResource.failed();

            // PARSE RESPONSE
            matched_orders= APIParser.getInstance().parseMultiOrderResponse(matched_orders_string);

            // GET NUMBER OF PAGES RETURNED
            no_of_pages= matched_orders.getMeta().getLast_page();

            // GET LIST OF RETURNED ORDERS
            matched_orders_list= matched_orders.getOrders();

            // BOOLEAN TO SAVE THE STATE OF THE SEARCH
            is_found= false;

            // IF RESULT IS A SINGLE PAGE
            if( no_of_pages == 1 ){

                // SEARCH LIST OF ORDERS FOR THE FAILED ORDER
                is_found= matched_orders_list.contains(added_order);

            }
            // OTHERWISE IF RESULT SPANS MULTIPLE PAGES
            else{

                // loop through the pages and search till the FAILED order is found
                for( int page= 1; page <= no_of_pages; page++){

                    // SET GET page PARAMETER
                    orderResource.setPage_number(page);

                    // GET NEW RESULT
                    matched_orders_string= orderResource.failed();

                    // GET NEW LIST
                    matched_orders_list= APIParser.getInstance().parseMultiOrderResponse(matched_orders_string).getOrders();

                    // SEARCH FOR THE ORDER
                    if( matched_orders_list.contains(added_order) ){

                        // IF FOUND, SET is_found TO TRUE AND BREAK THE LOOP
                        is_found= true;
                        break;

                    }

                }

            }

            // ASSERT THAT ADDED ORDER WAS FOUND IN LIST OF FAILED ORDERS
            assertFalse(

                //ASSERT TRUE
                !is_found

            );


            /*========================================================================================*/           
        
            //LOGIN ADMIN
            staffResource= new StaffResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");
            
            loginAdmin();

            //DELETE ADDED ORDER
            orderResource.delete(added_order.getId());

            //DELETE ADDED PRODUCT
            productResource.delete(added_product.getId());




            
        } );

    }


    @Test
    public void test_single_and_product_and_customer_and_staff(){


        assertDoesNotThrow( ()->{

            // LOGIN ADMIN TO CREATE A NEW PRODUCT
            loginAdmin();

            // CREATE A NEW PRODUCT
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
                            "Test Prttoduct", "Firma", "This is a test product from Firma",
                            "clothing", "male", "Trousers", "Red", "23000",
                            "Leather", options, image_one, image_two, image_three
                    );

            String add_product_string= productResource.add(product_to_add);

            //PARSE ADDED PRODUCT
            Product added_product= APIParser.getInstance().parseSingleProductResponse(add_product_string);

            //LOGOUT ADMIN
            staffResource.logout();


            loginAdmin();

            /*========================================================================================*/

            // CREATE AND LOGIN CUSTOMER TO PLACE A NEW ORDER
            List<String> phone_numbers= new ArrayList<>();
            phone_numbers.add("+2348005050690");
            phone_numbers.add("+2348505050690");

            UploadablePerson customer_to_add= new UploadablePerson( "Aisha", "Yetunde",
                    "nawhodehere@cbbccodecreekk.orgg", "PasswordHere", "Kurt-Schumacher, 16",
                    "female", phone_numbers);

            // ADD NEW CUSTOMER
            String added_customer_string= customerResource.add(customer_to_add);

            //PARSE CUSTOMER
            Customer added_customer= APIParser.getInstance().parseSingleCustomerResponse(added_customer_string);


            // LOGOUT ADMIN
            staffResource.logout();

            // LOGIN CUSTOMER
            customerResource= new CustomerResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");
            customerResource.login("nawhodehere@cbbccodecreekk.orgg", "PasswordHere", "yes");

            /*========================================================================================*/

            // CREATE A NEW ORDER
            UploadableOrder new_order= new UploadableOrder(added_product.getId(), optionM.get("size"), 
                                                            Integer.valueOf(optionM.get("quantity")), "xhaag@example.net");

            // ADD ORDER
            String added_order_string= orderResource.add(new_order);

            Order added_order= APIParser.getInstance().parseSingleOrderResponse(added_order_string);


            /*========================================================================================*/ 
            // GET THE NEWLY ADDED ORDER
            String matched_order_string= orderResource.single(added_order.getId());

            Order matched_order= APIParser.getInstance().parseSingleOrderResponse(matched_order_string);

            //ASSERT THAT THE SAME ORDER WAS RETURNED
            assertFalse(
                //assert true
                !added_order.equals(matched_order)
            );


            /*========================================================================================*/ 
            // GET ORDERS BELONGING TO THE NEWLY CREATED CUSTOMER
            String matched_orders_string= orderResource.customer(added_customer.getEmail());

            OrderCollection matched_orders= APIParser.getInstance().parseMultiOrderResponse(matched_orders_string);

            // ASSERT THAT EVERY MATCHED ORDER IS OF THE CUSTOMER
            matched_orders.getOrders().forEach( order->{

                assertEquals( added_customer.getEmail(), order.getCustomer_email() );

            } );


            // LOGOUT CUSTOMER, LOGIN STAFF
            customerResource.logout();

            loginAdmin();


            /*========================================================================================*/
            // GET UNASSIGNED ORDERS
            matched_orders_string= orderResource.unassigned();

            matched_orders= APIParser.getInstance().parseMultiOrderResponse(matched_orders_string);

            // ASSERT THAT EVERY MATCHED ORDER IS OF THE SPECIFIED PRODUCT
            matched_orders.getOrders().forEach( order->{

                assertEquals( null, order.getStaff_email() );

            } );


            /*========================================================================================*/ 
            // GET ORDERS PERTAINING TO A SINGLE PRODUCT
            matched_orders_string= orderResource.product(added_product.getId());

            matched_orders= APIParser.getInstance().parseMultiOrderResponse(matched_orders_string);

            // ASSERT THAT EVERY MATCHED ORDER IS OF THE SPECIFIED PRODUCT
            matched_orders.getOrders().forEach( order->{

                assertEquals( added_product.getId(), order.getProduct_id() );

            } );


            /*========================================================================================*/ 
            // CREATE A NEW STAFF
            phone_numbers= new ArrayList<>();
            phone_numbers.add("+2348098766789");
            phone_numbers.add("+2347090876543");

            UploadablePerson staff_to_add= new UploadablePerson("First", "Last",
                    "first@ciiEEK.org", "TestPassword", "1, Firma Avanue, off Firma Road",
                    "male", phone_numbers
                    );

            //ADD STAFF
            String added_staff_string= staffResource.add(staff_to_add);

            //PARSE STAFF
            Staff added_staff= APIParser.getInstance().parseSingleStaffResponse(added_staff_string);

            // ASSIGN THE NEW ORDER TO THE NEW STAFF
            new_order.setStaff_email(added_staff.getEmail());
            // EXECUTE UPDATE   
            orderResource.update(new_order, added_order.getId());


            // GET ORDERS PERTAINING TO A SINGLE STAFF
            matched_orders_string= orderResource.staff(added_staff.getEmail());

            matched_orders= APIParser.getInstance().parseMultiOrderResponse(matched_orders_string);

            // ASSERT THAT EVERY MATCHED ORDER IS OF THE SPECIFIED PRODUCT
            matched_orders.getOrders().forEach( order->{

                assertEquals( added_staff.getEmail(), order.getStaff_email() );

            } );




            /*========================================================================================*/
            // DELETE PRODUCT AND ORDER
            orderResource.delete(added_order.getId());

            productResource.delete(added_product.getId());

            // DELETE ADDED CUSTOMER
            customerResource.delete(added_customer.getEmail());

            //DELETE ADDED STAFF
            staffResource.delete(added_staff.getEmail());


        } );


    }


    @Test
    public void test_massDelete(){


        assertDoesNotThrow( ()->{

            loginAdmin();

            // CREATE PRODUCTS TO DELETE
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

            String add_product_one_string= productResource.add(product_to_add);

            String add_product_two_string= productResource.add(product_to_add);

            String add_product_three_string= productResource.add(product_to_add);

            //PARSE ADDED PRODUCTS
            Product added_product_one= APIParser.getInstance().parseSingleProductResponse(add_product_one_string);
            Product added_product_two= APIParser.getInstance().parseSingleProductResponse(add_product_two_string);
            Product added_product_three= APIParser.getInstance().parseSingleProductResponse(add_product_three_string);


            // MASS DELETE ADDED PRODUCTS
            List<Integer> ids= new ArrayList<>();
            ids.add(added_product_one.getId());
            ids.add(added_product_two.getId());
            ids.add(added_product_three.getId());


        } );

    }


}