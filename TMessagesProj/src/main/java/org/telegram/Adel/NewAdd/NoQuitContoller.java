package org.telegram.Adel.NewAdd;

import org.telegram.Adel.Setting;

public class NoQuitContoller {
    public static void addToNoQuit(Long id){
        String m= Setting.getNoQuitList();
        m=m+"-"+String.valueOf(id);
        Setting.setNoQuitList(m);
  
    public static Boolean isNoQuit(String user){
        try {
            return m;
        }catch (Exception e){
            return false;
        }
    }
   
}
