package com.moksha.plant_watering;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class ApplicationAction {
    public Long id;
    public String name;
    public String time;
    public TimeUnit timeUnit;
    public boolean done;
    public String status;
    public Long actionStartedAt;
    public Long actionEndedAt;
    public Long createdOn;
    public Long updatedOn;

}
