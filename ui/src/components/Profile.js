import React, { Component } from 'react';
import NavBar from './NavBar'
import axios from 'axios'
import ViewPicture from './ViewPicture';
import LikeIcon from './LikeIcon';
import Comments from './Comments';

class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = { activeViewPost:false,activeHover:null, Image:[] ,imagePost:null ,activeLike:null,selectedFile:null};
        this.viewPost = React.createRef();
        this.editProfile = React.createRef();
        this.onClickViewPost = this.onClickViewPost.bind(this)
        this.onEditProfile = this.onEditProfile.bind(this)
    }

    componentWillMount(){
        this.checkLogin()
        this.getAccountPicture()
    }

    onClickViewPost(id){
        this.viewPost.current.classList.add('view-post-active');
        this.userPostDetails(id)
        this.setState({id:id})

        axios.get(`http://13.82.84.219/isliked?postid=${id}&username=${this.state.username}`).then(res=>{
            this.setState({liked:res.data.liked})
        }).catch(error=>{

        })

        this.getComments(id)
        
    }

    userPostDetails = (id)=>{
		axios.get(`http://13.82.84.219/getpost?postid=${id}`).then(res=>{
            this.setState({userData:res.data})
		}).catch(error=>{

        })
    }

    handlePostModalClose = ()=>{
        this.viewPost.current.classList.remove('view-post-active');
    }

    handleEditModalClose = ()=>{
        this.editProfile.current.classList.remove('active');
    }

    onEditProfile(){
        this.editProfile.current.classList.add('active');
     }

    onMouseHoverView = (e)=>{
        e.target.classList.add('user-post-hover');
    }
    onMouseOut = (e)=>{
        e.target.classList.remove('user-post-hover');
    }

    checkLogin= ()=>{
        axios.get('http://13.82.84.219/loginstatus').then(res=>{
            this.setState({username:res.data.username})
            if(res.data.status === "notloggedin"){
                this.props.history.push("/login")
            }
        })
    }

    getAccountPicture = ()=>{
        axios.get('http://13.82.84.219/accountposts').then(res=>{
            console.log(res)
            const Images = Object.values(res.data.posts)
            this.setState({Image:Images});
            this.setState({userDetails:res.data})

        }).catch(error=>{

        })
    }

    getComments = (id)=>{
        axios.get(`http://13.82.84.219/getcommentsfrompost?postid=${id}`).then(res=>{
            const Comments= Object.values(res.data)
           this.setState({userCommentData:Comments})
        })
    }

    onLikePost=(e)=>{
           e.target.classList.toggle('active');
            axios.post(`http://13.82.84.219/liketoggle?postid=${this.state.id}`).then(res=>{
                this.setState({liked:res.data.liked})
                this.userPostDetails(this.state.id)
                this.onClickViewPost(this.state.id)
                this.getAccountPicture()
            }).catch(error=>{
    
            })
      
    }
    onUpload =(e)=>{
        this.setState({selectedFile:  e.target.files[0]})
    }

    onChange = (e)=>{
        this.setState({[e.target.name]:e.target.value})
    }
    onSubmitProfile = (e) =>{
        e.preventDefault()

       var  data  = new FormData()
       data.append("image",this.state.selectedFile)
       data.append("firstname",this.state.firstName)
       data.append("lastname",this.state.lastName)
       data.append("bio",this.state.bio)
      // data.append("email",this.state.email)

       axios.put('http://13.82.84.219/updateprofile',data,{headers:{'Content-Type' :' multipart/data'}}).then(res=>{
        this.getAccountPicture()
            console.log(res)
       }).catch(error=>{

       })

      
       this.setState({firstname:'',lastname:'',bio:'',email: ''})
    }

    render() {
        const {Image,userData,liked,userCommentData,username,userDetails} = this.state
        console.log(this.state)
        const ImageList = Image.length ? ( Image.map(image =>{    
            const id = image._id
            const likes = image.likes
            const comments = image.comments.length
            const images = image.imageId[0].toString()
            return( 
                      
               <ViewPicture images ={images} comment = {comments} likes = {likes} onClickViewPost = {this.onClickViewPost} keyy = {id}/>
            )
            }) ): null


            const CommentList = userCommentData ? ( userCommentData.map(comment =>{    
                return( 
                    <li class="comment"><span class="username">{comment.username}</span> {comment.comment}</li>
                )
                }) ): null



        const UserDetail = userData?<div class="post-image" style={{backgroundImage : "url('" +userData.images[0]+ "')",backgroundSize : "cover",backgroundPosition : 'center'}} ></div>:null
        const UserLike = userData?<h4 class="post-likes data">{userData.likes} likes</h4>:null
        const datePosted = userData? <h4 class="post-time data">{userData.date}</h4> :null
        const caption = userData? <p class="post-caption"><span class="username">{username} </span>{userData.description}</p>:null
        const userName  = username ? <h1 class="username">{username}</h1>:null
        const smallFontUserName  = username ? <h5 class="username">{username}</h5>:null
        const followedUsersCount = userDetails ? <p class="data followers"><span class="value">{userDetails.followercount}</span> followers</p>:null
        const followingUsersCount = userDetails ?  <p class="data following"><span class="value">{userDetails.followingcount}</span> following</p>: null
        const bio = userDetails ? <p class="bio">{userDetails.bio}</p> :null
        const name = userDetails ? <p class="name">{userDetails.firstName} {userDetails.lastName}  </p> : null
        const avatarMedium = userDetails ? <div class="avatar-medium user-image"  style={{backgroundImage : "url('" + userDetails.profilepicture + "')",backgroundSize : "cover",backgroundPosition : 'center'}}>></div>: null
        const avatar = userDetails ?  <div class="avatar-display user-image" style={{backgroundImage : "url('" + userDetails.profilepicture + "')",backgroundSize : "cover",backgroundPosition : 'center'}}></div>:null
            return (
            <div class = "container">
            <NavBar  username = {this.state.username}  getAccountPicture = {this.getAccountPicture}/>
            <div id="body">
            <div class="user-profile">
                <div class="profile-brief clearfix">
                    {avatar}
                    <div class="profile-info">
                        {userName}
                         {/* <button class="def-button edit-profile" onClick={this.onEditProfile}>Edit Profile</button>  */}
                         <button class="def-button edit-profile" onClick={this.onEditProfile}>Edit Profile</button> 
                        <div class="analysis clearfix">
                            <p class="data posts"><span class="value">{Image.length}</span> posts</p>
                            {followedUsersCount}
                           {followingUsersCount}
                        </div>
                        {name}
                        {bio}
                    </div>
                </div>
            </div>
		<ul class="user-post-list" >
			{ImageList}
		</ul>

        <div class="view-post" ref = {this.viewPost}>
			<div class="cancel-icon-white close-view" onClick = {this.handlePostModalClose}></div>
			
            <div class="post-view clearfix">
						{UserDetail}
						<div class="post-view-details">
                        <div class="header clearfix">
								<div class="avatar-medium user-image"></div>
								{smallFontUserName}
							</div>
							<div class="post-analysis clearfix">
                                {UserLike}
                                {datePosted}		
							</div>
							{caption}
							<ul class="post-comments">
								{CommentList}
							</ul>
							<div class="post-actions clearfix">
                            {liked === "true"? <LikeIcon onLikePost = {this.onLikePost}/>: <div class="like-icon like-post " onClick = {this.onLikePost}></div>}
                            <Comments id = {this.state.id} userPostDetails = {this.userPostDetails} onClickViewPost = {this.onClickViewPost} getAccountPicture = {this.getAccountPicture}/>
							</div>
						</div>
					</div>			
		</div>
		
		<div class="edit-profile-view" ref = {this.editProfile}>
			<div class="cancel-icon-white close-view" onClick={this.handleEditModalClose}></div>
			<form class="edit-profile-form" onSubmit = {this.onSubmitProfile}>
				<div class="header clearfix">
					{avatarMedium}
					<h2 class="username">{this.state.username}</h2>
				</div>
				<label for="name">First Name</label>
				<input type="text" name="firstName" class="name text-field" onChange = {this.onChange} placeholder="muiz"/>
                <label for="name">Last Name</label>
				<input type="text" name="lastName" class="name text-field" onChange = {this.onChange}  placeholder="muiz"/>
				<label for="bio">Bio</label>
				<textarea class="text-field bio" name = "bio" onChange = {this.onChange} placeholder="Design, Code, Art"></textarea>
                <label for="username"> Profile Photo</label>
                <input type="file" class="name text-field" name = "profilepicture" id="files" onChange = {this.onUpload}/>
                
				<input type="submit" name="submit" class="def-button submit" value="Submit"/>
			</form>
		</div>
       
	</div>
   
        </div>
         
        );
    }
}

export default Profile;