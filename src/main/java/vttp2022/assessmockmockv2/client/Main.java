package vttp2022.assessmockmockv2.client;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException{
    HttpClientConnection client = new HttpClientConnection(args);

    client.start();
  }
}
