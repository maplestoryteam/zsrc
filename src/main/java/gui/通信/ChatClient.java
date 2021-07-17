package gui.通信;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {

    private static final String M_IP = "123.207.53.97";

    private static final Integer M_HOST = 7033;

    private DataOutputStream dos = null;

    public static void main(String[] args) {

        ChatClient client = new ChatClient();
        client.start();

    }

    private void start() {

        Socket socket = null;

        try {
            socket = new Socket(M_IP, M_HOST);
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            System.out.println("ERROR:" + " 未知的连接地址，请稍后再试…");
            System.exit(0);
        } catch (ConnectException e) {
            System.out.println("ERROR:" + "网络异常，请稍后再试…");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        String line = null;
        do {
            //System.out.print("");
            line = scanner.nextLine();
            try {
                dos.writeUTF(line);
                dos.flush();
            } catch (SocketException e) {
                System.out.println("ERROR:" + "网络异常，请稍后再试…");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!"bye".equalsIgnoreCase(line));

        try {
            if (scanner != null) {
                scanner.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
