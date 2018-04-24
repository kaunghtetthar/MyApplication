package com.example.kaunghtetthar.myapplication.DAOs;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Low level networking utility
 * Created by kaunghtetthar on 5/2/17.
 */

public class NetworkDAO {

    /**
     * Make a network call to the URI, and return the data that results.
     * @param uri the URI that we wish to invoke, using the GET method.
     * @return the data from the URI
     * @throws ClientProtocolException if we have any trouble making the network call.
     * @throws IOException if we have any trouble making the network call.
     */
    public String request(String uri) throws ClientProtocolException,IOException {

        String result = "";


        // make a get call to HTTP.
        HttpGet httpGet = new HttpGet(uri);


        // handle the response that we get in return
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        // create an HTTPClient, which will coordinate the get and response.
        HttpClient httpClient = new DefaultHttpClient();

        //send the URI to the get method, and have the response handler parse it and return a
        //result to us.
        result = httpClient.execute(httpGet, responseHandler);

        //return our return variable.
        return result;

    }

}
