package http.methods;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;

import json.JsonSerializer;
import network.ServerTCP;

public class GET {

	private OutputStream os;
	//request string from ClientThread class
	protected String[] request;
	//status information for header
	private int statusCode;
	private String status;
	protected long length;
	private String type;	//application/octet startet immer download im browser, applccation/json, application/javascript
	private LocalDateTime date;
	//body 
	protected String body;
	//JSON
	private JsonSerializer jsonSer;
	private ServerTCP server;
	//resource path
	protected String file;
	
	//set all status header to default
	public GET(String[] request, OutputStream os, JsonSerializer jsonSer, ServerTCP server){
		this.os = os;
		this.request = request;
		statusCode = 404;
		status = "Page Not Found";
		length = 0;
		type = "";
		date = LocalDateTime.now();
		this.jsonSer = jsonSer;
		this.server = server;
	}
	
	//request handler
	public boolean response(){
			//unknown request
			if(request == null){
				statusCode = 400;
				status = "Bad Request";
				return send("");
			//call from POST subclass ("file" is set in POST)
			}else if(request[1].equals("POST")){
				type = "text/html";
				//login() call from POST, send access data to local browser storage
				if(file.equals("logged in")){
					length = body.length();
					type = "application/json";
					statusCode = 200;
					status = "OK";
					return send(body);
				//same as login() just automatic autentification
				}else if(file.equals("autentificated")){
					length = body.length();
					type = "application/json";
					statusCode = 200;
					status = "OK";
					return send(body);
				}else if(file.equals("not autentificated")){
					length = body.length();
					type = "application/json";
					statusCode = 403;
					status = "Forbidden";
					return send(body);
				}else if(file.equals("canvas_save")){
					length = body.length();
					type = "application/json";
					statusCode = 200;
					status = "OK";
					return send(body);
				}
			//html requested
			}else if(request[1].contains("index.html") || request[1].trim().equals("/") && request[2].contains("HTTP")){
				file = "src/http.root/index.html";
				type = "text/html";
			//text requested
			}else if(request[1].contains("test.txt")){
				file = "src/http.root/test.txt";
				type = "text/plain";
			//json requested
			}else if(request[1].contains("json")){
				body = jsonSer.getString();
				type = "application/json";
				statusCode = 200;
				status = "OK";
				length = body.length();
				return send(body);
			//upload requested
			}else if(request[1].contains("upload")){
				file = "src/http.root/upload.html";
				type = "text/html";
			//find and resource in non persistent storage (formerly posted data) 
			}else if(server.getMap().containsKey(request[1])){
				body = server.getUser(request[1]).getData();
				type = "text/plain";
				statusCode = 200;
				status = "OK";
				length = body.length();
				return send(body);
			//todoadd requested
			}else if(request[1].contains("todoadd")){
				file = "src/http.root/todoadd.html";
				type = "text/html";
			//todoget requested (add http footer to output stream)
			}else if(request[1].contains("todoget")){
				file = "src/http.root/todoget.html";
				type = "text/html, text/plain";
				try(FileInputStream fis = new FileInputStream(file)){
					byte[] b = new byte[fis.available()];
					while(fis.available() > 0)fis.read(b);
					body = new String(b);
					statusCode = 200;
					status = "OK";
					length = body.length();
					return send(body + server.getTodo() + "</body>\r\n" + "</html>");
				} catch (IOException  e) {
					e.printStackTrace();
				} 
			//send canvas.html
			}else if(request[1].equals("/canvas")){
				file = "src/http.root/canvas.html";
				type = "text/html";
			}else if(request[1].equals("/canvaslist")){
				file = "src/http.root/canvaslist.html";
				type = "text/html";
			//answer JS request for canvas ids
			}else if(request[1].equals("/getlist")){
				body = "{\"canvasIDs\":\"" + server.getList().size() + "\"}";
				type = "application/json";
				statusCode = 200;
				status = "OK";
				length = body.length();
				return send(body);
			//call method to send requested canvas
			}else if(request[1].contains("/getcanvas")){
				return transmitCanvas();
			//send 404Error if no match
			}else return send("");
			//read file to send
			try(FileInputStream fis = new FileInputStream(file)){
				byte[] b = new byte[fis.available()];
				while(fis.available() > 0)fis.read(b);
				body = new String(b);
				statusCode = 200;
				status = "OK";
				length = body.length();
			} catch (IOException  e) {
				e.printStackTrace();
			} 
		return send(body);
	}
	//send answer string with content modified in request handler
	public boolean send(String body){
		String answer = "HTTP/1.0 " + statusCode + status + "\r" + 
				"Connection: close \r" + 
				"Date: " + date + "\r" + 
				"Content-Length: " + length + "\r" +
				"Content-Type: " + type + "\r" +
				"Expires: " + date + "\r" +
				"Cache-control: private" + "\r" + "\n\n" + body ;
		try(OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8")){
			osw.write(answer);
			os.flush();
			return true;	
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}
	
	//build JSON and send all or just a single canvas
	public boolean transmitCanvas(){
		ArrayList<String> list = server.getList();
		type = "application/json";
		statusCode = 200;
		status = "OK";
		StringBuilder stb = new StringBuilder();
		if(!request[1].contains("=")){
			//read all Canvas from server array to JSON String
			if(!list.isEmpty()){
				stb.append("{\"Lines\":[");
				for(int i = 0; i < list.size(); i++){
					stb.append(list.get(i));
					if(i < list.size() -1) stb.append(",");
				}
				stb.append("]}");
			}
		}else{
			try{
				//parse id from request and read from Array to JSON String
				String str = request[1].substring(request[1].indexOf("=") + 1);
				int id = Integer.parseInt(str);
				if(!list.isEmpty() && list.size() > id )
					stb.append("{\"Lines\":[" + list.get(id) + "]}");
			}catch(NumberFormatException | IndexOutOfBoundsException e){
				System.err.println(e);
			}
		}
		body = stb.toString();
		length = body.length();
		return send(body);
	}
	
}
