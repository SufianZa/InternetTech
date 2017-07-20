package json;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JsonSerializer implements IJsonSerialize{
	
	private JsonCollector jsonCol;
	@SuppressWarnings("unused")
	private String type;
	private String content;
	
	public JsonSerializer(){
		jsonCol = new JsonCollector();
	}

	@Override
	public void addString(String key, String str) {
		jsonCol.addObject("{ \"" + key + "\" : ", "\"" + str + "\"}");
	}

	@Override
	public void addInteger(String key, int num) {
		jsonCol.addObject("{ \"" + key + "\" : ", num + "}");
	}

	@Override
	public void addDouble(String key, double num) {
		jsonCol.addObject("{ \"" + key + "\" : ", num + "}");
	}

	@Override
	public void addArray(String key, Map<String, Object> array) {
		String content = checkType(array);
		jsonCol.addObject(String.format("{ \"%1$s\" : ", key), String.format("%1$s }", content));
	}

	@Override
	public String getString() {
		HashMap<String, Object> map = jsonCol.getAll();
		content = "";
		map.forEach((k, v) -> {
			content += k; 
			content += v + "\n";
		});
		content += "LastChange: " + jsonCol.getDate();
		return content;
	}

	@Override
	public void parseString(String str) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String[] splitString = str.split("}");
		for(int i = 0; i < splitString.length -1; i++){
			splitString[i] += "}";
			map.put(splitString[i].substring(splitString[i].indexOf("{"), splitString[i].indexOf(":")), splitString[i].substring(splitString[i].indexOf(":"), splitString[i].indexOf("}")+1));
		}
		jsonCol.setAll(map);
	}

	//Objects stored as Strings in the Map
	//this method parses the string and returns a map with objects of the enclosed type
	@Override
	public Map<String, Object> getObjects() {
		String jString = getString();
		HashMap<String, Object> objectMap = new HashMap<String, Object>();
		String[] splitString = jString.split("}");
		for(int i = 0; i < splitString.length -1; i++){
			splitString[i] += "}";
			String tempK = splitString[i].substring(splitString[i].indexOf("{")+1, splitString[i].indexOf(":")).trim();
			String tempV = splitString[i].substring(splitString[i].indexOf(":")+1, splitString[i].indexOf("}")).trim();
			char v = tempV.charAt(0);
			if(v == 34){
				objectMap.put(tempK, tempV);
			}else if(v > 47 && v < 58){
				try{
					objectMap.put(tempK, Integer.parseInt(tempV));
				}catch(NumberFormatException e1){
					try{
						objectMap.put(tempK, Double.parseDouble(tempV));
					}catch(NumberFormatException e2){
					}
				}
			}else if(v == 91){
				String array = tempV.substring(tempV.indexOf("[")+1, tempV.indexOf("]")+1);
				String[] splitArray = array.split(",");	
				try{
					Integer.parseInt(splitArray[0]);
					int[] intObject = new int[splitArray.length -1];
					for(int j = 0; j < splitArray.length -1 ; j++){
						intObject[j] = Integer.parseInt(splitArray[j]);
					}
					objectMap.put(tempK, intObject);
					continue;
				}catch(NumberFormatException e1){
				}
				try{
					Double.parseDouble(splitArray[0]);
					double[] doubleObject = new double[splitArray.length -1];
					for(int j = 0; j < splitArray.length -1 ; j++){
						doubleObject[j] = Double.parseDouble(splitArray[j]);
					}
					objectMap.put(tempK, doubleObject);
					continue;
				}catch(NumberFormatException e2){
				}
				String[] stringObject = new String[splitArray.length -1];
				for(int j = 0; j < splitArray.length -1 ; j++){
					stringObject[j] = splitArray[j];
				}
				objectMap.put(tempK, stringObject);
			}
		}
		return objectMap;
	}

	@Override
	public Object getKey(String key) {
		return getObjects().get(key);
	}
	
	//checks the type of array and set the key in the map to the found type
	public String checkType(Map<String, Object> map){
		String content = "[";
		Collection<Object> arr = map.values();
		if(!arr.isEmpty()){
			for(Object obj : arr){
				try{
					int[] i = (int[]) obj;
					for(int x : i) content += x + ",";
					type = "int";
					return content.concat("]");
				}catch(ClassCastException|NumberFormatException e1){
				}
				try{
					double[] d = (double[]) obj;
					for(double x : d) content += x + " ,";
					type = "double";
					return content.concat("]");
				}catch(ClassCastException|NumberFormatException e2){
				}
				try{
					String[] s = (String[]) obj;
					for(String x : s) content += x + " ,";
					type = "string";
					return content.concat("]");
				}catch(ClassCastException|NumberFormatException e3){
				}
			}
		}
		return content;
	}
	
	public Boolean writeFile(){
		File file = new File("C:\\Users\\Nicolas Frick\\workspace\\IT&WE2017\\src\\json\\file.txt");
		try(FileWriter fw = new FileWriter(file)){
			fw.write(this.getString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String readFile(String file){
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			char[] ca = new char[((int)Files.size(Paths.get(file, "")))];
			CharBuffer cbuf = CharBuffer.allocate(ca.length);
			br.read(ca);
			cbuf = CharBuffer.wrap(ca);
			return cbuf.toString();
		}catch(IOException e){
			e.printStackTrace();
		}
		return "reading failed";
	}
	
	

}
