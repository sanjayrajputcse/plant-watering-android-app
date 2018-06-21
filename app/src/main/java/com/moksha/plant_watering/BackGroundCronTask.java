package com.moksha.plant_watering;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class BackGroundCronTask extends AsyncTask<Void, Void, List<ApplicationAction>> {

    private String actionURL;

    public BackGroundCronTask(String actionURL) {
        this.actionURL = actionURL;
    }

    @Override
    protected List<ApplicationAction> doInBackground(Void... voids) {
        Gson gson = new Gson();
            try {
                APITask apiTask = new APITask(actionURL, "GET", APIUtil.getCommonHeaders(), null);
                apiTask.onPreExecute();
                String response = apiTask.doInBackground();
                List<ApplicationAction> allActions = gson.fromJson(response, new TypeToken<List<ApplicationAction>>(){}.getType());
                return allActions;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }

    public static List<String> getActionStrings(List<ApplicationAction> allActions) {
        List<String> list = new ArrayList<>();
        if (allActions != null && !allActions.isEmpty()) {
            for (ApplicationAction action : allActions) {
                list.add(getActionString(action));
            }
        }
        return list;
    }

    public static String getActionString(ApplicationAction action) {
        String s = action.id + ".|" + action.name + "|" + action.time + " " + action.timeUnit + "|" + action.status;
        if (action.actionStartedAt != null) {
            s = s + "|" + TimeUtil.getDateString(action.actionStartedAt);
        }
        return s;
    }
}
