import React, { Component } from 'react';
import axios from 'axios';
import NavBar from './NavBar'
import ViewPicture from './ViewPicture'
import LikeIcon from './LikeIcon'
import Comments from './Comments'

class OtherUserProfiles extends Component {
    constructor(props) {
        super(props);
        this.viewPost = React.createRef();
        this.onClickViewPost = this.onClickViewPost.bind(this)
        this.state = { Image:[] };
        this.checkLogin()
    }
    componentWillMount(){
        this.getAccountProfile()
        this.isFollowing()
    }

    getAccountProfile = ()=>{
        const { username} = this.props.location.state
      //  if(username = "")
        axios.get(`http://localhost:8081/getuser?userid=${username}`).then(res=>{
           
            const Images = Object.values(res.data.posts)
            this.setState({Image:Images});
            this.setState({userDetails:res.data})
        })
    }
    
    onClickViewPost(id){
        this.viewPost.current.classList.add('view-post-active');
        this.userPostDetails(id)
        this.setState({id:id})
       // const { username} = this.props.location.state
        axios.get(`http://localhost:8081/isliked?postid=${id}&username=${this.state.loggedInUsername}`).then(res=>{
            this.setState({liked:res.data.liked})
        }).catch(error=>{

        })

        this.getComments(id)
        
    }

    userPostDetails = (id)=>{
		axios.get(`http://localhost:8081/getpost?postid=${id}`).then(res=>{
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

    getComments = (id)=>{
        axios.get(`http://localhost:8081/getcommentsfrompost?postid=${id}`).then(res=>{
            const Comments= Object.values(res.data)
           this.setState({userCommentData:Comments})
        })
    }

    onLikePost=(e)=>{
           e.target.classList.toggle('active');
          axios.post(`http://localhost:8081/liketoggle?postid=${this.state.id}`).then(res=>{
                this.setState({liked:res.data.liked})
                this.userPostDetails(this.state.id)
                this.onClickViewPost(this.state.id)
                this.getAccountPicture()
            }).catch(error=>{
    
            })
      
    }

    checkLogin= ()=>{
        axios.get('http://localhost:8081/loginstatus').then(res=>{
            this.setState({loggedInUsername:res.data.username})
            if(res.data.status === "notloggedin"){
                this.props.history.push("/login")
            }
        })
    }

    onFollow = (e)=>{
        e.target.classList.toggle('follow');
        //e.target.innerHTML.toggle('follow');
       // e.target.innerHTML  = "following" ?  e.target.innerHTML  = "follow" : e.target.innerHTML  = "following"
       
        const { username} = this.props.location.state
        axios.post(`http://localhost:8081/followtoggle?targetaccount=${username}`).then(res=>{
            console.log(res)
            this.isFollowing()
            this.getAccountProfile()
        }).catch(error=>{
            console.log(error)
        })
    }

    isFollowing=()=>{
        const { username} = this.props.location.state
        axios.get(`http://localhost:8081/isfollowing?targetaccount=${username}`).then(res=>{
            this.setState({isfollowing:res.data.isfollowing})
        }).catch(error=>{

        })
    }

    render() {
        
            const {Image,userData,liked,userCommentData,isfollowing,userDetails,loggedInUsername} = this.state
            console.log(loggedInUsername)
            console.log(isfollowing)
            const { username} = this.props.location.state
           console.log(Image)
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
            const isFollowing = isfollowing === true ?<button class="def-button edit-profile" onClick={this.onFollow}>Following</button>:<button class="def-button follow" onClick={this.onFollow}>Follow</button>
            const followedUsersCount = userDetails ? <p class="data followers"><span class="value">{userDetails.followercount}</span> followers</p>:null
            const followingUsersCount = userDetails ?  <p class="data following"><span class="value">{userDetails.followingcount}</span> following</p>: null
            const bio = userDetails ? <p class="name">{userDetails.bio}</p> :null
            const name = userDetails ? <p class="bio">{userDetails.firstName} {userDetails.lastName}</p> : null
            const avatar = userDetails ?  <div class="avatar-display user-image" style={{backgroundImage : "url('" + userDetails.profilepicture + "')",backgroundSize : "cover",backgroundPosition : 'center'}}></div>:null
            return (
            <div class = "container">
            <NavBar getAccountPicture = {this.getAccountPicture} username = {loggedInUsername}/>
            <div id="body">
            <div class="user-profile">
                <div class="profile-brief clearfix">
                    {avatar}
                    <div class="profile-info">
                        {userName}
                         {/* <button class="def-button edit-profile" onClick={this.onEditProfile}>Edit Profile</button>  */}
                       { isFollowing}
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
                                <i class="fas fa-images add-button"></i>
                                 </div>
                            </div>
                        </div>			
            </div>
            
            
           
        </div>
       
            </div>
             
            );
        }
    }


export default OtherUserProfiles;