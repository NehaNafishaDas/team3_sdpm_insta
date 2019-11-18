package com.InstagramClone.ImageService;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Properties;

import com.InstagramClone.model.Comment;
import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.InstagramClone.model.Account;
import com.InstagramClone.model.BlockedUser;
import com.InstagramClone.model.Image;
import com.InstagramClone.model.Post;
import com.InstagramClone.model.Privacy;
import com.mongodb.ConnectionString;


public class DatabaseController {
    private static DatabaseController single_instance = null; 
	
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Image> imageDb;
	private MongoCollection<Post> postDb;
	private MongoCollection<Account> accountDb;
	private MongoCollection<Privacy> privacyDb;
	private MongoCollection<BlockedUser> blockedUserDb;

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
	    privacyDb = database.getCollection("Privacy", Privacy.class);
	    blockedUserDb = database.getCollection("BlockedUser", BlockedUser.class);
	}
	
	// Insert an image object into the database given an image object
//	public void insertImage(Image image) {
//	    imageDb.insertOne(image);
//	}
	
	// Return image object given an objectid as a string
	public Image getImage(String id) {
		return imageDb.find(eq("_id", new ObjectId(id))).first();
	}
	
	// Create an account from a given account object
	public String createAccount(Account account) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(account.getPassword().getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		account.setPassword(sb.toString());
		accountDb.insertOne(account);
		return account.get_id();
	}
	
	//Create a privacy flag table with same id as the account._id and a flag
	public void createPrivacy(Privacy privacy) throws NoSuchAlgorithmException {
		privacyDb.insertOne(privacy);
	}
	
	public void createBlockList(BlockedUser blockedUsers) throws NoSuchAlgorithmException {
		blockedUserDb.insertOne(blockedUsers);
	}
	
	public Account checkAccount(String username, String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		Account login = accountDb.find(and(eq("username", username), eq("password", sb.toString()))).first();
		if(login != null) {
			return login;
		} else {
			return null;
		}
	}

	public String followUser(ObjectId targetAccount, ObjectId currentAccount) {
		accountDb.updateOne(eq("_id", currentAccount), Updates.addToSet("followedUsers", targetAccount));
		accountDb.updateOne(eq("_id", targetAccount), Updates.addToSet("followedBy", currentAccount));
		return "success";
	}

	public void changePassword(ObjectId targetAccount, String newPassword) {
		accountDb.updateOne(eq("_id", targetAccount), Updates.set("password", newPassword));
	}

	public void changeEmail(ObjectId targetAccount, String newEmail) {
		accountDb.updateOne(eq("_id", targetAccount), Updates.set("email", newEmail));
	}

	public String unfollowUser(ObjectId targetAccount, ObjectId currentAccount) {
		accountDb.updateOne(eq("_id", currentAccount), Updates.pull("followedUsers", targetAccount));
		accountDb.updateOne(eq("_id", targetAccount), Updates.pull("followedBy", currentAccount));
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

	public ArrayList<Post> getPost(String description){
		String similarDesc = "/.*"+description+".*/";
		FindIterable<Post> posts = postDb.find(eq("description",similarDesc));
		MongoCursor<Post> iterator = posts.iterator();
		ArrayList<Post> response = new ArrayList<Post>();
		while(iterator.hasNext()) {
			response.add(iterator.next());
		}
		return response;
	}

	public Privacy getPrivacy(String userId) throws NoSuchAlgorithmException {
		return privacyDb.find(eq("userId", userId)).first();
	}
	
	public BlockedUser getBlockList(String currentUser) throws NoSuchAlgorithmException {
		return blockedUserDb.find(eq("currentUser", currentUser)).first();
	}

	public void setProfilePicture(ObjectId targetAccount, String url) {
		accountDb.updateOne(eq("_id", targetAccount), Updates.set("profilepicture", url));
	}

	public void changeBio(ObjectId targetAccount, String bio) {
		accountDb.updateOne(eq("_id", targetAccount), Updates.set("bio", bio));
	}

	public void changeFirstname(ObjectId targetAccount, String firstname) {
		accountDb.updateOne(eq("_id", targetAccount), Updates.set("firstName", firstname));
	}

	public void changeLastname(ObjectId targetAccount, String lastname) {
		accountDb.updateOne(eq("_id", targetAccount), Updates.set("lastName", lastname));
	}

	// Makes a post on the database
	public void insertPost(Post post) {
		postDb.insertOne(post);
		accountDb.updateOne(eq("_id", new ObjectId(post.getAccount().toHexString())),
				Updates.addToSet("posts", post.get_id()));
	}

	public boolean likePost(Account account, Post post) {
		ArrayList<ObjectId> likedPosts = account.getLikedPosts();
		if(!likedPosts.contains(post._id)) {
			accountDb.updateOne(eq("_id", account._id), Updates.addToSet("likedPosts", post._id));
			postDb.updateOne(eq("_id", post._id), Updates.inc("likes", 1));
			return true;
		} else return false;
	}

	public boolean  unlikePost(Account account, Post post) {
		ArrayList<ObjectId> likedPosts = account.getLikedPosts();
		if(likedPosts.contains(post._id)) {
			accountDb.updateOne(eq("_id", account._id), Updates.pull("likedPosts", post._id));
			postDb.updateOne(eq("_id", post._id), Updates.inc("likes", -1));
			return true;
		} else return false;
	}

    public void writeComment(Account account, Post post, String comment) {
        postDb.updateOne(eq("_id", post._id),
				Updates.addToSet("comments",
						new Comment(account.getUsername(), account._id, comment)));
    }

	public void writeComment(Account account, Post post, String comment, String imageUrl) {
		postDb.updateOne(eq("_id", post._id),
				Updates.addToSet("comments",
						new Comment(account.getUsername(), account._id, comment, imageUrl)));
	}

    public ArrayList<Post> getPopularPosts() {
		FindIterable<Post> posts = postDb.find().sort(Sorts.descending("likes")).limit(9);
		MongoCursor<Post> iterator = posts.iterator();
		ArrayList<Post> response = new ArrayList<Post>();
		while(iterator.hasNext()) {
			response.add(iterator.next());
		}
		return response;
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
    
    public void changePrivacy(String currentUserId, boolean isPrivate) {
    	privacyDb.updateOne(eq("userId", currentUserId), Updates.set("isPrivate", isPrivate));
	}
  
    public void addToBlockedList(String currentUser, String targetUser) {
    	blockedUserDb.updateOne(eq("userId", currentUser), Updates.addToSet("blockedUsers", targetUser));
	}
    
    public void removeFromBlockedList(String currentUser, String targetUser) {
    	blockedUserDb.updateOne(eq("userId", currentUser), Updates.pull("blockedUsers", targetUser));
	}


}
