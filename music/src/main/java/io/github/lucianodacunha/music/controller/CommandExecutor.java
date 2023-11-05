package io.github.lucianodacunha.music.controller;

import org.springframework.stereotype.Component;

@Component
public class CommandExecutor {
    public void executor(Command command){
        command.execute();
    }
}
