package com.moksha.plant_watering;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static com.moksha.plant_watering.Constants.ADD_ACTION_URL;
import static com.moksha.plant_watering.Constants.GET_ACTIONS_URL;
import static com.moksha.plant_watering.Constants.GET_DEVICES_URL;

public class MainActivity extends AppCompatActivity {

    private static final Logger logger = Logger.getLogger("MainActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Gson gson = new Gson();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView systemLastPingValue = findViewById(R.id.systemLastPingValue);
        final TextView lastWateredValue = findViewById(R.id.lastWateredValue);
        final EditText editText = findViewById(R.id.durationEditView);
        final Button button = findViewById(R.id.startButton);
        final ListView pendingItems = findViewById(R.id.pendingItemsListView);
        final ListView history = findViewById(R.id.historyListView);

        final ArrayAdapter<String> pendingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        pendingItems.setAdapter(pendingAdapter);

        final ArrayAdapter<String> historyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        history.setAdapter(historyAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> body = new HashMap<>();
                        body.put("name", "ON");
                        body.put("time", editText.getText().toString());
                        body.put("timeUnit", "SECONDS");
                        try {
                            String post = new APITask(ADD_ACTION_URL, "POST", APIUtil.getCommonHeaders(), body).execute().get();
                            ApplicationAction action = gson.fromJson(post, ApplicationAction.class);
                            pendingAdapter.add(BackGroundCronTask.getActionString(action));
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url1 = GET_ACTIONS_URL + "?limit=20&order_type=desc&order_by=updatedOn&is_done=false";
                    List<ApplicationAction> pendingActionStrings = new BackGroundCronTask(url1).execute().get();
                    pendingAdapter.addAll(BackGroundCronTask.getActionStrings(pendingActionStrings));
                    String url2 = GET_ACTIONS_URL + "?limit=20&order_type=desc&order_by=updatedOn&is_done=true";
                    List<ApplicationAction> historyActionStrings = new BackGroundCronTask(url2).execute().get();
                    if (historyActionStrings != null && !historyActionStrings.isEmpty()) {
                        historyAdapter.addAll(BackGroundCronTask.getActionStrings(historyActionStrings));
                        ApplicationAction action = historyActionStrings.get(0);
                        lastWateredValue.setText(TimeUtil.getDateString(action.actionEndedAt));
                    }
                    String devicesJson = new APITask(GET_DEVICES_URL, "GET", APIUtil.getCommonHeaders(), null).execute().get();
                    List<Device> devices = gson.fromJson(devicesJson, new TypeToken<List<Device>>(){}.getType());
                    systemLastPingValue.setText(TimeUtil.getDateString(devices.get(0).updatedOn));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
