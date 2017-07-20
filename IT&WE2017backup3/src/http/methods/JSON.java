package http.methods;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import json.JsonSerializer;

public class JSON {
	
	private OutputStream os;
	private JsonSerializer jsonSer;
	
	public JSON(OutputStream os, JsonSerializer jsonSer){
		this.os = os;
		this.jsonSer = jsonSer;
	}
	
	public boolean sendJSON(){
//		byte[] b = jsonSer.getString().getBytes();
		System.out.println("Writing json..");
		System.out.println(jsonSer.getString());
		OutputStreamWriter buw = new OutputStreamWriter(os);
		try {
			buw.write(jsonSer.getString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean sendJsonFile(){
		//create fileinput stream and read in the element at the location FILEPATH
		try(FileInputStream fis = new FileInputStream("file.txt")){
			System.out.println("Writing json..");
			//create new byte[] with dimension of FILEPATH
			byte[] b = new byte[fis.available()];
			//read the element into byte[]
			while(fis.available() > 0){
				fis.read(b);
			}
			//write the element in byte[] to the output stream
			os.write(b);
			//flush the stream! 
			os.flush();
			return true;
		}catch(IOException e1) {
			e1.printStackTrace();
			System.err.println("Writing failed..");
		}
		return false;
	}
}
