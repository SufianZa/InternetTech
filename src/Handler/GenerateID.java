package Handler;

import java.util.ArrayList;

/**
 * Created by Sufian Vaio on 16.07.2017.
 */
public class GenerateID {
    static int id =1;

   static ArrayList<Integer> idList =new ArrayList();
   static int generate(){
        for(int i = 0 ; i< 100 ;i++){
            if(!searchID(id)){
                System.out.println(id);
                idList.add(id);
                return id;
            }else{
                id++;
            }
        }
       return -1;
    }

    public static boolean searchID(int a) {
        for(int x : idList){
            if(x==a) return true;
        }
        return false;
    }

    public static int getlast() {
        return idList.get(idList.size()-1);
    }
}
