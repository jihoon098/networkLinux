package kr.co.itcen.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {

	   private static final int PORT = 8000;

	   public static void main(String[] args) {
	      ServerSocket serverSocket = null;

	      try {
	         InetAddress inetAddress = InetAddress.getLocalHost();
	         serverSocket = new ServerSocket();
	         InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, PORT);

	         System.out.println(inetAddress.getHostAddress());
	         serverSocket.bind(inetSocketAddress);

	         Socket socket = serverSocket.accept();
	         InetSocketAddress remoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
	         int remoteFromHost = remoteSocketAddress.getPort();

	         try {
	            InputStream is = socket.getInputStream();
	            OutputStream os = socket.getOutputStream();

	            while(true) {
	               byte[] buffer = new byte[256];
	               int readByteCount = is.read(buffer);
	               if (readByteCount == -1) {
	                  System.out.println("[TCPServer] closed by client");
	                  break;
	               }
	               String data = new String(buffer, 0, readByteCount, "UTF-8");
	               os.write(data.getBytes("UTF-8"));
	               //               System.out.println("[TCPServer] received:" + data);


	            }

	         } catch (SocketException e) {
	            System.out.println(e);
	         } catch (IOException e) {
	            System.out.println(e);
	         } finally {
	            if(socket != null && socket.isClosed() == false) {
	               socket.close();
	            }
	         }
	      } catch (IOException e) {
	         e.printStackTrace();
	      } finally {


	         try {
	            //1. 서버소켓 생성
	            serverSocket = new ServerSocket();

	            //2. Binding
	            InetAddress inetAddress = InetAddress.getLocalHost();
	            InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, PORT);
	            serverSocket.bind(inetSocketAddress);
	            log("binding " + inetAddress.getHostAddress() + ":" + PORT);

	            //3. accept
	            while(true) {
	               Socket socket = serverSocket.accept();
	               new EchoServerReceiveThread(socket).start();
	            }

	         } catch (IOException e) {
	            e.printStackTrace();
	         } finally {
	            //8. Server Socket 자원정리

	            try {
	               if(serverSocket != null && serverSocket.isClosed() == false) {
	                  serverSocket.close();
	               }
	            } catch (IOException e) {
	               e.printStackTrace();
	            }
	         }
	      }

	   }

	   public static void log(String log) {
	      System.out.println("[Echo Server#" + Thread.currentThread().getId() + "] " + log);
	   }
	}