package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class Wget implements Runnable {
    private final String url;
    
    private final int speed;
    
    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }
    
    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var file = new File("tmp.xml");
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[100];
            int bytesRead;
            int bytesTotal = 0;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                output.write(dataBuffer, 0, bytesRead);
                var downloadAt = System.currentTimeMillis();
                bytesTotal += bytesRead;
                if (bytesTotal >=  speed) {
                    var downloadTime = downloadAt - startAt;
                    System.out.println("Download: " +  bytesTotal + " >= " + speed + " bytes. Download time: " + downloadTime + " ms");
                    if (downloadTime < 1000) {
                        long sleepTime = 1000 - downloadTime;
                        System.out.println("Sleep time: " + sleepTime + " ms");
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    bytesTotal = 0;
                    startAt = System.currentTimeMillis();
                }
            }
            System.out.println("Download complete: " + Files.size(file.toPath()) + " bytes");
        }   catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        validateParams(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread thread = new Thread(new Wget(url, speed));
        thread.start();
        thread.join();
    }

    private static void validateParams(String[] args) {
          if (args.length != 2) {
              throw new IllegalArgumentException(
                      "Invalid number of arguments. Expected 2 parameters: URL and speed (bytes/ms)."
              );
          }
          String url = args[0];
          if (url.isBlank()) {
              throw new IllegalArgumentException(
                      "Invalid first argument: " + args[0] + ". URL cannot be empty."
              );
          }
          if (!url.startsWith("http://") && !url.startsWith("https://")) {
              throw new IllegalArgumentException(
                      "Invalid first argument: " + args[0] + ". URL must start with http/https."
              );
          }
          try {
              new URL(url).toURI();
          } catch (Exception ex) {
              throw new IllegalArgumentException(
                      "Invalid first argument: " + url + ". Invalid URL format."
              );
          }
          try {
              int speed = Integer.parseInt(args[1]);
              if (speed <= 0) {
                  throw new IllegalArgumentException(
                       "Invalid second argument: " + speed + ". Speed must be integer number greater than 0."
                  );
              }
          } catch (NumberFormatException ex) {
              throw new IllegalArgumentException(
                      "Invalid second argument: " + args[1] + ". Speed must be integer number."
              );
          }
    }
}
