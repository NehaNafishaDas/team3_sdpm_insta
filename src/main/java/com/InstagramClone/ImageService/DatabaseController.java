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
import java.util.regex.Pattern;

import com.InstagramClone.model.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;


public class DatabaseController {
    private static DatabaseController single_instance = null; 
	
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Image> imageDb;
	private MongoCollection<Post> postDb;
	private MongoCollection<Account> accountDb;
	private MongoCollection<Album> albumDb;

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
		CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		database = mongoClient.getDatabase("db");
	    database = database.withCodecRegistry(pojoCodecRegistry);
	    imageDb = database.getCollection("Images", Image.class);
	    postDb = database.getCollection("Posts", Post.class);
	    postDb.createIndex(Indexes.text("description"));
	    accountDb = database.getCollection("Accounts", Account.class);
		albumDb = database.getCollection("Albums", Album.class);
	}

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
//	public void createPrivacy(Privacy privacy) throws NoSuchAlgorithmException {
//		privacyDb.insertOne(privacy);
//	}
//
//	public void createBlockList(BlockedUser blockedUsers) throws NoSuchAlgorithmException {
//		blockedUserDb.insertOne(blockedUsers);
//	}
	
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

	public boolean followUser(ObjectId targetAccount, ObjectId currentAccount) {
		Account target = getAccount(targetAccount);
		if(target.isPrivate && !target.followedUsers.contains(currentAccount)){
			return false;
		}
		accountDb.updateOne(eq("_id", currentAccount), Updates.addToSet("followedUsers", targetAccount));
		accountDb.updateOne(eq("_id", targetAccount), Updates.addToSet("followedBy", currentAccount));
		return true;
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

	public ArrayList<String> searchUsername(String search) {
		Pattern regex = Pattern.compile(search, Pattern.CASE_INSENSITIVE);
		Bson filter = Filters.eq("username", regex);
		MongoCursor<Account> iterator = accountDb.find(filter).iterator();
		ArrayList<String> list = new ArrayList<>();
		while(iterator.hasNext()) {
			Account a = iterator.next();
			list.add(a.getUsername());
		}
		return list;
	}

	// Return account object given an username as a string
	public Account getAccount(String username) {
		return accountDb.find(eq("username", username)).first();
	}

	public Post getPost(ObjectId id){
		return postDb.find(eq("_id", id)).first();
	}

	public ArrayList<Post> getPost(String description){
		FindIterable<Post> posts  = postDb.find(Filters.text(description));
		MongoCursor<Post> iterator = posts.iterator();
		ArrayList<Post> response = new ArrayList<>();
		while(iterator.hasNext()) {
			response.add(iterator.next());
		}
		return response;
	}

	public boolean getPrivacy(String username) {
		Account account = getAccount(username);
		return account.isPrivate;
	}
	
	public ArrayList<ObjectId> getBlockList(String username) {
		Account account = getAccount(username);
		return account.blockedUsers;
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

	public Album createAlbum(ObjectId accId, String name) {
		Account account = getAccount(accId);
		Album album = new Album(account._id, account.getUsername(), name);
		accountDb.updateOne(eq("_id", account._id), Updates.addToSet("albums", album._id));
		albumDb.insertOne(album);
		return album;
	}

	public Album getAlbum(ObjectId id) {
		return albumDb.find(eq("_id", id)).first();
	}

	public void addUserToAlbum(ObjectId albumId, String username) {
		Album album = getAlbum(albumId);
		if(album == null) return;
		Account account = getAccount(username);
		accountDb.updateOne(eq("_id", account._id), Updates.addToSet("albums", album._id));
		albumDb.updateOne(eq("_id", albumId), Updates.addToSet("group", account._id));
	}

	public void addImageToAlbum(ObjectId albumId, String imageUrl) {
		Album album = getAlbum(albumId);
		if(album == null) return;
		albumDb.updateOne(eq("_id", albumId), Updates.addToSet("images", imageUrl));
	}

	public ArrayList<ObjectId> getAllAlbums(ObjectId accId) {
		Account account = getAccount(accId);
		ArrayList<ObjectId> response = account.getAlbums();
		return response;
	}

	public void removeUserFromAlbum(String username, ObjectId albumId) {
		Account account = getAccount(username);
		accountDb.updateOne(eq("_id", account._id), Updates.pull("albums", albumId));
		albumDb.updateOne(eq("_id", albumId), Updates.pull("group", account._id));
	}


    public static DatabaseController getInstance()
    { 
        if (single_instance == null) 
            single_instance = new DatabaseController(); 
  
        return single_instance; 
    }
    
    public void changePrivacy(ObjectId currentUserId, boolean isPrivate) {
    	accountDb.updateOne(eq("_id", currentUserId), Updates.set("isPrivate", isPrivate));
	}
  
    public void addToBlockedList(ObjectId currentUser, ObjectId targetUser) {
		Account account = accountDb.find(eq("_id", currentUser)).first();
		if(!account.blockedUsers.contains(targetUser)) {
			accountDb.updateOne(eq("_id", currentUser), Updates.addToSet("blockedUsers", targetUser));
		}
	}
    
    public void removeFromBlockedList(ObjectId currentUser, ObjectId targetUser) {
		accountDb.updateOne(eq("_id", currentUser), Updates.pull("blockedUsers", targetUser));
	}

}
