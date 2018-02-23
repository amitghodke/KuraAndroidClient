package amittestapp.admin.example.com.tcpconnectdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class ConnectionActivity extends AppCompatActivity {
Button btnCall;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        textView = (TextView) findViewById(R.id.txt_result);
        btnCall=(Button)findViewById(R.id.btn_Call);
        btnCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                call();
            }
        });
    }

    private void call() {
        String url="add_number?email=5&pwd=5";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, MainApp.URL+"hello?name=Amitghodke90", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                textView.setText("Response:-"+s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                textView.setText("Exception:-"+volleyError.getMessage());
            }
        });

        MainApp.getInstance().addToRequestQueue(stringRequest);
    }

}
