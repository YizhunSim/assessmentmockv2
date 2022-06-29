package vttp2022.assessmockmockv2.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

// Open/Active connection within the client and server
public class Session implements Runnable{
  private Repository repository;
  private Socket socket;

  private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;

	private final String GET = "GET";
  private final String SUCCESS200 = "HTTP/1.1 200 OK\r\n\r\n";
  private final String SENDSTATUS405 = "HTTP/1.1 405 Method Not Allowed\r\n\r\n";
  private final String SENDSTATUS404 = "HTTP/1.1 404 Not Found\r\n\r\n";
	private final String ADDCONTENTTYPEIMAGE = "Content-Type: image/png\r\n\r\n";
	private final String PNGEXTENSION = ".png";

  public Session(Repository repository, Socket socket){
    this.repository = repository;
    this.socket = socket;
  }

	public void start() throws IOException {
		System.out.println("checkDocPath: " + this.repository.getAbsolutePath());
		if (!isValidDocPath(this.repository)){
			System.err.println("Server Exiting...");
			System.exit(1);
		} else{
				initializeStreams(this.socket);
				// GET<space>/index.html<space>HTTP/1.1
				String request = read();

				System.out.printf("Received request: %s\n", request);

				// Perform some operation on the request
				request = parseRequestResponse(request);

				//HTTP/1.1<space>200<space>OK\r\n
				// \r\n
				// <contents of index.html in bytes>
				System.out.printf("Response to be send to client: %s\n", request);

				write(request);

				close();
		}

	}

	 private String parseRequestResponse(String request) throws IOException{
    // GET<space>/index.html<space>HTTP/1.1
    String[] requestTerms = request.split(" ");
    String restAPICommand = requestTerms[0];
    String resourceFilePath = requestTerms[1];
    String restProtocol = requestTerms[2];
    String response = "";
    switch (restAPICommand){
      case GET:
        response = parseGetRequest(restAPICommand, resourceFilePath, restProtocol);
        break;

      default:
        response = SENDSTATUS405 + restAPICommand + " not supported\r\n";
    }
    return response;
  }

  private String parseGetRequest(String restCommand, String resourcePath, String httpProtocol){
    String result = "";
    String extension = "";

    // System.out.println("getResponse - restCommand: " + restCommand);
    // System.out.println("getResponse - resourcePath: " + resourcePath);
    // System.out.println("getResponse - httpProtocol: " + httpProtocol);
    int indexOfResourceExtension = resourcePath.lastIndexOf('.');
    System.out.println("Index of resource: " + indexOfResourceExtension);
    if (indexOfResourceExtension > 0) {
      extension = resourcePath.substring(indexOfResourceExtension);
      System.out.println("extension: " +extension);
    }

		// If the resource name is /, replace it with /index.html before
		// performing the file search
    if (resourcePath == "/"){
      resourcePath = "/index.html";
    }

		String resourceAbsolutePath = this.repository.getAbsolutePath().concat(resourcePath);

		if (this.repository.isResouceFound(resourcePath)){
			if (extension.equals(PNGEXTENSION)){
				  result = SUCCESS200 + ADDCONTENTTYPEIMAGE + parseResourceContentsToBytes(resourceAbsolutePath).toString();
			} else{
					result = SUCCESS200 + parseResourceContentsToBytes(resourceAbsolutePath).toString();
			}
		} else{
			 result = SENDSTATUS404 + resourcePath + " not found\r\n";
		}

    return result;
  }

  private byte[] parseResourceContentsToBytes(String path){
    try{
      byte[] array = Files.readAllBytes(Paths.get(path));
      if (array != null){
        return array;
      } else{
        return null;
      }
    } catch(IOException ex){
        System.out.println(ex.getMessage());
        return null;
    }
  }

  public boolean isValidDocPath(Repository repo){
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
