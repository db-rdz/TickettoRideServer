package Database;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ServerModel.GameModels.Game;
import ServerModel.Models.GameModel;
import ServerModel.UserModel.User;

/**
 * Created by benjamin on 11/02/17.
 */
public class DAO implements iDAO {
    public static iDAO _SINGLETON = new DAO();
    private static DataBase _db;
    private int AUTH_TOKEN_LENGTH = 15;

    public DAO(){
        initializeDB();
    }

    @Override
    public User getUserFromId(int userId) {
        return null;
    }

    @Override
    public int getUserId(String userName) {
        return 0;
    }

    @Override
    public Game getGameFromId(int gameId){
        return null;
    }

    @Override
    public Boolean login(String userName, String password) throws SQLException {
        User dbUser = getUserByUserName(userName);
        if(dbUser == null)
            return false;
        if(password.equals(dbUser.get_S_password()))
        {
            updateUserToken(userName);
            return true;
        }
        return false;
    }

    @Override
    public Boolean authenticateUserWithToken(String token) throws SQLException {
        try {
            return getUserByAccessToken(token) != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByUserName(String username) throws SQLException
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User readUser = null;
        try
        {
            String sql = "select * from Users where Users.userName = ?";
            statement = _db.connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            while(resultSet.next())
            {
                readUser = new User();
                readUser.set_S_userName(resultSet.getString(1));
                readUser.set_S_password(resultSet.getString(2));
                readUser.set_S_token(resultSet.getString(3));
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            if(statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return readUser;
    }

    public User getUserByAccessToken(String token) throws SQLException
    {
        PreparedStatement statement = null;
        ResultSet rs = null;
        User readUser = null;
        try
        {
            String sql = "select * from Users where Users.token = ?";
            statement = _db.connection.prepareStatement(sql);
            statement.setString(1, token);
            rs = statement.executeQuery();
            while(rs.next())
            {
                readUser = new User();
                readUser.set_S_userName(rs.getString(1));
                readUser.set_S_password(rs.getString(2));
                readUser.set_S_token(rs.getString(3));
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            if(statement != null)
                statement.close();
            if (rs != null)
                rs.close();
        }
        return readUser;
    }

    @Override
    public Boolean registerUser(String userName, String password) throws SQLException {
        if(userName == null || password == null){
            return false;
        }
        PreparedStatement registerUserStatement = null;
        ResultSet keyRS = null;
        boolean success = false;
        String registerUserSQL = "insert into Users (userName, password, token) values " +
                "(?, ?, ?)";
        try {
            registerUserStatement = _db.connection.prepareStatement(registerUserSQL);
            registerUserStatement.setString(1, userName);
            registerUserStatement.setString(2, password);
            registerUserStatement.setString(3, makeToken());

            if(registerUserStatement.executeUpdate() == 1){
                success = true;
                updateUserToken(userName);
            }
            else {
                throw new SQLException();
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if(registerUserStatement != null) {
                registerUserStatement.close();
            }
            if (keyRS != null)
                keyRS.close();
        }
        return success;
    }

    @Override
    public List<GameModel> getGamesByUserName(String userName) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<GameModel> gamesList = new ArrayList<GameModel>();
        try {
            String getGamesSQL = "select * from Games where Games.id in " +
                    "(select gameId from UserGames where UserGames.userName = ?)";
            statement = _db.connection.prepareStatement(getGamesSQL);
            statement.setString(1, userName);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                GameModel game = new GameModel();
                game._gameName = resultSet.getString(1);
                game._numberOfPlayers = resultSet.getInt(2);
                game._active = resultSet.getBoolean(3);
                gamesList.add(game);
            }

        } catch (SQLException e ){
            System.out.println(e.getMessage());
        }
        finally {
            if (statement != null)
                statement.close();
            if (resultSet != null)
                statement.close();
        }
        return gamesList;
    }

    @Override
    public List<GameModel> getAllGames() throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<GameModel> gamesList = new ArrayList<GameModel>();

        try {
            String getGamesSQL = "select * from Games";
            statement = _db.connection.prepareStatement(getGamesSQL);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                GameModel game = new GameModel();
                game._gameName = resultSet.getString(1);
                game._numberOfPlayers = resultSet.getInt(2);
                game._active = resultSet.getBoolean(3);
                gamesList.add(game);
            }

        } catch (SQLException e ){
            System.out.println(e.getMessage());
        }
        finally {
            if (statement != null)
                statement.close();
            if (resultSet != null)
                statement.close();
        }
        return gamesList;
    }

    @Override
    public void initializeDB() {
        _db = new DataBase();
    }

    private void updateUserToken(String userName)
    {
        PreparedStatement statement = null;
        try
        {
            String sql = "Update Users set token = ? where Users.userName = ?";
            statement = _db.connection.prepareStatement(sql);
            statement.setString(1, makeToken());
            statement.setString(2, userName);
            statement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(statement != null)
                statement = null;
        }
    }

    private String makeToken()
    {
        return RandomGenerator.getInstance().randomUUID().substring(0, AUTH_TOKEN_LENGTH);
    }
}
