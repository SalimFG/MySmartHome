package mysmarthome.londonhydro.com.mysmarthome.DevicesAsyncThreads;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by Salim on 26/05/2016.
 */
public class AsyncOneWemoOff extends AsyncTask<String ,Void,Void>
{
    int success=0;
    String message="null";
    String url_ac_wemo_on =  "https://maker.ifttt.com/trigger/ac_wemo_off/with/key/WUfKHhCQj6I147ATXA6Y5";



    @Override
    protected Void doInBackground(String ... params) {

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost_ac_wemo_on;
        httppost_ac_wemo_on = new HttpPost(url_ac_wemo_on);


        try {

            // Execute HTTP Post Request
            HttpResponse response_1 = httpclient.execute(httppost_ac_wemo_on);

//            HttpResponse response_7 = httpclient.execute(httppost);
//            HttpResponse response_8 = httpclient.execute(httppost);
//            HttpResponse response_9 = httpclient.execute(httppost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            Log.e("salim",e.getMessage().toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("salim",e.getMessage().toString());

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("async","done");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("Asyinc","DONE");
        super.onPostExecute(aVoid);
    }

    }
