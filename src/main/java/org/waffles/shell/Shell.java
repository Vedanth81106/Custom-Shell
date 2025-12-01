package org.waffles.shell;

import org.waffles.commands.AiCommand;
import org.waffles.commands.Command;
import org.waffles.commands.HelpCommand;
import org.waffles.commands.MathCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Shell {

    private final Map<String, Command> cmds = new HashMap<>();
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
    }

    public void start(){

        System.out.println("Custom shell initialized");

        while(isRunning){
            System.out.print("cShell>");

            String input = scan.nextLine().trim();

            if(input.isEmpty()) continue;

            String[] parts = input.split("\\s+");
            String cmdName = parts[0];

            Command cmd = cmds.get(cmdName);

            if(cmd != null){
                try{
                    cmd.execute(parts);
                } catch (Exception e) {
                    System.out.println("Error executing command: " + e.getMessage());
                }
            }else{
                System.out.println("Unknown command: " + cmdName);
            }
        }

        System.out.println("Exiting shell...");
    }
}
