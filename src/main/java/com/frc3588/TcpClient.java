/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc3588;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

/**
 *
 * @author davegoldy
 */
public class TcpClient {
    private static final Logger logger = Logger.getLogger(TcpClient.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Socket s = null;
        try {
            int serverPort = 7896;
            s = new Socket("Daves-New-iMac.local", serverPort);
            InputStream anInputStream = s.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(anInputStream));
            PrintStream out = new PrintStream(s.getOutputStream());
            for (int i = 0; i < 5; i++) {
                logger.debug("Sending data.");
                out.println("Daves-New-iMac.local");
                logger.debug("Ready to receive data.");
                String data = in.readLine();
                logger.debug("Received: " + data);
            }
            out.println("quit");
        } catch (UnknownHostException e) {
            logger.error("Sock:", e);
        } catch (EOFException e) {
            logger.error("EOF:", e);
        } catch (IOException e) {
            logger.error("IO:", e);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    logger.error("close:", e);
                }
            }
        }
    }
}
