package com.faaizz.dev.online_platform.api_outbound.platform;

import com.faaizz.dev.online_platform.api_outbound.exceptions.ResponseException;
import com.faaizz.dev.online_platform.api_outbound.model.Cookie;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
/**
 * This is the main class.
 */
public abstract class APIResource {


    /**
     * Holds the server's address
     */
    private String base_URL;
    /**
     * Holds the path to the API (including the /api)
     */
    private String api_path;
    private String api_token;

    //HTTP CLIENT
    CloseableHttpClient http_client;


    /* P   A   G   I   N   A   T   I   O   N    */
    private int per_page;
    private int page_number;


    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public APIResource(String base_URL, String api_path, String api_token){

        this.base_URL= base_URL;
        this.api_path= api_path;
        this.api_token= api_token;

        this.per_page= 0;
        this.page_number= 0;

        //CREATE CLIENT
        this.http_client= HttpClients.createDefault();
    }


    /*========================================================================================*/
    /*  S   E   T   T   E   R   S   */

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }



    /*========================================================================================*/

    void setHeaders(HttpRequestBase request){

        //AUTHORIZATION
        request.addHeader("Authorization", "Bearer " + this.api_token);

        //ACCEPT
        request.addHeader("Accept", "text/json");

    }

    void examineResponse(CloseableHttpResponse response) throws Exception {

        StatusLine responseLine= response.getStatusLine();

        //IF RESPONSE IS NOT "OK" OR REDIRECTION
        if( (responseLine.getStatusCode() < 200) || (responseLine.getStatusCode() >= 400) ){

            try (BufferedReader br= new BufferedReader(new InputStreamReader(response.getEntity().getContent()))){

                //GET RESPONSE BODY
                StringBuilder temp = new StringBuilder();

                String line= "";

                while( (line= br.readLine()) != null){

                    temp.append(line);

                }

                throw new ResponseException(temp.toString(), responseLine.getStatusCode());
            }
            finally{
                response.close();
            }

        }

    }

    URI buildUri(String extra_path) throws URISyntaxException {

        //IF per_page IS SET
        if(this.per_page > 0){

            return new URIBuilder()
                    .setScheme("http")
                    .setHost(this.base_URL)
                    .setPath(this.api_path + extra_path)
                    .addParameter("per_page", String.valueOf(this.per_page))
                    .build();

        }

        //IF page_number IS SET
        if(this.page_number > 0){

            return new URIBuilder()
                    .setScheme("http")
                    .setHost(this.base_URL)
                    .setPath(this.api_path + extra_path)
                    .addParameter("page", String.valueOf(this.page_number))
                    .build();


        }

        //OTHERWISE
        return new URIBuilder()
                .setScheme("http")
                .setHost(this.base_URL)
                .setPath(this.api_path + extra_path)
                .build();

    }

    CloseableHttpResponse executeRequest(HttpRequestBase request) throws IOException, ClassNotFoundException {

        //SET HEADERS
        this.setHeaders(request);

        //ATTACH COOKIES
        this.attachCookies(request);

        //EXECUTE REQUEST
        return this.http_client.execute(request);

    }

    String handleResponse(CloseableHttpResponse response) throws Exception {

        //A TRY BLOCK IS USED HERE TO ENABLE PROPER CLOSING OF THE CloseableHttpResponse OBJECT
        try {

            //EXAMINE RESPONSE
            this.examineResponse(response);

            //SAVE COOKIES
            this.saveCookies(response);

            //GET RESPONSE ENTITY
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {

                //WRITE RESPONSE INTO A STRING
                StringBuilder response_stringSB= new StringBuilder();

                try (BufferedReader br = new BufferedReader(new InputStreamReader(responseEntity.getContent()))) {

                    String line;

                    while ((line = br.readLine()) != null) {
                        response_stringSB.append(line);

                    }

                }

                //RETURN RESPONSE BODY
                return response_stringSB.toString();

            } else {

                return "";

            }
        }
        finally{
            EntityUtils.consume(response.getEntity());
            response.close();
        }

    }


    /**
     * Check for response cookies and save them into persistent storage if they exist
     * @param response
     */
    void saveCookies(CloseableHttpResponse response) throws IOException {

        //CHECK FOR COOKIES
        Header[] cookie_headers= response.getHeaders("Set-Cookie");

        for(Header header: cookie_headers){

            String temp= header.getValue();

            //Cut at first "=" to get Cookie name
            int equals_index= temp.indexOf("=");
            String name= temp.substring(0, equals_index);

            //Grab everything before the first semicolon
            int semi_colon_index= temp.indexOf(";");
            String value= temp.substring(0, semi_colon_index);

            //IF WE HAVE COOKIES, ADD TO COOKIE STORE
            CookieStore.getCookies().put(name, new Cookie(value));

        }


    }


    /**
     * This method tries to attach cookies to the request
     * @param request
     */
    void attachCookies(HttpRequestBase request) throws IOException, ClassNotFoundException {


        if(CookieStore.getCookies().size() > 0){

            CookieStore.getCookies().forEach( (name, cookie)->{

                request.addHeader("Cookie", cookie.getValue());

            });

        }

    }

}
