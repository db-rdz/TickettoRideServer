package Database;

public class AbstractFactory {

	public static IFactory loadProvider(String plugin) {
		
		if (plugin.equals("relational")) {
			return new RelationalFactory();
		}
		else if (plugin.equals("mongo")) {
			return new MongoFactory();
		}
		return null;
	}
}
