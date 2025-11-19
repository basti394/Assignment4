package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

// This Class handles the continuous listening for incoming messages from the server
public class LTClientThread implements Runnable {

    private final DatagramSocket clientSocket;
    private final LamportTimestamp lc;
    byte[] receiveData = new byte[1024];

    public LTClientThread(DatagramSocket clientSocket, LamportTimestamp lc) {
        this.clientSocket = clientSocket;
        this.lc = lc;
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
                    int receivedTimestamp = Integer.parseInt(parts[1]);
                    int senderId = Integer.parseInt(parts[2]);

                    lc.updateClock(receivedTimestamp);

                    System.out.println("Client " + senderId + ": " + messageContent + ":" + lc.getCurrentTimestamp());
                    System.out.println("Current clock: " + lc.getCurrentTimestamp());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}