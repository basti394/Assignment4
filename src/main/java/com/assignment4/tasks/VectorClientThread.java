package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;
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

                  int senderIndex = senderId - 1;

                  VectorClock receivedClock = parseVectorClock(vectorString);
                  Message newMessage = new Message(messageContent, receivedClock, senderIndex);

                  if (vcl.checkAcceptMessage(senderIndex, receivedClock)) {
                      displayMessage(newMessage);

                      checkBuffer();
                  } else {
                      System.out.println("Buffered Message " + messageContent + " with clock: " + receivedClock.showClock());
                      buffer.add(newMessage);
                  }
              }
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

    private void checkBuffer() {
        boolean deliveredSomething = true;

        while (deliveredSomething) {
            deliveredSomething = false;
            Iterator<Message> iterator = buffer.iterator();

            while (iterator.hasNext()) {
                Message msg = iterator.next();
                if (vcl.checkAcceptMessage(msg.getSenderID(), msg.getClock())) {
                    displayMessage(msg);
                    iterator.remove();
                    deliveredSomething = true;
                }
            }
        }
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
      vcl.updateClock(message.getClock());

      int displayId = message.getSenderID() + 1;

      System.out.println("Client " + displayId + ": " + message.getMessage() + ": " + message.getClock().showClock());
      System.out.println("Current clock: " + vcl.showClock());
  }
}
