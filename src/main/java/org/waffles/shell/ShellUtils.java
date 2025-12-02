package org.waffles.shell;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ShellUtils {

    public static void cmdStarter(String... cmd) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.start();
    }

    public static String queryBuilder(int start, String... args) {

        StringBuilder sb = new StringBuilder();
        for(int i = start; i < args.length;i++){
            sb.append(args[i]);
            if (i < args.length - 1) {
                sb.append(" ");
            }
        }

        return URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8);
    }

    public static void otherCmd(String app) throws IOException{

        String[] cmd = {"cmd.exe","/c","start",app + ":"};
        cmdStarter(cmd);
        System.out.println("Opening " + app + "...");
    }

    public static void googleCmd(String[] query) throws IOException{
        String[] cmd;
        if(query.length == 1){
            cmd = new String[]{"cmd.exe", "/c", "start", "https://google.com"};
        }else{
            cmd = new String[]{"cmd.exe", "/c", "start", "https://google.com/search?q=" + queryBuilder(1, query)};
        }
        cmdStarter(cmd);
        System.out.println("Opening google...");
    }

    public static void spotiCmd(String[] query) throws IOException {

        String[] cmd;
        if(query.length == 1){
            cmd = new String[]{"cmd.exe", "/c", "start", "spotify:"};
        }else{
            cmd = new String[]{"cmd.exe", "/c", "start", "spotify:search:" + queryBuilder(1, query)};
        }
        cmdStarter(cmd);
        System.out.println("Opening spotify...");
    }

    public static void ytCmd(String[] query) throws IOException {

        String[] cmd;
        if(query.length == 1){
            cmd = new String[]{"cmd.exe", "/c", "start", "https://youtube.com/"};
        }else{
            cmd = new String[]{"cmd.exe", "/c", "start", "https://www.youtube.com/results?search_query=" + queryBuilder(1, query)};
        }
        cmdStarter(cmd);
        System.out.println("Opening youtube...");
    }

}
