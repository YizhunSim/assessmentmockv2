package vttp2022.assessmockmockv2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
  private int port = 3000;
  private String repoDirectory = "static";

  private final String PORT_COMMAND = "--port";
  private final String DOCROOT_COMMAND = "--docRoot";

  public HttpServer(String[] args){
    if (args.length > 0){
      // Transform my String[] args into a ArrayList of string
      List<String> argsList = new ArrayList<>(Arrays.asList(args));

      // java -cp ./myserver.jar
                                  //String[] args
                                  // 0.
                                  // NIL -> port 3000 docRoot static

                                  // 1.
                                  //    0     1
                                  // --port 8080

                                  // 2.
                                  //    0               1
                                  // --docRoot ./target:/opt/tmp/www

                                  // 3.
                                  //    0     1       2             3
                                  // --port 8080 --docRoot ./target:/opt/tmp/www

                                  // 4.
                                  //    0               1               2     3
                                  // --docRoot ./target:/opt/tmp/www --port 8080

      if (argsList.contains(PORT_COMMAND)){
       int indexOfPortValue = argsList.indexOf(PORT_COMMAND) + 1;
        // 3.                         2                      + 1 = 3

        //  args[1] - 8080
        // System.out.println("IndexOfPortValue: " + indexOfPortValue);
        // System.out.println("portValue: "+ args[indexOfPortValue]);
       this.port = Integer.parseInt(args[indexOfPortValue]);
      }
      if (argsList.contains(DOCROOT_COMMAND)){
        // 3.                                        2              + 1 = 3
        int indexOfDocRootValue = argsList.indexOf(DOCROOT_COMMAND) + 1;

        // args[3] - ./target:/opt/tmp/www
        // System.out.println("indexOfDocRootValue: " + indexOfDocRootValue);
        // System.out.println("DocRootValue: " + args[indexOfDocRootValue]);
        this.repoDirectory = args[indexOfDocRootValue];
      }
    }
  }

  public void start() {
		try {
      ServerSocket server = new ServerSocket(port); // Object - server
		  ExecutorService threadPool = Executors.newFixedThreadPool(3);

      // ******Object***** - Instiantiation from a class (Blueprint DNA)
      // Class
      // 1. Fields
      // 2. Constructor
      // 3. Methods (public/private/protected)

      // Access Modifier - It gives the visibility or security permission to
      // how you want that field or methods to be access by other classes/object

      // Type
      // void, String, int, Boolean, List<String>,

      // Method name

      // Method Arguements (String arg1, int, arg2)

      // Methods Body {
      //  ....
      // }


			while (true) {
				System.out.println("Waiting for client connection");
				Socket sock = server.accept(); // Hang and wait for client connection

				System.out.println("Starting client connection handler");

				// Create a repository
				Repository repo = new Repository(repoDirectory); // Object - repo
				Session sess = new Session(repo, sock);  // Object - sess
				threadPool.execute(sess);
			}

		} catch (IOException ex) {
			System.err.println("Server error, exiting");
			ex.printStackTrace();
		}
	}
}
