package com.assignment4.tasks;

import java.util.Arrays;

public class VectorClock {

  private final int[] timestamps;

  public VectorClock(int numOfClients) {
    timestamps = new int[numOfClients];
    Arrays.fill(timestamps, 0);
  }

  public synchronized void setVectorClock(int processId, int time) {
    if (checkProcessIdValidity(processId)) {
        timestamps[processId] = time;
    }
  }

  public synchronized void tick(int processId) {
    if (checkProcessIdValidity(processId)) {
        timestamps[processId]++;
    }
  }

  public synchronized int getCurrentTimestamp(int processId) {
    if (!checkProcessIdValidity(processId)) {
        return -1; // Invalid process ID
    }
    return timestamps[processId];
  }

  public synchronized void updateClock(VectorClock other) {
    for (int i = 0; i < timestamps.length; i++) {
        int otherTimeStamp = other.getCurrentTimestamp(i);
        if (otherTimeStamp > timestamps[i]) {
            timestamps[i] = otherTimeStamp;
        }
    }
  }

  public synchronized String showClock() {
    return Arrays.toString(timestamps);
  }

  // TODO:
  // For Task 2.2
  // Check if a message can be delivered or has to be buffered
  public synchronized boolean checkAcceptMessage(int senderId, VectorClock senderClock) {

      if (senderClock.getCurrentTimestamp(senderId) != timestamps[senderId] + 1) {
          return false;
      }

      for (int i = 0; i < timestamps.length; i++) {
          if (i != senderId) {
              if (senderClock.getCurrentTimestamp(i) > timestamps[i]) {
                  return false;
              }
          }
      }

      return true;
  }

  private boolean checkProcessIdValidity(int processId) {
      return processId >= 0 && processId < timestamps.length;
  }
}
