package Command.Phase1;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Client.User;
import Command.ICommand;
import GameModels.Game;
import Server.ServerFacade;

public class StartGameCommand implements ICommand {
    private int gameId;
    private List<String> usernamesInGame;
    private String _strAuthenticationCode;
    
    private StartGameCommand(){}
    public StartGameCommand(int g, List<String> k){
        gameId = g;
        usernamesInGame = k;
    }


    @Override
    public List<ICommand> execute(){
    	return ServerFacade.SINGLETON.startGame(gameId, usernamesInGame, _strAuthenticationCode);
    }

    @Override
    public String getAuthenticationCode(){
        return _strAuthenticationCode;}

    public int getGameId() {
        return gameId;
    }

    public List<String> getUsernamesInGame() {
        return usernamesInGame;
    }
    
    public String getStrAuthenticationCode()
    {
    	return _strAuthenticationCode;
    }
}

