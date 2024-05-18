package com.example.myapplication.chapter10.mediaplayers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPExample {
    private void udpserver() throws IOException {
        byte[] data = new byte[1024];
        DatagramPacket dp = new DatagramPacket(data, data.length);

        DatagramSocket socket = new DatagramSocket(7777);
        socket.receive(dp);


        byte[] datareceved = dp.getData();

        String content = new String(datareceved, 0, datareceved.length);

        socket.close();

    }
}
