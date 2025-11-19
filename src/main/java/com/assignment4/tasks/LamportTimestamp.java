
package com.assignment4.tasks;


import static java.lang.Integer.max;

public class LamportTimestamp {

    private int timestamp;

    public LamportTimestamp(int time) {
        timestamp = time;
    }

    public synchronized void tick() {
        timestamp += 1;
    }

    public synchronized int getCurrentTimestamp(){
        return timestamp;
    }

    public synchronized void updateClock(int receivedTimestamp) {
        timestamp = max(timestamp, receivedTimestamp) + 1;
    }
}
