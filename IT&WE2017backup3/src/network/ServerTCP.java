package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import http.methods.*;
import json.JsonSerializer;

public class ServerTCP{
	//create here all Objects and distribute them with live time of the Server instance
	//the JSON Serializer
	private final JsonSerializer jsonSer;
	//non persistent storage for POST Object
	private final HashMap<String,POST> userMap;
	//non persistent storage for the todo List
	private String todo;
	//count todo entries
	private int count;
	//the date-of-change values
	private LocalDateTime dateMap;
	private LocalDateTime dateList;
	//central storage for line coordinates from ClientThreads
	private final ArrayList<String> coords;
	//thread safety for multi-user storage
	private final ReentrantReadWriteLock lock;
	private final Lock readLock, writeLock;
	
	public ServerTCP(JsonSerializer jsonSer){
		this.jsonSer = jsonSer;
		todo = "";
		count = 1;
		//last time of change in userMap
		dateMap = LocalDateTime.now();
		//last time of change in coords
		dateList = LocalDateTime.now();
		userMap = new HashMap<String, POST>();
		coords = new ArrayList<String>(); 
		lock = new ReentrantReadWriteLock(true);
		readLock = lock.readLock();
		writeLock = lock.writeLock();
		server();
	}
	
	//return the line coordinates, thread safe (redundant)
	public ArrayList<String> getList(){
		readLock.lock();
		try{
			return coords;
		}finally{
			readLock.unlock();
		}
	}
	
	//multiple clients can add their line coords, thread safe with fair policy
	public boolean addLine(String line){
		writeLock.lock();
		boolean bln = false;
		try{//prevent duplication
			if(!coords.isEmpty()){
				if(!coords.get(coords.size() -1).equals(line)) coords.add(line);
			}else coords.add(line);
			bln = true;
		}
		finally{
			writeLock.unlock();
		}
		return bln;
	}
	
	//multiple clients can add their line coords, thread safe with fair policy
	public boolean replaceLine(String line, int id){
		writeLock.lock();
		boolean bln = false;
		try{//prevent duplication
			if(!coords.isEmpty()){
				String temp = coords.get(id) + ",";
				coords.set(id,temp + line);
				bln = true;
			}
		}
		finally{
			writeLock.unlock();
		}
		return bln;
	}
	
	//return list with POST objects
	public HashMap<String, POST> getMap(){
		return userMap;
	}
	
	//return single POST object by key(filename or username/ ip adress)
	public POST getUser(String user){
		return userMap.get(user);
	}
	
	//add name and entry from ClientThread object (else case in POST class), thread safe
	public synchronized void addUser(String title, POST post){
		userMap.put(title, post);
		dateMap = LocalDateTime.now();
	}
	
	//returns the todo list and concats a date
	public String getTodo() {
		return todo + "<br><br>Last update: " + dateList;
	}

	//enter todo entry if POST matches "todo", thread safe 
	public synchronized void setTodo(String todo) {
		this.todo += count + ": " + todo + "<br>";
		dateList = LocalDateTime.now();
		count++;
	}
	
	//returns just a date
	public LocalDateTime getMapDate(){
		return dateMap;
	}
	
	private void server(){
		//create server socket with PORT Nr
		try(ServerSocket servSock = new ServerSocket(8080)){
			System.out.println("Server listens...");
			//continuous listening for incoming calls
			while(true){
					//do not use try with resources     try(...){...}
				    //otherwise the socket will be closed immediately after passing to the threaded class
				    //causes socket exception
					try{
						//create client socket for handling an incoming call, accept() provides a socket object
						Socket socket = servSock.accept();
						System.out.println("Server accepted...");
						// instantiate new ClientThread object and passes the socket object
						ClientThread ct = new ClientThread(socket, jsonSer, this);
						// create new Thread for ClientThread obj and start
						new Thread(ct).start();
					}catch(Exception e1){
						e1.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
