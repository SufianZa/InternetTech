import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sufian Vaio on 05.05.2017.
 */
 class JSONSerialize implements IJsonSerialize {
    String JSON = "{" + "\n";
    HashMap<String, Object> josnHash = new HashMap<>();


    @Override
    public void addString(String key, String str) {
        josnHash.put(key, "\"" + str + "\"");
    }

    @Override
    public void addInteger(String key, int num) {
        josnHash.put(key, num);
    }

    @Override
    public void addDouble(String key, double num) {
        josnHash.put(key, num);
    }

    @Override
    public void addArray(String key, Map<String, Object> array) {

    }

    public void customAddArray(String key, Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].getClass() == String.class) array[i] = "\"" + array[i] + "\"";
        }
        josnHash.put(key, array);
    }

    @Override
    public String getString() {
        for (Map.Entry<String, Object> entry : josnHash.entrySet()) {
            try {
                if (entry.getValue() instanceof Object[]) {
                    JSON += "\"" + entry.getKey() + "\": " + Arrays.toString((Object[]) entry.getValue()) + ",\n";
                } else {
                    JSON += "\"" + entry.getKey() + "\": " + entry.getValue() + ",\n";
                }
            } catch (NullPointerException e) {
                JSON += "\"" + entry.getKey() + "\": " + "null" + ",\n";
            }
        }
        return JSON + "}";
    }


    @Override
    public void parseString(String str) {
        String value = null;
        String key = "";
        String[] objects = str.split(",");
        for (int i = 0; i < objects.length - 1; i++) {
            key = objects[i].substring(objects[i].indexOf("\"") + 1, objects[i].indexOf("\"", objects[i].indexOf("\"") + 1));
            value = objects[i].substring(objects[i].indexOf(":") + 2);
            while (value.contains("[") ^ value.contains("]")) {
                value = value.concat("," + objects[++i]);
            }
            josnHash.put(key, checkType(value));
        }
    }


    public Object checkType(String string) {
        // if it is an Array
        if (string.contains("[")) {
            string = string.replace("[", "").replace(" ", "").replace("]", "");
            String[] arrayValues = string.split(",");
            Object[] objectArray = new Object[arrayValues.length];
            for (int i = 0; i < objectArray.length; i++) { // deep copy of values and checktype every value in the Array
                objectArray[i] = checkType(arrayValues[i]);
            }
            return objectArray;
            // if is not an Array try boolean
        } else if (string.equals("true")) {
            return true;
        } else if (string.equals("false")) {
            return false;
        } else {
            try {
                // if it is not a boolean
                throw new NotBooleanException();
            } catch (NotBooleanException f) {
                try {
                    int i = Integer.parseInt(string);
                    //try to make it int
                    return i;
                } catch (NumberFormatException e) {
                    //if it is not a Integer nor a boolean
                    try {
                        double i = Double.parseDouble(string);
                        //try to make it double
                        return i;
                    } catch (NumberFormatException fe) {
                        //it  is not a double,an int nor a boolean
                        //e.printStackTrace();
                        if (!(string.replace("\"", "").isEmpty() || string.equals("null") || string.equals("NaN")))
                            return string;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Map<String, Object> getObjects() {
        return null;
    }

    @Override
    public Object getKey(String key) {
        for (Map.Entry<String, Object> entry : josnHash.entrySet()) {
            if (entry.getKey().equals(key)) {
                if (entry.getValue().getClass() == String.class) {
                    return String.valueOf(entry.getValue()).replace("\"", "");
                } else return entry.getValue();
            }

        }
        return null;
    }
}
