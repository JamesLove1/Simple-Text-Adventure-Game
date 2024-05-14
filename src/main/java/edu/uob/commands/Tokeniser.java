package edu.uob.commands;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//notes
// add way to deal with puntuation

public class Tokeniser {

    private String player;

    ArrayList<String> commandTokens;

    Boolean validCommand;

    public Tokeniser (String command){
        String regex = ":";
        String[] commandStr = command.split(regex,2);
        this.player = commandStr[0].toLowerCase();

        if(commandStr.length == 2 ){
            String regexCommandStr = "[ ,!?:@/\\$%\\^\\*\\(\\)\";~>/\\<\\.\\|#£\\\\]+";
            this.commandTokens = new ArrayList<>();
            this.commandTokens.addAll(Arrays.asList(commandStr[1].toLowerCase().split(regexCommandStr)));
            validCommand = true;
        } else {
            validCommand = false;
        }

    }

    public boolean getValidPlayerName(){
        String regex = "^[a-zA-Z '-]+$";
        String input = this.player;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        boolean matches = matcher.matches();
        return matches;
    }

    public boolean getValidCommand(){
        return this.validCommand;
    }

    public String getPlayer(){
        return this.player;
    }

    public ArrayList<String> getTokens(){
        return this.commandTokens;
    }

}
