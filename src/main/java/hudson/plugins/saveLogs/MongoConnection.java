package hudson.plugins.saveLogs;

import com.mongodb.MongoClient;

public class MongoConnection {

    public static MongoClient getMongoConnection(String hostname, String port){
        Integer mongoPort = Integer.valueOf(port);
        return new MongoClient( hostname , mongoPort );
    }
}
