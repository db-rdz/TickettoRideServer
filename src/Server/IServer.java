package Server;
import Client.IClient;
import Client.User;
import Command.ICommand;
import GameModels.Game;
import ServerModel.GameModels.CardsModel.TrainCard;
import ServerModel.GameModels.RouteModel.Route;
import ServerModel.GameModels.RouteModel.iRoute;
import UserModel.*;

import java.util.List;

/**
 * Created by RyanBlaser on 2/5/17.
 */

public interface IServer {

    public static class GameIsFullException extends Exception {
    }
    
    public static class UserAlreadyLoggedIn extends Exception {
    	
    }

    public List<ICommand> login(String username, String password) throws IClient.InvalidUsername, IClient.InvalidPassword, UserAlreadyLoggedIn;
    public List<ICommand> register(String username, String password) throws IClient.UsernameAlreadyExists, UserAlreadyLoggedIn;
    public List<ICommand> addJoinableGameToServer(Game game, String authenticationCode);
    public List<ICommand> addWaitingGame(Game game);
    public List<ICommand> removeGame(Game game);
    public List<ICommand> startGame(int gameId, List<String> usernamesInGame, String strAuthenticationCode);
    public List<ICommand> addPlayer(String str_authentication_code, int iGameId) throws GameIsFullException;
    public List<ICommand> logout(String str_authentication_code);
    public List<ICommand> broadcastToChatCommand(int gameId, String authenticationToken, String message);
    public List<ICommand> claimRoute(int gameId, String authenticationCode, Route theRoute, List<TrainCard> cardsUsedToClaimRoute);
    public List<ICommand> getTopDeckTrainCard(int gameId, String authenticationToken, int turnNumber);
    public List<ICommand> getFaceUpTableTrainCard(int gameId, int cardIndex, boolean isWild, String authenticationCode, int turnNumber);
    
}