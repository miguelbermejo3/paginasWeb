package app.gui.mongo;

import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoSession {

	private static final String USER = "pagWeb";
	private static final String PASS = "pagWeb";
	private static final String DATABASE = "PagWeb";
	private static final String SERVER = "pagweb.huvr1qa.mongodb.net";
	
	private static MongoDatabase database;
	
	public static MongoDatabase getDatabase() {
		if(database==null){
        String connectionString = "mongodb+srv://"+USER+":"+PASS+"@"+SERVER+"/?retryWrites=true&w=majority";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        MongoClient mongoClient = MongoClients.create(settings);
            
                // Send a ping to confirm a successful connection
                 database = mongoClient.getDatabase(DATABASE);
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            
                CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
    			CodecProvider codecProvider = PojoCodecProvider.builder().automatic(true).build();
    			CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(codecProvider);
    			CodecRegistry codecRegistry = CodecRegistries.fromRegistries(defaultCodecRegistry, pojoCodecRegistry);
    			database = database.withCodecRegistry(codecRegistry);
    }
		return database;
	
	}
}
