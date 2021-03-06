package vttp2022.assessmockmockv2.client;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class HttpClientConnection {
	private int port = 80;
	private String host = "localhost";

	private Socket sock;
	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;

	public HttpClientConnection(String[] args){
		if (args.length > 0){
			// Socket -
			// host				port
			// 			0
			// localhost:12345
			String terms[] = args[0].split(":");
			// String terms[] - values inside are string
			// 			0						1
			//		"localhost"		"12345"
			this.host = terms[0];
			this.port = Integer.parseInt(terms[1]);
		}
	}

	public void start() throws UnknownHostException, IOException{
			try{
			System.out.printf("Connecting to %s on port %d...\n", host, port);
			Socket sock = new Socket(host, port);
			System.out.println("Connected");
			initializeStreams(sock);
			Console cons = System.console();

			String input = cons.readLine(">");
			// GET /about.html HTTP/1.1
			// GET /image.png HTTP/1.1
			write(input);

			String response = read();
			System.out.println(response);

			close();
			sock.close();
		} catch (IOException ex){
			System.err.println(ex);
			ex.printStackTrace();
		}
	}

	private void initializeStreams(Socket sock) throws IOException {
		os = sock.getOutputStream();
		oos = new ObjectOutputStream(os);
		is = sock.getInputStream();
		ois = new ObjectInputStream(is);
	}

	private String read() throws IOException {
		return ois.readUTF();
	}

	private void write(String out) throws IOException {
		oos.writeUTF(out);
		oos.flush();
	}

	private void close() throws IOException {
		is.close();
		os.close();
	}

}