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
 * The TcpClient class assumes the TcpServer is running on the desired host and
 * port. It will send a message to the server and then record in a log file the
 * response from the server. In the future, modify this class to return the data
 * to the caller rather than just writing it to a log file.<br>
 * <br>
 * To use this class, you need to know the name of the server on your network
 * and the port the server is listening on. At this point the code is not set up
 * to handle an IP address.<br>
 *
 * @author Dave Goldy
 */
public class TcpClient
{

    private static final Logger logger = Logger.getLogger(TcpClient.class);

    public static void main(String args[])
    {
        TcpClient aTcpClient = new TcpClient("Daves-New-iMac.local", 7896);
        aTcpClient.setData(
            "{\"children\" : [{\"text\" : \"Mitchell Simoens\",\"children\" : [{\"text\" : \"@SenchaMitch\",\"link\" : \"http://www.twitter.com/SenchaMitch\",\"leaf\" : true},{\"text\" : \"http://www.linkedin.com/in/mitchellsimoens\",\"link\" : \"http://www.linkedin.com/in/mitchellsimoens\",\"leaf\" : true}]},{\"text\" : \"Jay Garcia\",\"children\" : [{\"text\" : \"@ModusJesus\",\"link\" : \"http://www.twitter.com/ModusJesus\",\"leaf\" : true},{\"text\" : \"http://www.linkedin.com/in/tdginnovations\",\"link\" : \"http://www.linkedin.com/in/tdginnovations\",\"leaf\" : true}]},{\"text\" : \"Anthony De Moss\",\"children\" : [{\"text\" : \"@ademoss1\",\"link\" : \"http://www.twitter.com/ademoss1\",\"leaf\" : true},{\"text\" : \"http://www.linkedin.com/in/ademoss\",\"link\" : \"http://www.linkedin.com/in/ademoss\",\"leaf\" : true}]}]}");
        aTcpClient.send();
        logger.debug("Received : " + aTcpClient.getData());
    }

    private String fData; //Holds the data to send to the server.

    private String fHost; //Holds the name of the server.
    private int fPort;    //Holds the port the server is listening on.

    public TcpClient(String host, int port)
    {
        if (port <= 1023 || host == null || host.isEmpty())
        {
            throw new IllegalArgumentException("Both host and port need to be defined.");
        }
        else
        {
            this.fHost = host;
            this.fPort = port;
        }
    }

    /**
     * Get the data returned from the server.
     *
     * @return Data as a string.
     */
    public String getData()
    {
        return fData;
    }

    /**
     * Return the server name.
     *
     * @return host name
     */
    public String getHost()
    {
        return fHost;
    }

    /**
     * Return the port number the server is listening on.
     *
     * @return Port number.
     */
    public int getPort()
    {
        return fPort;
    }

    /**
     * After the host, port and data attributes have been set, use this method
     * to send the data to the server. At the end of this processing the results
     * are available in the getData method.
     */
    public void send()
    {
        Socket s = null;
        try
        {
            s = new Socket(fHost, fPort);
            InputStream anInputStream = s.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(anInputStream));
            PrintStream out = new PrintStream(s.getOutputStream());
            logger.debug("Sending data.");
            out.println(fData);
            logger.debug("Ready to receive data.");
            fData = in.readLine();
            logger.debug("Received: " + fData);
        }
        catch (UnknownHostException e)
        {
            logger.error("Sock:", e);
        }
        catch (EOFException e)
        {
            logger.error("EOF:", e);
        }
        catch (IOException e)
        {
            logger.error("IO:", e);
        }
        finally
        {
            if (s != null)
            {
                try
                {
                    s.close();
                }
                catch (IOException e)
                {
                    logger.error("close:", e);
                }
            }
        }
    }

    /**
     * Set the data to be sent to the server. For now the interface only accepts
     * string data. Use a JSON format if you what to send a structured type of
     * data.
     *
     * @param data
     */
    public void setData(String data)
    {
        this.fData = data;
    }

    /**
     * Set the desired server name. At the current time, you can not set this to
     * an IP address. It must be the DNS name of the server.
     *
     * @param host - must be defined.
     */
    public void setHost(String host)
    {
        if (host == null || host.isEmpty())
        {
            throw new IllegalArgumentException("Host name must be defined");
        }
        this.fHost = host;
    }

    /**
     * Set the desired server port number. This number must be greater than
     * 1023.
     *
     * @param port
     */
    public void setPort(int port)
    {
        if (port <= 1023)
        {
            throw new IllegalArgumentException("The port number must be greater than 1023");
        }
        else
        {
            this.fPort = port;
        }
    }

}
