/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.commons.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class ConvertUtils {
    public static InputStream convertUTF8toWIN1251(InputStream   data){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(data));
            String line;
            Boolean isUtf = false;
                while ((line = reader.readLine()) != null) {
                    line+="\n";
                    if (line.toUpperCase().contains("UTF-8")) {
                    	isUtf=true;
                        line = line.replace("UTF-8", "windows-1251");
                    }
                    if (isUtf){
                        baos.write(convertEncoding(line.getBytes() , "UTF-8", "windows-1251" ));
                    } else {
                    //line+="\n";
                    baos.write(line.getBytes());
                    }
                }
        } catch (IOException e) {
        }        
        InputStream is2 = new ByteArrayInputStream(baos.toByteArray());
        return is2;
    }
	
    public static byte[] convertEncoding(byte[] bytes, String from, String to) throws UnsupportedEncodingException {
        return new String(bytes, from).getBytes(to);
    }
    
    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
            byte[] buffer = new byte[0xFFFF];

            for (int len; (len = is.read(buffer)) != -1;)
                os.write(buffer, 0, len);

            os.flush();

            return os.toByteArray();
        }
    }    
}
