package com.chaos.util.wiki;

import com.chaos.model.definitions.ItemDefinition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.webkit.WebPage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Jonathan on 9/1/2016.
 */
public class WikiDumper {

    public static void dumpItemDefinitions() {
        // Create the path and file objects.
        Path path = Paths.get("./wiki/", "ItemDefinitions.json");
        File file = path.toFile();
        file.getParentFile().setWritable(true);

        // Attempt to make the player save directory if it doesn't
        // exist.
        if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (SecurityException e) {
                System.out.println("Unable to create directory for wiki data!");
            }
        }
        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i <= ItemDefinition.getDefinitions().length - 1; i++) {
                if (ItemDefinition.getDefinitions()[i] == null) {
                    continue;
                }
                if (ItemDefinition.getDefinitions()[i].getName().equalsIgnoreCase("null")) {
                    Gson builder = new GsonBuilder().setPrettyPrinting().create();
                    ItemDefinition.getDefinitions()[i].setId(i);
                    writer.write(builder.toJson(ItemDefinition.getDefinitions()[i]));
                    writer.write(System.getProperty("line.separator"));
                    continue;
                }
                if(ItemDefinition.getDefinitions()[i].getHighAlchValue() > 0 && ItemDefinition.getDefinitions()[i].getLowAlchValue() > 0 && ItemDefinition.getDefinitions()[i].getValue() > 0) {
                    System.out.println("Dumping item definition " + i + "");
                    Gson builder = new GsonBuilder().setPrettyPrinting().create();
                    ItemDefinition.getDefinitions()[i].setId(i);
                    writer.write(builder.toJson(ItemDefinition.getDefinitions()[i]));
                    writer.write(System.getProperty("line.separator"));
                    continue;
                }
                WikiDumper.dumpItemDefinition(i);
                System.out.println("Dumping item definition " + i + "");
                Gson builder = new GsonBuilder().setPrettyPrinting().create();
                ItemDefinition.getDefinitions()[i].setId(i);
                writer.write(builder.toJson(ItemDefinition.getDefinitions()[i]));
                writer.write(System.getProperty("line.separator"));
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dumpItemDefinition(int itemId) {
        System.out.println("Starting wiki dump of Item ID: "+itemId);
        ArrayList<String> lines = getItemPage(ItemDefinition.forId(itemId).getName().replaceAll(" ", "_"));
        Iterator<String> iterator = lines.iterator();
        try {
            while (iterator.hasNext()) {
                String line = iterator.next();
                if (line.contains("High Level Alchemy\">High alch</a></th>")) {
                    if(ItemDefinition.forId(itemId).getHighAlchValue() <= 0 || ItemDefinition.forId(itemId).getValue() <= 0) {
                        if(line.contains("Grand Exchange")) {
                            String[] globalValue = line.split("<span class=\"infobox-quantity-replace\">");
                            String price = globalValue[1];
                            price = price.substring(0, price.indexOf("</span> coins (<a href=\"/wik"));
                            price = price.replaceAll(",", "");
                            System.out.println("Successfully dumped exchange value: " + price + "");
                            ItemDefinition.forId(itemId).setValue(Integer.parseInt(price));
                        } else {
                            String[] globalValue = line.split("\"/wiki/Value\" title=\"Value\">Value</a></th><td>");
                            String price = globalValue[1];
                            if(price.contains("coin</td></tr><tr><th><a href=\"/wiki/High_Leve")) {
                                price = price.substring(0, price.indexOf(" coin</td></tr><tr><th><a href=\"/wiki/High_Leve"));
                            } else {
                                price = price.substring(0, price.indexOf(" coins</td></tr><tr><th><a href=\"/wiki/High_Leve"));
                            }
                            price = price.replaceAll(",", "");
                            System.out.println("Successfully dumped regular value: " + price + "");
                            ItemDefinition.forId(itemId).setValue(Integer.parseInt(price));
                        }
                    }
                    if(ItemDefinition.forId(itemId).getHighAlchValue() <= 0) {
                        String[] highAlch;
                        if(line.contains("High Level Alchemy\">High alch</a></th><td data-attr-param=\"high\">")) {
                            highAlch = line.split("High Level Alchemy\">High alch</a></th><td data-attr-param=\"high\">");
                        } else {
                            highAlch = line.split("High Level Alchemy\">High alch</a></th><td>");
                        }
                        String price = highAlch[1];
                        if(price.contains(" coins</td></tr><tr><th><a href=\"/wiki/Low_L")) {
                            price = price.substring(0, price.indexOf(" coins</td></tr><tr><th><a href=\"/wiki/Low_L"));
                        } else {
                            price = price.substring(0, price.indexOf(" coin</td></tr><tr><th><a href=\"/wiki/Low_L"));
                        }
                        price = price.replaceAll(",", "");
                        System.out.println("Successfully dumped high alch value: " + price + "");
                        ItemDefinition.forId(itemId).setHighAlchValue(Integer.parseInt(price));
                    }
                    if(ItemDefinition.forId(itemId).getLowAlchValue() <= 0) {
                        String[] lowAlch;
                        if(line.contains("itle=\"Low Level Alchemy\">Low alch</a></th><td data-attr-param=\"low\">")) {
                            lowAlch = line.split("itle=\"Low Level Alchemy\">Low alch</a></th><td data-attr-param=\"low\">");
                        } else {
                            lowAlch = line.split("itle=\"Low Level Alchemy\">Low alch</a></th><td>");
                        }
                        String price = lowAlch[1];
                        if(price.contains(" coins</td></tr><tr><th><a href=\"/wiki/Destroy_(action)\" ")) {
                            price = price.substring(0, price.indexOf(" coins</td></tr><tr><th><a href=\"/wiki/Destroy_(action)\" "));
                        } else {
                            price = price.substring(0, price.indexOf(" coin</td></tr><tr><th><a href=\"/wiki/Destroy_(action)\" "));
                        }
                        price = price.replaceAll(",", "");
                        System.out.println("Successfully dumped low alch value: " + price + "");
                        ItemDefinition.forId(itemId).setLowAlchValue(Integer.parseInt(price));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getItemPage(String name) {
        ArrayList<String> PAGE_LINES = new ArrayList<String>();
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            url = new URL("http://runescape.wikia.com/wiki/"+name);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                PAGE_LINES.add(line);
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
        return PAGE_LINES;
    }
}
