
import java.util.HashMap;
import java.util.Map;
import json.JsonSerializer;
import network.ServerTCP;

public class MainClass {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		JsonSerializer jsonSer = new JsonSerializer();
		
		jsonSer.addString("String", "Ich bin ein String");
		jsonSer.addDouble("Double", 0.0);
		jsonSer.addInteger("Int", 0);
		
		Map<String, Object> mop = new HashMap<String, Object>();
		int[] arr = {1,2,3,45,890};
		mop.put("Eins", arr);
		jsonSer.addArray("IntArray", mop );
		
		Map<String, Object> mip = new HashMap<String, Object>();
		double[] irr = {0.1,2.5,3.0,4.2};
		mip.put("Eins", irr);
		jsonSer.addArray("DoubleArray", mip );
		
		Map<String, Object> mup = new HashMap<String, Object>();
		String[] urr = {"Zwo","Drei","Vier"};
		mup.put("Eins", urr);
		jsonSer.addArray("StringArray", mup );
		
//		jsonSer.parseString(jsonSer.readFile());
//		System.out.println(jsonSer.getString());
//		jsonSer.getObjects().forEach((k,v) -> {
//			System.out.println(k + "  " + v);
//		});
//		for(double d : (double[])jsonSer.getKey("\"DoubleArray\"")){
//			System.out.println(d);
//		}
		
		ServerTCP server = new ServerTCP(jsonSer);	
	}
}
