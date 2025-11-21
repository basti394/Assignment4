package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class VectorClientThread implements Runnable {

  private final DatagramSocket clientSocket;
  private final VectorClock vcl;
  private final int id;
  private final byte[] receiveData = new byte[1024]; // Buffer for incoming data
  private final List<Message> buffer = new ArrayList<>(); // This buffer can be used for Task 2.2

  public VectorClientThread(DatagramSocket clientSocket, VectorClock vcl, int id) {
    this.clientSocket = clientSocket;
    this.vcl = vcl;
    this.id = id;
  }

  @Override
  public void run() {

      try {
          while (true) {
              DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
              clientSocket.receive(packet);
              String receivedMessage = new String(packet.getData(), 0, packet.getLength());
              String[] parts = receivedMessage.split(":");

              if (parts.length >= 3) {
                  String messageContent = parts[0];
                  String vectorString = parts[1];
                  int senderId = Integer.parseInt(parts[2]);

                  VectorClock receivedClock = parseVectorClock(vectorString);

                  vcl.updateClock(receivedClock);

                  System.out.println("Client" + senderId + ": " + messageContent + ":" + vcl.showClock());
                  System.out.println("Current clock: " + vcl.showClock());
              }
          }
      } catch (IOException e) {
          e.printStackTrace();
      }

    displayMessage(null);

  }

    private VectorClock parseVectorClock(String vectorString) {
        String cleanString = vectorString.replaceAll("[\\[\\]]", "");
        String[] clockValues = cleanString.split(",\\s*");

        int length = clockValues.length;
        VectorClock tempClock = new VectorClock(length);

        for (int i = 0; i < length; i++) {
            try {
                int val = Integer.parseInt(clockValues[i].trim());
                tempClock.setVectorClock(i, val);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return tempClock;
    }

// TODO:
/*
    This method should print out the message (e.g. Client 1: Hello World!: [1, 0, 0]) and update
    the vector clock without ticking on receive. Then it should display the the updated vector clock.
    Example: Initial clock [0,0,0], updated clock after message from Client 1: [1, 0, 0]
*/
  private void displayMessage(Message message) {

  }
}
