package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import http.methods.GET;
import http.methods.POST;
import json.JsonSerializer;

public class ClientThread extends Thread{

	//create socket 
	private Socket socket;
	private JsonSerializer jsonSer;
	private ServerTCP server;
	LocalDateTime now;
	
	//apply the socket obj passed from ServerTCP to the local socket obj
	public ClientThread(Socket socket, JsonSerializer jsonSer, ServerTCP server){
		this.socket = socket;
		this.jsonSer = jsonSer;
		this.server = server;
		now = LocalDateTime.now();
	}
	
	@Override
	public void run() {
		System.out.print("\n\nNew Client created: ");
		System.out.println(socket.getInetAddress());
		//create in- and output streams on existing socket
		try(OutputStream os = socket.getOutputStream();
			InputStream in = socket.getInputStream()){
			//cancel connection if estimated input is too much
			if(in.available() > 100000){
				socket.close();
				System.out.println("safety close");
				return;
			}
			byte[] bin = new byte[in.available()];
			in.read(bin);
			String[] request = new String(bin).split(" ");
			System.out.println("ServerClient: \n" + new String(bin));
//			for(String s : request) System.out.println(s);
			
			//handle request
			if(request.length != 0){
				switch (request[0].trim()){
					case "GET" : GET get = new GET(request, os, jsonSer, server);
								 if(get.response()) System.out.println("GET sent");
								 else System.out.println("GET failed");
								 break;
					case "POST": POST post = new POST(os, new String(bin), jsonSer, server.getList());
								 String title = null;
								 //check title and safe POST object to a matching Data-Structure in Server object
								 if(post.post()){
									 title = post.getTitle();
									 //save to todolist
									 if(title.equals("todo"))server.setTodo(post.getData());
									 //add coords in server (list)
									 else if(title.equals("coords")) server.addLine(post.getData().trim());
									 else if(title.equals("coordsId")) server.replaceLine(post.getData().trim(), post.getId());
									 //save posted object by name and content in server (userMap)
									 else if(title.equals("login")) System.out.println("login");
									 else server.addUser(title, post);
								 }
								 else System.out.println("post failed");
								 break;
					default    : @SuppressWarnings("unused") GET getBadRequest = new GET(null, os, null, null);
				}
			}
			//close socket
			socket.close();
			System.out.println("closing socket: " + socket.isClosed());
		}catch(IOException e1) {
			e1.printStackTrace();
			System.err.println("Reading failed..");
		}	
	}
}
