import java.io.Console;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Scan {
    public static class MyThread extends Thread {
        @Override
        public void run() {

        }
    }

    public static class Read extends Thread{
        @Override
        public void run(){

        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            while (true){
                System.out.println("Hello, World!");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            Scanner scan = new Scanner(System.in);
            while (true){
                if (scan.hasNext()){
                    String[] userCommand = scan.nextLine().trim().split(" ", 2);
                    System.out.println(userCommand);
                }
            }
        }).start();
    }
}

