    package com.faaizz.dev.online_platform.api_outbound.platform;

    import com.faaizz.dev.online_platform.api_inbound.model.Staff;
    import com.faaizz.dev.online_platform.api_inbound.model.collection.StaffCollection;
    import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
    import com.faaizz.dev.online_platform.api_outbound.exceptions.ResponseException;
    import com.faaizz.dev.online_platform.api_outbound.model.UploadablePerson;
    import org.junit.jupiter.api.*;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    import static org.junit.jupiter.api.Assertions.*;

    public class Test_StaffResource {

    private StaffResource staffResource;


    /**
     * Performs admin authentication before each test
     * @throws Exception
     */
    @BeforeEach
    public void initialize() throws Exception {

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

        //DESTROY STAFF RESOURCE
        staffResource= null;

    }


    @Test
    public void test_login_and_my_account_and_logout(){

        assertDoesNotThrow( ()->{

            //LOG OUT THE USER (LOGIN ALREADY PERFORMED IN @BeforeAll)
            staffResource.logout();

            staffResource= new StaffResource("127.0.0.1:8000", "/api", "x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

            //LOGIN STAFF
           staffResource.login("aishayetunde", "9b7b0ae9d8d17ce3e893b45206ecd3e139381560e6c61c8550627f072f0286ec", "yes");

           //GRAB DETAILS OF AUTHENTICATED STAFF
            String login_staff_string= staffResource.myAccount();

            //PARSE STAFF
            Staff login_staff= APIParser.getInstance().parseSingleStaffResponse(login_staff_string);

            //VERIFY SUCCESSFUL LOGIN
            assertEquals("aishayetunde", login_staff.getEmail());

        });

    }


    @Test
    public void test_all_and_add_and_single(){

        assertDoesNotThrow( ()->{

            //ADD STAFF TO CHECK FOR
            List<String> phone_numbers= new ArrayList<>();
            phone_numbers.add("+2348098766789");
            phone_numbers.add("+2347090876543");

            UploadablePerson staff_to_add= new UploadablePerson("First", "Last",
                    "first@cCottddODECnmgggRiiiiEEK.org", "TestPassword", "1, Firma Avanue, off Firma Road",
                    "male", phone_numbers
                    );

            //ADD STAFF
            String added_staff_string= staffResource.add(staff_to_add);

            //PARSE STAFF
            Staff added_staff= APIParser.getInstance().parseSingleStaffResponse(added_staff_string);

            /*========================================================================================*/
            //GET ALL STAFF
            String matched_staff_string= staffResource.all();
            //PARSE STAFF COLLECTION
            StaffCollection matched_staff= APIParser.getInstance().parseMultiStaffResponse(matched_staff_string);
            //GET STAFF LIST
            List<Staff> staff_list= matched_staff.getStaffs();

            //CHECK NUMBER OF PAGES
            int no_of_pages= matched_staff.getMeta().getLast_page();


            boolean is_found= false;

            //IF RESULT IS ONLY ONE PAGE, SEARCH FOR THE ADDED PRODUCT ON THAT PAGE
            if(no_of_pages == 1){

                is_found= staff_list.contains(added_staff);

            }
            //OTHERWISE IF RESULT SPANS MULTIPLE PAGES, LOOP THROUGH (STARTING FROM PAGE 2, PAGE 1 ALREADY CHECKED)
            // AND CHECK EACH PAGE FOR ADDED STAFF
            else if(staff_list.contains(added_staff)){

                //CHECK FIRST PAGE
                is_found= true;

            }
            else{

                for (int page_no = 2; page_no <= no_of_pages; page_no++){

                    //CHANGE PAGE NUMBER
                    staffResource.setPage_number(page_no);
                    //GET NEW SET OF STAFF
                    matched_staff_string= staffResource.all();
                    //PARSE NEW SET OF STAFF
                    matched_staff= APIParser.getInstance().parseMultiStaffResponse(matched_staff_string);
                    //GET NEW STAFF LIST
                    staff_list= matched_staff.getStaffs();

                    //IF PRODUCT IS FOUND, SET is_found TO true AND BREAK LOOP
                    if(staff_list.contains(added_staff)){

                        is_found= true;
                        break;

                    }

                }

            }

            //PERFORM ASSERTION
            assertFalse(
                    //ASSERT TRUE
                    !(is_found)
            );


            /*========================================================================================*/
            //SINGLE STAFF
            String search_match_string= staffResource.single(added_staff.getEmail());
            //PARSE STAFF
            Staff search_match= APIParser.getInstance().parseSingleStaffResponse(search_match_string);

            assertEquals(added_staff.getEmail(), search_match.getEmail());


            /*========================================================================================*/
            //REMOVE ADDED STAFF
            staffResource.delete(added_staff.getEmail());


        });

    }

    @Test
    public void test_delete(){

        assertDoesNotThrow( ()->{

            //ADD STAFF TO CHECK FOR
            List<String> phone_numbers= new ArrayList<>();
            phone_numbers.add("+2348098766789");
            phone_numbers.add("+2347090876543");

            UploadablePerson staff_to_add= new UploadablePerson("First", "Last",
                    "first.last@CODECREEK.orrg", "TestPassword", "1, Firma Avanue, off Firma Road",
                    "male", phone_numbers
            );

            //ADD STAFF
            String added_staff_string= staffResource.add(staff_to_add);

            //PARSE STAFF
            Staff added_staff= APIParser.getInstance().parseSingleStaffResponse(added_staff_string);

            /*========================================================================================*/

            //DELETE ADDED STAFF
            staffResource.delete(added_staff.getEmail());

            //VERIFY DELETE
            assertThrows(ResponseException.class, ()->{
                staffResource.single(added_staff.getEmail());
            });

        } );

    }


    @Test
    public void test_search(){

        assertDoesNotThrow( ()->{

            //ADD STAFF TO SEARCH FOR
            List<String> phone_numbers= new ArrayList<>();
            phone_numbers.add("+2348098766789");
            phone_numbers.add("+2347090876543");

            UploadablePerson staff_to_add= new UploadablePerson("First", "Last",
                    "firfstuu@codecreDekggkky.o0orgyy", "TestPassword", "1, Firma Avanue, off Firma Road",
                    "male", phone_numbers
            );

            //ADD STAFF
            String added_staff_string= staffResource.add(staff_to_add);

            //PARSE STAFF
            Staff added_staff= APIParser.getInstance().parseSingleStaffResponse(added_staff_string);


            /*========================================================================================*/
            //SETUP SEARCH PARAMETERS
            Map<String, String> search_params= new HashMap<>();
            search_params.put("first_name", added_staff.getFirst_name());
            search_params.put("gender", added_staff.getGender());

            //PERFORM SEARCH
            String search_result_string= staffResource.search(search_params);

            //PARSE RESPONSE
            StaffCollection search_result_collection= APIParser.getInstance().parseMultiStaffResponse(search_result_string);

            List<Staff> search_result_list= search_result_collection.getStaffs();

            //GET NUMBER OF PAGES
            int no_of_pages= search_result_collection.getMeta().getLast_page();

            boolean all_match= true;

            //IF RESPONSE IS A SINGLE PAGE LONG
            if( no_of_pages == 1){

                //VERIFY THAT ALL MATCHED STAFF SATISFY THE SEARCH CRITERIA
                for(Staff staff: search_result_list){

                    if( !staff.getFirst_name().contains(added_staff.getFirst_name())  &&
                        !staff.getGender().contains(added_staff.getGender())
                    ){
                        //IF ONE OF THE MATCHES DONESN'T SATISFY THE SEARCH CRITERIA, SET all_match TO false AND BREAK THE LOOP
                        all_match= false;
                        break;
                    }

                }

            }
            //OTHERWISE IF RESPONSE IS MULTIPLE PAGES LONG
            else{

                //CHECK STAFF RESTURNED ON EACH PAGE
                for( int page= 1; page <= no_of_pages; page++){

                    staffResource.setPage_number(page);

                    //REFRESH RESULTS
                    search_result_string= staffResource.search(search_params);
                    search_result_collection= APIParser.getInstance().parseMultiStaffResponse(search_result_string);
                    search_result_list= search_result_collection.getStaffs();

                    //VERIFY THAT ALL MATCHED STAFF SATISFY THE SEARCH CRITERIA
                    for(Staff staff: search_result_list){

                        if( !staff.getFirst_name().contains(added_staff.getFirst_name())  ||
                            !staff.getGender().contains(added_staff.getGender())
                        ){
                            //IF ONE OF THE MATCHES DONESN'T SATISFY THE SEARCH CRITERIA, SET all_match TO false AND BREAK THE LOOP
                            all_match= false;
                            break;
                        }

                    }

                }

            }

            //ASSERT THAT ALL MATCHES SATISFY THE SEARCH CRITERIA
            assertFalse(
                    //ASSERT TRUE
                    !all_match
            );

            /*========================================================================================*/
            //REMOVE ADDED STAFF
            staffResource.delete(added_staff.getEmail());
            

        } );



    }


    @Test
    public void test_update(){

        assertDoesNotThrow( ()->{

            //UPDATE INFORMATION OF AUTHENTICATED USER

            //GET AUTHENTICATED STAFF
            Staff this_staff= APIParser.getInstance().parseSingleStaffResponse(staffResource.myAccount());

            //PREPARE UPLOAD DATA
            List<String> new_numbers= new ArrayList<>();
            new_numbers.add("+2348000000000");
            new_numbers.add("+2348000000021");

            UploadablePerson updated_staff= new UploadablePerson(
                    "Barton", "Joey", "", "Qui tempore dolores qui excepturi corrupti magni.",
                    "New Adress is set here", "", new_numbers
            );

            //PERFORM UPDATE
            String update_result_string= staffResource.update(this_staff.getEmail(), updated_staff);

            //ASSERT SUCCESSFUL UPDATE
            Staff update_result= APIParser.getInstance().parseSingleStaffResponse(update_result_string);

            assertEquals(updated_staff.getFirst_name(), update_result.getFirst_name());
            assertEquals(updated_staff.getLast_name(), update_result.getLast_name());
            assertEquals(updated_staff.getAddress(), update_result.getAddress());

            for(int index= 0; index < updated_staff.getPhone_numbers().size(); index++){

                assertEquals( updated_staff.getPhone_numbers().get(index), update_result.getPhone_numbers().get(index) );

            }

        } );

    }

}
