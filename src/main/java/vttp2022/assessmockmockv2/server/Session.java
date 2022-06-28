package vttp2022.assessmockmockv2.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

// Open/Active connection within the client and server
public class Session implements Runnable{
  private Repository repository;
  private Socket socket;

  private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;

  public Session(Repository repository, Socket socket){
    this.repository = repository;
    this.socket = socket;
  }

	public void start() throws IOException {
		System.out.println("checkDocPath: " + this.repository.getAbsolutePath());
		if (!isValidDocPath(this.repository)){
			System.exit(1);
		} else{
				initializeStreams(this.socket);
		}

	}

  public boolean isValidDocPath(Repository repo){
		System.out.println("Executing isValidDocPath");
		boolean isValid = true;

		if (!repo.isPathExist()){
			isValid = false;
			System.err.println("Path do not exists");
		}
		if (!repo.isPathDirectory()){
			isValid = false;
			System.err.println("Path is not a directory");
		}
		if (!repo.isPathReadable()){
			isValid = false;
			System.err.println("Path is not readable by the server");
		}

		return isValid;
  }

	private void initializeStreams(Socket sock) throws IOException {
		is = sock.getInputStream();
		ois = new ObjectInputStream(is);
		os = sock.getOutputStream();
		oos = new ObjectOutputStream(os);
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

	@Override
	public void run() {
		try{
			start();
		} catch(IOException ex){
			ex.printStackTrace();
		}
	}
}
