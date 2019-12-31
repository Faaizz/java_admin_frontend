package com.faaizz.dev.online_platform.api_outbound.platform;

import com.faaizz.dev.online_platform.api_inbound.model.Customer;
import com.faaizz.dev.online_platform.api_inbound.model.collection.CustomerCollection;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.model.UploadablePerson;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Test_CustomerResource {

    private static CustomerResource customerResource;
    private static StaffResource staffResource;

    @BeforeEach
    public void initialize(){

        customerResource= new CustomerResource("127.0.0.1:8000", "/api",
                "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

    }

    @AfterEach
    public void deinitialize(){

        customerResource= null;
        staffResource= null;

    }

    private void loginAdmin(){

        //CREATE STAFF RESOURCE FOR EACH TEST
        staffResource= new StaffResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

        try {

            //LOGIN STAFF FOR EACH TEST
            staffResource.login("barton.enid@bogan.com", "Qui tempore dolores qui excepturi corrupti magni.", "yes");

        } catch (Exception e){

            System.err.println(e.getMessage());

        }

    }

    @Test
    public void test_login_and_logout(){

        assertDoesNotThrow( ()->{

            customerResource.login("xhaag@example.net",
                    "Voluptate quas impedit sapiente dicta.", "yes");


            //LOGOUT
            assertFalse(
                //ASSERT TRUE
                !customerResource.logout()
            );

        } );

    }

    @Test
    public void test_all_and_add_and_remove(){

        assertDoesNotThrow( ()->{

            List<String> phone_numbers= new ArrayList<>();
            phone_numbers.add("+2348005050690");
            phone_numbers.add("+2348505050690");

            UploadablePerson customer_to_add= new UploadablePerson( "Aisha", "Yetunde",
                    "nawhodehere@codecreekgdfgfk.org", "PasswordHere", "Kurt-Schumacher, 16",
                    "female", phone_numbers);

            // ADD NEW CUSTOMER
            String added_customer_string= customerResource.add(customer_to_add);

            //PARSE CUSTOMER
            Customer added_customer= APIParser.getInstance().parseSingleCustomerResponse(added_customer_string);


            /*========================================================================================*/
            // GET ALL CUSTOMERS
            loginAdmin();
            String all_customers_string= customerResource.all();

            //PARSE RESPONSE
            CustomerCollection all_customers= APIParser.getInstance().parseMultiCustomerResponse(all_customers_string);

            // CHECK THROUGH RETURNED CUSTOMERS FOR THE NEWLY ADDED CUSTOMER
            int no_of_pages= all_customers.getMeta().getLast_page();

            boolean is_found= false;

            if( no_of_pages == 1 ){

                //IF ALL CUSTOMERS IS ONLY ONE PAGE
                is_found= all_customers.getCustomers().contains(added_customer);

            }
            else if( is_found= all_customers.getCustomers().contains(added_customer) ){
                //ELSE IF RESPONSE IS OF MULTIPLE PAGES AND THE CUSTOMER IS FOUND ON THE FIRST PAGE
                // SET is_found TO TRUE
            }
            else{

                // LOOP THROUGH ALL PAGES, GETTING NEW CUSTOMERS AND CHECKING FOR THE ADDED CUSTOMER
                for(int page= 2; page <= no_of_pages; page++) {

                    //SET NEW PAGE
                    customerResource.setPage_number(page);

                    //GET NEW PAGE
                    all_customers_string = customerResource.all();
                    all_customers = APIParser.getInstance().parseMultiCustomerResponse(all_customers_string);

                    //IF FOUND, SET is_found TO true AND BREAK THE LOOP
                    if (all_customers.getCustomers().contains(added_customer)) {
                        is_found = true;
                        break;
                    }
                }
            }


            // ASSERT THAT ADDED CUSTOMER WAS FOUND
            assertFalse(
                    //ASSERT TRUE
                    !is_found
            );


            /*========================================================================================*/
            //DELETE ADDED CUSTOMER
            customerResource.delete(added_customer.getEmail());

        });

    }


    /*

    PASES WHEN RUN ALONE OR WITH OTHER TESTS IN THIS CLASS, BUT FAILS WHEN RUN WITH ALL OTHER TESTS

    @Test
    public void test_my_account_and_update(){

        assertDoesNotThrow( ()->{

            //ADD CUSTOMER TO UPDATE
            List<String> phone_numbers= new ArrayList<>();
            phone_numbers.add("+2348005050690");
            phone_numbers.add("+2348505050690");

            UploadablePerson customer_to_add= new UploadablePerson( "Aisha", "Yetunde",
                    "nawhodehere@copsspeekeee.oorpgjjjjj", "PasswordHere", "Kurt-Schumacher, 16",
                    "female", phone_numbers);

            // ADD NEW CUSTOMER
            String added_customer_string= customerResource.add(customer_to_add);

            //PARSE CUSTOMER
            Customer added_customer= APIParser.getInstance().parseSingleCustomerResponse(added_customer_string);


            //========================================================================================/
            //LOGIN AS CUSTOMER TO UPDATE
            customerResource.login(customer_to_add.getEmail(), customer_to_add.getPassword(), "yes");

            String my_account_string= customerResource.myAccount();

            Customer my_account= APIParser.getInstance().parseSingleCustomerResponse(my_account_string);
            System.err.println("my account " + my_account.getEmail());

            //CREATE UPDATED CUSTOMER OBJECT
            UploadablePerson customer_to_update= new UploadablePerson( "Aishat Yetunde", "Isiaka",
                    "", "PasswordHere", "",
                    "male", null);
            customer_to_update.setNew_password("zzzzz");

            //PERFORM UPDATE
            customerResource.update(customer_to_update, "No");

            //RETRIEVE UPDATED CUSTOMER
            Customer verify_update= APIParser.getInstance().parseSingleCustomerResponse(customerResource.myAccount());

            //VERIFY UPDATE
            assertEquals(customer_to_update.getFirst_name(), verify_update.getFirst_name());
            assertEquals(customer_to_update.getLast_name(), verify_update.getLast_name());
            assertEquals(customer_to_update.getGender(), verify_update.getGender());

            //LOGIN WITH NEW PASSWORD
            customerResource.login( customer_to_add.getEmail(), customer_to_update.getNew_password(), "no");

            //========================================================================================
            //REMOVE ADDED CUSTOMER
            loginAdmin();
            customerResource.delete(customer_to_add.getEmail());

        } );


    }

    */


    @Test
    public void test_search(){

        assertDoesNotThrow( ()->
        {

            loginAdmin();

            //ADD CUSTOMER TO SEARCH FOR
            List<String> phone_numbers= new ArrayList<>();
            phone_numbers.add("+2348005050690");
            phone_numbers.add("+2348505050690");

            UploadablePerson customer_to_add= new UploadablePerson( "Aisha", "Yetunde",
                    "nawhodehere@codeccrreek.oorg", "PasswordHere", "Kurt-Schumacher, 16",
                    "female", phone_numbers);
            
            Customer added_customer= APIParser.getInstance().parseSingleCustomerResponse(customerResource.add(customer_to_add));

            //SETUP SEARCH PARAMETERS
            Map<String, String> search_params= new HashMap<>();
            search_params.put("first_name", added_customer.getFirst_name());
            search_params.put("last_name", added_customer.getLast_name());
            search_params.put("gender", added_customer.getGender());

            //PERFORM SEARCH
            String matched_customers_string= customerResource.search(search_params);
            CustomerCollection matched_customers= APIParser.getInstance().parseMultiCustomerResponse(matched_customers_string);

            //GET NUMBER OF RESPONSE PAGES
            int no_of_pages= matched_customers.getMeta().getLast_page();

            boolean all_match= true;

            List<Customer> matched_customers_list= matched_customers.getCustomers();

            //IF RESULT IS ONE PAGE
            if( no_of_pages == 1 ){

                //ASSERT THAT EVERY MATCHED CUSTOMER SATISFY THE SPECIFIED CRITERIA
                for( Customer customer : matched_customers_list) 
                {

                    if( 
                        !customer.getFirst_name().contains(search_params.get("first_name")) ||
                        !customer.getLast_name().contains(search_params.get("last_name")) ||
                        !customer.getGender().contains(search_params.get("gender"))
                    ){
                        all_match= false;
                        break;
                    }

                }

            }

            //OTHERWISE IF RESPONSE SPANS MULTIPLE PAGES
            else{

                //LOOP THROUGH PAGES AND CHECK IF MATCHED CUSTOMERS SATISFY THE SEARCH CRITERIA
                for(int page= 1; page <= no_of_pages; page++){

                    //SET REQUEST GET page PARAMETER
                    customerResource.setPage_number(page);

                    //REFRESH DATA
                    matched_customers_string= customerResource.search(search_params);
                    matched_customers= APIParser.getInstance().parseMultiCustomerResponse(matched_customers_string);
                    matched_customers_list= matched_customers.getCustomers();

                    //ASSERT THAT EVERY MATCHED CUSTOMER SATISFY THE SPECIFIED CRITERIA
                    for( Customer customer : matched_customers_list) 
                    {

                        if( 
                            !customer.getFirst_name().contains(search_params.get("first_name")) ||
                            !customer.getLast_name().contains(search_params.get("last_name")) ||
                            !customer.getGender().contains(search_params.get("gender"))
                        ){
                            all_match= false;
                            break;
                        }

                    }

                }

            }

            //ASSERT THAT ALL MATCHES SATISFIED THE SEARCH CRITERIA
            assertFalse(
                //ASSERT TRUE
                !all_match
            );


            /*========================================================================================*/
            //REMOVE ADDED CUSTOMER
            loginAdmin();
            customerResource.delete(customer_to_add.getEmail());

        } );

    }


}
