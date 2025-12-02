package org.waffles.shell;

import org.waffles.Colors;
import org.waffles.commands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Shell {

    public static final Map<String, Command> cmds = new HashMap<>();
    private final Scanner scan = new Scanner(System.in);
    public boolean isRunning = true;

    public Shell(){

        cmds.put("exit",args -> isRunning = false);
        cmds.put("spotify",ShellUtils::spotiCmd); // same as args->StringUtils.spotiCmd(args);
        cmds.put("yt",ShellUtils::ytCmd);
        cmds.put("steam",args -> ShellUtils.otherCmd("steam"));
        cmds.put("discord",args -> ShellUtils.otherCmd("discord"));
        cmds.put("google",ShellUtils::googleCmd);
        cmds.put("math",new MathCommand());
        cmds.put("ai",new AiCommand());
        cmds.put("help",new HelpCommand());
        cmds.put("leetcode", new LeetCodeCommand());
        cmds.put("do", new DoCommand());
    }

    public void start(){

        System.out.println(Colors.CYAN_BOLD + "Custom Shell Initialized" + Colors.RESET);

        while(isRunning){
            System.out.print(Colors.CYAN_BOLD + "cShell> " + Colors.RESET);

            String input = scan.nextLine().trim();

            if(input.isEmpty()) continue;

            String[] parts = input.split("\\s+");
            String cmdName = parts[0];

            Command cmd = cmds.get(cmdName);

            if(cmd != null){
                try{
                    cmd.execute(parts);
                } catch (Exception e) {
                    System.out.println(Colors.RED + "Error executing command: " + e.getMessage() + Colors.RESET);
                }
            }else{
                System.out.println(Colors.YELLOW + "Unknown command: " + cmdName + Colors.RESET);            }
        }

        System.out.println("Exiting shell...");
    }
}
