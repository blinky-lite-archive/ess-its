package se.esss.litterbox.beaglebonesockettest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
//got code from http://mrbool.com/communicating-node-js-and-java-via-sockets/33819


public class NodeJsEcho 
{
	private Socket socket = null;
	private Socket getSocket() {return socket;}

	private void socketConnect(String ip, int port) throws UnknownHostException, IOException 
	{
		System.out.println("[Connecting to socket...]");
		this.socket = new Socket(ip, port);
	}

	public String echo(String message) throws Exception
	{
		PrintWriter out = new PrintWriter(getSocket().getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));


		// writes str in the socket and read
		out.println(message);
		String returnStr = in.readLine();
		return returnStr;
	}

	public static void main(String[] args) throws Exception 
	{
		// class instance
		NodeJsEcho client = new NodeJsEcho();


		// socket tcp connection
		String ip = "127.0.0.1";
		int port = 6969;
		client.socketConnect(ip, port);


		// writes and receives the message
		String message = "message123";


		System.out.println("Sending: " + message);
		String returnStr = client.echo(message);
		System.out.println("Receiving: " + returnStr);
	}
}
