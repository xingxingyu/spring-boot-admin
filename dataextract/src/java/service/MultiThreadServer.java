package service;


import thread.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dylan on 2017/5/7.
 */
public class MultiThreadServer {
    private int port = 33582;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private final int POOL_SIZE = 2;


    public MultiThreadServer() throws IOException {
        serverSocket = new ServerSocket(port);
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * POOL_SIZE);
        System.out.println("服务已启动");
    }

    public void service() {
        while (true) {
            Socket socket ;
            try {
                socket = serverSocket.accept();
                executorService.execute(new Handler(socket));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new MultiThreadServer().service();
    }

}
