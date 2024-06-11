package tr4nt.autofish.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ConfigFile {

    private static String fileName;

    private static Path path = FabricLoader.getInstance().getConfigDir();

    private static Path configFilePath;
    public static void register(String file)   {
        fileName = '/' + file + ".json";
        configFilePath = Paths.get(path.toString()+fileName);
        if (!checkFile(path.toString()+fileName))
        {
            try {
                createConfig();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static File getFile(String path)
    {
        return new File(path);
    }
    private static boolean checkFile(String path)
    {

        return getFile(path).exists();
    }

    private static void createConfig() throws IOException {
        File f = new File(configFilePath.toString());


        if (f.createNewFile())
        {
            PrintWriter filewrite = new PrintWriter(path.toString()+fileName);

            filewrite.write("{}");

            filewrite.close();
        };
    }

    public static void addValue(Map<String, String> vals, boolean replace) {
        String s = null;
        try {
            s = Files.readString(configFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject obj = JsonParser.parseString(s).getAsJsonObject();
        String key = (String) vals.keySet().toArray()[0];
        JsonElement value = (JsonElement)JsonParser.parseString((String) vals.values().toArray()[0]);
        if (!obj.has(key))
        {
            obj.add(key, value);

        } else if(replace)
        {
            obj.remove(key);
            obj.add(key, value);

        }

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(configFilePath.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pw.write(obj.toString());
        pw.close();
        format();
    }

    public static JsonElement getValue(String key)  {
        String s = null;
        try {
            s = Files.readString(configFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObject obj = JsonParser.parseString(s).getAsJsonObject();

        return obj.get(key);
    }

    private static void format() {
        String buff = "";
        String s = null;
        try {
            s = Files.readString(configFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (i != 0 ) {
               char prevChar =  s.charAt(i-1);

               if (prevChar ==',' || prevChar == '{' || (i == s.length()-1))
               {
                   if (i == s.length()-1)
                   {
                       buff += "\n";
                   } else
                   {
                       buff += "\n\t";
                   }
               }

            }
            buff += c;
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(configFilePath.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pw.write(buff);
        pw.close();

    }
}
