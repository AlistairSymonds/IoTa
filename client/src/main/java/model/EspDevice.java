package model;

import java.net.Socket;

public class EspDevice implements Runnable {

  private Socket connection;
  
  public EspDevice(Socket connection){
    this.connection = connection;
  }
  
  public void run() {
    // TODO handle data in and out

  }

}
