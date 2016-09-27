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
public class AsyncDevicesOn extends AsyncTask<String ,Void,Void>
{
    int success=0;
    String message="null";
    String url_lock_on =  "https://maker.ifttt.com/trigger/lock_on/with/key/WUfKHhCQj6I147ATXA6Y5";
    String url_blinds_on =  "https://maker.ifttt.com/trigger/blinds_commercial_on/with/key/WUfKHhCQj6I147ATXA6Y5";
    String url_blinds_residential_on =  "https://maker.ifttt.com/trigger/blinds_residential_on/with/key/WUfKHhCQj6I147ATXA6Y5";
    String url_osram_light_one_on =  "https://maker.ifttt.com/trigger/osram_light_one_on/with/key/WUfKHhCQj6I147ATXA6Y5";
    String url_osram_light_two_on =  "https://maker.ifttt.com/trigger/osram_light_two_on/with/key/WUfKHhCQj6I147ATXA6Y5";
    String url_philips_on =  "https://maker.ifttt.com/trigger/all_philips_on/with/key/WUfKHhCQj6I147ATXA6Y5";
    String url_wemo_on =  "https://maker.ifttt.com/trigger/all_wemos_on/with/key/WUfKHhCQj6I147ATXA6Y5";


    @Override
    protected Void doInBackground(String ... params) {

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost_lock,httppost_comercial_blinds,httppost_residential_blinds,httppost_osram_light_one_on,httppost_osram_light_two_on,httppost_wemo_on,httppost_philips_on;
        httppost_lock = new HttpPost(url_lock_on);
        httppost_comercial_blinds = new HttpPost(url_blinds_on);
        httppost_residential_blinds = new HttpPost(url_blinds_residential_on);
        httppost_osram_light_one_on = new HttpPost(url_osram_light_one_on);
        httppost_osram_light_two_on = new HttpPost(url_osram_light_two_on);
        httppost_wemo_on = new HttpPost(url_philips_on);
        httppost_philips_on = new HttpPost(url_wemo_on);

        try {

            // Execute HTTP Post Request
            HttpResponse response_1 = httpclient.execute(httppost_lock);
            HttpResponse response_2 = httpclient.execute(httppost_comercial_blinds);
            HttpResponse response_7 = httpclient.execute(httppost_residential_blinds);
            HttpResponse response_3 = httpclient.execute(httppost_osram_light_one_on);
            HttpResponse response_4 = httpclient.execute(httppost_osram_light_two_on);
            HttpResponse response_5 = httpclient.execute(httppost_wemo_on);
            HttpResponse response_6 = httpclient.execute(httppost_philips_on);
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
