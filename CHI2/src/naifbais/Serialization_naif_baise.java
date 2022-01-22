package naifbais;

import java.io.*;
import java.util.Map;

public class Serialization_naif_baise implements Serializable {
    private static final String PATH = "C:\\Users\\ASUS\\Desktop\\text mining looqman\\mapSaver\\naiBaise\\mapIn.txt";
    private static final String PATH2 = "C:\\Users\\ASUS\\Desktop\\text mining looqman\\mapSaver\\naiBaise\\mapIn2.txt";
    public Serialization_naif_baise()
    {

    }
    public void saveBigMap(Map<String, Map<String, Integer>> users)
            throws IOException
    {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PATH))) {
            os.writeObject(users);
        }
    }

    public Map<String, Map<String, Integer>> readBigMap()
            throws ClassNotFoundException, IOException
    {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(PATH))) {
            return (Map<String, Map<String, Integer>>) is.readObject();
        }
    }

    public void saveNumberPerClass(Map<String, Integer> users)
            throws IOException
    {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PATH2))) {
            os.writeObject(users);
        }
    }

    public Map<String, Integer> readNumberPerClass()
            throws ClassNotFoundException, IOException
    {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(PATH2))) {
            return (Map<String, Integer>) is.readObject();
        }
    }
}
