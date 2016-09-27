package mysmarthome.londonhydro.com.mysmarthome.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import mysmarthome.londonhydro.com.mysmarthome.DevicesAsyncThreads.AsyncOneWemoOn;
import mysmarthome.londonhydro.com.mysmarthome.R;

public class WemoActivity extends AppCompatActivity {

    ImageButton btn_turn_on_off;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wemo);

        btn_turn_on_off=(ImageButton)findViewById(R.id.btn_turn_on_off);

        btn_turn_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncOneWemoOn().execute("");
            }
        });
    }
}
