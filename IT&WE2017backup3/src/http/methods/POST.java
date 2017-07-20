package http.methods;


import java.io.OutputStream;
import java.util.ArrayList;
import json.JsonSerializer;

public class POST extends GET{

	@SuppressWarnings("unused")
	private OutputStream os;
	//whole request string
	private String keeper;
	//splitted request string
	private String[] content;
	//head part 
	private String[] head;
	//body part
	private String[] body;
	//only data from body
	private String data;
	//uploaded document username
	private String user;
	//uploaded document title
	private String title;
	//JSON
	private JsonSerializer jsonSer;
	//ArrayList with Lines
	private ArrayList<String> list;
	//Id for Coords List
	private int id;
	
	//create new GET object and set objects
	public POST(OutputStream os, String keeper, JsonSerializer jsonSer, ArrayList<String> list){
		//set dummy request string in GET
		super(new String[]{"null","null"},os,null, null);
		this.os = os;
		this.keeper = keeper;
		this.jsonSer = jsonSer;
		this.list = list;
	}
	
	//handle post requests
	public boolean post(){
		//divide message into array (head and body)
		if(keeper != null && keeper.contains("\r\n\r\n")){
			content = keeper.split("\r\n\r\n");
			//divide head into array of tags
			head = content[0].split("\r\n");
			//get body data in string
			data = keeper.substring(keeper.indexOf("\r\n\r\n"));
			//if no body is posted
			if(content.length  < 2){
				//fill array to 2 entries
				String[] temp = {content[0],"empty"};
				content = temp;
				title = "/" + user;
			}
			//get user address and proceed or return by special method (if cases)
			for(String s : head) {
				if(s.contains("Host"))user = (s.substring(5).trim());
				if(s.contains("filename:"))title = (s.substring(8).trim());
				if(s.contains("todoadd"))return todoadd();
				if(s.contains("login")) return login();
				if(s.contains("autentificate"))return autentificate();
				if(s.contains("/canvas_save")) return canvas_save(s);
			}
			//multipart/formdata case, set title to filename or user
			if(data.contains("filename"))findFilename();
			//empty filename, set resource for GET request
			else title = "/" + user;
			super.length = content[1].length();
			//set request string in GET (superclass) to POST and call response 
			super.request[1] = "POST";
			super.file = "src/http.root/uploadSuccess.html";
			return super.response();
		}
		return false;
	}
	
	//read sent lines to server array and send ArrayIndex +1 as Id back
	public boolean canvas_save(String s){
		title = "coords";
		super.body = "{\"Id\":\"" + list.size() + "\"}";
		if(s.contains("=")){
			try{
				id = Integer.parseInt(s.substring(s.indexOf("=") +1, s.indexOf("H") -1));
				if(!(id > list.size())){
					title = "coordsId";
					super.body = "{\"Id\":\"" + id + "\"}";
				}
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
		super.file = "canvas_save";
		super.request[1] = "POST";
		return super.response();
	}
	
	//check autentification 
	public boolean autentificate(){
		title = "autentificate";
		//login test: stored log in json = received json, answer by GET superclass
		if(jsonSer.readFile("src/http.root/autentificate.txt").equals(data.trim())){
			super.file = "autentificated";
			super.request[1] = "POST";
			//this is the access data
			super.body = "{\"message\":\"autentification ok\"}";
			return super.response();
		}else{
			super.file = "not autentificated";
			super.request[1] = "POST";
			//this is the access data
			super.body = "{\"message\":\"autentification failed\"}";
			return super.response();
		}
	}
	
	//check login information
	public boolean login(){
		title = "login";
		//login test: stored log in json = received json, answer by GET superclass
		if(jsonSer.readFile("src/http.root/login.txt").equals(data.trim())){
			super.file = "logged in";
			super.request[1] = "POST";
			//this is the access data
			super.body = "{\"id\":\"1234\"}";
			return super.response();
		}
		return false;
	}
	
	//called by post(), set title and save todo element in data, also send answer by GET superclass
	public boolean todoadd(){
		String todo = "go to bed";
		//extract todo task
		if(data.contains("todo")) todo = data.substring(data.indexOf("=")).trim();
		//trim todo task
		data = todo.substring(todo.indexOf("=")+1, todo.indexOf("MAX_FILE_SIZE")-1);
		//set title for handler in ClientThread class
		title = "todo";
		//response
		super.file = "src/http.root/uploadSuccess.html";
		super.length = todo.length();
		super.request[1] = "POST";
		return super.response();	
	}
	
	//extract filename from posted element, this method should handle only multipart/formdata posts
	public void findFilename(){
		body = data.split("\r\n");
		for(String s : body) 
			if(s.contains("filename")){
				title = "/" + (s.substring(s.indexOf("filename")).trim());
				if(title.contains("\"")){
					String temp = title.replace("\"", "");
					title = temp;
				}
				if(title.contains("=")){
					String temp = title.replace("=", "");
					title = temp;
				}
				if(title.contains("filename")){
					String temp = title.replace("filename", "");
					title = temp;
				}
			}
	}

	//all methods called from ClientThread class to extract information
	public String getUser() {
		return user;
	}

	public String getTitle(){
		return title;
	}
	
	public String[] getContent(){
		return content;
	}

	public String getData(){
		return data;
	}
	
	public int getId(){
		return id;
	}

}
