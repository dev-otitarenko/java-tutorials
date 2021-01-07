/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.commons.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author maestro
 */
public class RegexpUtl {
    public static String getMatch(String s, String p) {
       // returns first match of p in s for first group in regular expression 
        Matcher m = Pattern.compile(p).matcher(s);
        return m.find() ? m.group(1) : "";
    }
    
    public static List<String> getMatches(String s, String p) {
        List<String> matches = new ArrayList<String>();
        Matcher m = Pattern.compile(p).matcher(s);
        while(m.find()) {
            matches.add(m.group(1));
        }
        return matches;
    }    
}
