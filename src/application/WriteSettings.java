package application;

import structures.TaskSettings;

import java.io.*;
import java.util.HashMap;

public class WriteSettings {
    public void serializeObject(
            HashMap<String, TaskSettings> tasks, String path) {
        FileOutputStream fileOut = null;
        ObjectOutputStream objOutStream = null;

        File directory = new File(this.getCWD() + "\\saves");
        if (!directory.exists()) {
            try {
                directory.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            fileOut = new FileOutputStream(path);
            objOutStream = new ObjectOutputStream(fileOut);
            objOutStream.writeObject(tasks);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOut == null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objOutStream == null) {
                try {
                    objOutStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public HashMap<String, TaskSettings> loadObject(String fileName) {
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        HashMap<String, TaskSettings> obj = null;
        try {
            fileIn = new FileInputStream(fileName);
            in = new ObjectInputStream(fileIn);
            obj = (HashMap<String, TaskSettings>) in.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileIn == null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in == null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return obj;
    }

    public String getCWD() {
        return System.getProperty("user.dir");
    }
}
