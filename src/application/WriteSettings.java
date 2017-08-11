package application;

import structures.TaskSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriteSettings {
    public void serializeObject(TaskSettings task, String path){
        FileOutputStream fileOut = null;
        ObjectOutputStream objOutStream = null;

        File directory = new File(this.getCWD()+"\\saves");
        if(!directory.exists()){
            try{
                directory.mkdir();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        try{
            fileOut = new FileOutputStream(path);
            objOutStream = new ObjectOutputStream(fileOut);
            objOutStream.writeObject(task);

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(fileOut == null){
                try{
                    fileOut.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(objOutStream == null){
                try{
                    objOutStream.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }

            }
        }
    }

    public String getCWD(){
        return System.getProperty("user.dir");
    }
}
