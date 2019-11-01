package com.InstagramClone.ImageService;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.InstagramClone.model.Comment;
import com.mongodb.client.model.Updates;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.InstagramClone.model.Account;
import com.InstagramClone.model.Image;
import com.InstagramClone.model.Post;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class DatabaseController {
    private static DatabaseController single_instance = null; 
	
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Image> imageDb;
	private MongoCollection<Post> postDb;
	private MongoCollection<Account> accountDb;

	private DatabaseController () {
		Properties prop = new Properties();
		String propFileName = "db.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");
		
	    mongoClient = MongoClients.create(new ConnectionString
	    		("mongodb+srv://"+username+":"+password+"@cluster0-lhmsj.mongodb.net/test?retryWrites=true&w=majority"));
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder().register("com.InstagramClone.model").build();
		CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

	    database = mongoClient.getDatabase("db");
	    database = database.withCodecRegistry(pojoCodecRegistry);
	    imageDb = database.getCollection("Images", Image.class);
	    postDb = database.getCollection("Posts", Post.class);
	    accountDb = database.getCollection("Accounts", Account.class);
	}
	
	// Insert an image object into the database given an image object
	public void insertImage(Image image) {
	    imageDb.insertOne(image);
	}
	
	// Return image object given an objectid as a string
	public Image getImage(String id) {
		return imageDb.find(eq("_id", new ObjectId(id))).first();
	}
	
	// Create an account from a given account object
	public String createAccount(Account account) {
		accountDb.insertOne(account);
		return account.get_id();
	}
	
	public Account checkAccount(String username, String password) {
		Account login = accountDb.find(and(eq("username", username), eq("password", password))).first();
		if(login != null) {
			return login;
		} else {
			return null;
		}
	}

	public String followUser(ObjectId targetAccount, ObjectId currentAccount) {
		accountDb.updateOne(eq("_id", currentAccount), Updates.addToSet("followedUsers", targetAccount));
		return "success";
	}

	public String unfollowUser(ObjectId targetAccount, ObjectId currentAccount) {
		accountDb.updateOne(eq("_id", currentAccount), Updates.pull("followedUsers", targetAccount));
		return "success";
	}
	// Return account object given an objectid
	public Account getAccount(ObjectId id) {
		return accountDb.find(eq("_id", id)).first();
	}

	// Return account object given an username as a string
	public Account getAccount(String username) {
		return accountDb.find(eq("username", username)).first();
	}

	public Post getPost(ObjectId id){
		return postDb.find(eq("_id", id)).first();
	}

	// Makes a post on the database
	public void insertPost(Post post) {
		postDb.insertOne(post);
		accountDb.updateOne(eq("_id", new ObjectId(post.getAccount().toHexString())),
				Updates.addToSet("posts", new ObjectId(post.get_id())));
	}

	// TODO disallow liking a post more than once
	public void likePost(Account account, Post post) {
		accountDb.updateOne(eq("_id", account._id), Updates.addToSet("likedPosts", post._id));
		postDb.updateOne(eq("_id", post._id), Updates.inc("likes", 1));
	}

    public void writeComment(Account account, Post post, String comment) {
        postDb.updateOne(eq("_id", post._id),
				Updates.addToSet("comments",
						new Comment(account.getUsername(), comment)));
    }

	// Returns a list of posts based on a bson query
	public FindIterable<Post> postFind(Bson query) {
		return postFind(query);
	}
	
    public static DatabaseController getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new DatabaseController(); 
  
        return single_instance; 
    }
}
