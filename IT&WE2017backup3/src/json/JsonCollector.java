package json;

import java.time.LocalDateTime;
import java.util.HashMap;
public class JsonCollector extends HashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4281933782407823951L;
	
	//Metadata
	private LocalDateTime date;
	
	public JsonCollector(){
		setDate(LocalDateTime.now());
	}
	
	public void addObject(String key, Object obj){
		this.put(key, obj);
		setDate(LocalDateTime.now());
	}
	
	public Object getObject(String key){
		return this.get(key);
	}
	
	public void printAll(){
		this.forEach((k,v) -> System.out.println(k+ " " +v));	
	}
	
	public HashMap<String, Object> getAll(){
		return this;
	}
	
	public void setAll(HashMap<String, Object> map){
		this.clear();
		this.putAll(map);
		setDate(LocalDateTime.now());	
	}

	//get Metadata
	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}