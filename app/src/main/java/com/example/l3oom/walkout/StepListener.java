package com.example.l3oom.walkout;


// Will listen to step alerts
public interface StepListener {

    public void step(long timeNs);
    public void calDistance();
    public void calKcal();

}
