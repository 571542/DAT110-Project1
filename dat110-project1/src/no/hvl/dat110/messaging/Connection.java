package no.hvl.dat110.messaging;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {

	private DataOutputStream outStream; // for writing bytes to the TCP connection
	private DataInputStream inStream; // for reading bytes from the TCP connection
	private Socket socket; // socket for the underlying TCP connection

	public Connection(Socket socket) {

		try {

			this.socket = socket;

			outStream = new DataOutputStream(socket.getOutputStream());

			inStream = new DataInputStream(socket.getInputStream());

		} catch (IOException ex) {

			System.out.println("Connection: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void send(Message message) {
		try {
			outStream.write(message.encapsulate());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Message receive() {

		Message message = new Message();
		byte[] recvbuf = new byte[MessageConfig.SEGMENTSIZE];

		// TODO
		// read a segment from the input stream and decapsulate into message
		int readLength = -1;
			try {
				readLength = inStream.read(recvbuf, 0, MessageConfig.SEGMENTSIZE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		if(readLength != MessageConfig.SEGMENTSIZE) {
			System.out.println("Noke av segmentet er missta! Forventa "+MessageConfig.SEGMENTSIZE+", fekk"+readLength);
		}
		message.decapsulate(recvbuf);
			
		return message;

	}

	// close the connection by closing streams and the underlying socket
	public void close() {

		//TODO
		try {

			outStream.close();
			inStream.close();

			socket.close();
		} catch (IOException ex) {

			System.out.println("Connection: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}