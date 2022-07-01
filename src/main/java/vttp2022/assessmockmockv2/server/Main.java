package vttp2022.assessmockmockv2.server;

public class Main {
    public static void main( String[] args ) {
		 // Should check if parameter is present
		 HttpServer server = new HttpServer(args);
		 server.start();
    }
}