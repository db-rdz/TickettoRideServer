package Command.Phase2;

import Client.User;
import Command.ICommand;
import Server.IServer;
import Server.ServerFacade;
import GameModels.Game;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * FROM CLIENT -> SERVER
 * This command is called after a player's action. (Will decide later specifically to broadcast)
 * At the very least, when a route is claimed this command will be called.
 * This tells everyone in the game what a player has done during their turn.
 *
 * Created by natha on 2/28/2017.
 */

public class BroadcastToChatCommand implements ICommand {
    //Data members
	// Ryan: added authenticationCode so that I can display what user sent the message in the chatroom
    String message;
    String authenticationCode;
    int gameId;

    //Constructors
    public BroadcastToChatCommand(){}
    public BroadcastToChatCommand(int g, String code, String messageToSend) {
    	authenticationCode = code;
    	message = messageToSend;
        gameId = g;
    }

    //Functions
    @Override
    public List<ICommand> execute() throws IServer.GameIsFullException {
        return ServerFacade.SINGLETON.broadcastToChatCommand(gameId, authenticationCode, message);
    }

    @Override
    public String getAuthenticationCode() {
        return authenticationCode;
    }

    public String getMessage() {
        return message;
    }
    
    public int getGameId()
    {
    	return gameId;
    }
   
}
