package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileFunctions {

    public static void createFile(String fileName, String content)
    {
        try{
            FileWriter fileWriter = new FileWriter(fileName, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static List<String> readFile(String filePath) throws IOException {
        FileReader fileReader = new FileReader(new File(filePath));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> list = new ArrayList<>();
        String line = "";
        while((line = bufferedReader.readLine())!=null){
            list.add(line.trim());
        }
        return list;
    }
}
