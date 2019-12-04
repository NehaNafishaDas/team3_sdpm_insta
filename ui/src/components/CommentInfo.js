import React, { Component } from 'react';

import axios from 'axios'
import LikeIcon from './LikeIcon'
import Comments from './Comments'
import CommentWithImage from './CommentWithImage'
import NavBar from './NavBar'

class CommentInfo extends Component {
    constructor(props) {
        super(props);
        this.state = { activeViewPost:false,activeHover:null, activeCommentWithPictureModal:false,Image:[] ,imagePost:null ,activeLike:null,selectedFile:null};
    }

    componentDidMount(){
        const id = this.props.match.params.id
        this.onClickViewPost(id)
    }

    onClickViewPost(id){
        this.userPostDetails(id)
        this.setState({id:id})
        const username = this.props.match.params.username
        axios.get(`http://localhost:8081/isliked?postid=${id}&username=${username}`).then(res=>{
            this.setState({liked:res.data.liked})
        }).catch(error=>{

        })

        this.getComments(id)
        
    }

    

    userPostDetails = (id)=>{
		axios.get(`http://localhost:8081/getpost?postid=${id}`).then(res=>{
            console.log(res.data.tags)
            this.setState({userData:res.data})
            this.setState({tags:res.data.tags})
		}).catch(error=>{

        })
    }

    checkLogin= ()=>{
        axios.get('http://localhost:8081/loginstatus').then(res=>{
            this.setState({username:res.data.username})
            if(res.data.status === "notloggedin"){
                this.props.history.push("/login")
            }
        })
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

    commentingWithPicture=()=>{
        this.setState({ activeCommentWithPictureModal : "activeCommentingWithPictureModal"}) 
    }

     handleModalClose=()=>{
        this.setState({ activeCommentWithPictureModal:false})
    }






    render() {
        const {userData,liked,userCommentData,tags,activeCommentWithPictureModal} = this.state
        const username = this.props.match.params.username
        const CommentList = userCommentData ? ( userCommentData.map(comment =>{  
            if(comment.comment !== "" )    
            return( 
                
                <li class="comment"><span class="username">{comment.username}</span> {comment.comment}</li>
            )
         }) ): null

        const CommentImage = userCommentData ? ( userCommentData.map(comment =>{    
            if(comment.image)  
            return( 
                <li  class="comment" style = {{maxWidth:300}}> <span class="username">{comment.username}  </span><img src ={comment.image} style = {{maxWidth:300}}alt = "...."/></li>
            )
            }) ): null

        const Tags = tags ? ( tags.map(tag =>{    
             console.log(tag)
            return( 
                <p style ={ {paddingBottom:10,paddingLeft:5,paddingRight:5,display: 'inline'}}>#{tag}</p>
            )
            }) ): null


        const UserDetail = userData?<div class="post-image" style={{backgroundImage : "url('" +userData.images[0]+ "')",backgroundSize : "cover",backgroundPosition : 'center'}} ></div>:null
        const UserLike = userData?<h4 class="post-likes data">{userData.likes} likes</h4>:null
        const datePosted = userData? <h4 class="post-time data">{userData.date}</h4> :null
        const caption = userData? <p class="post-caption"><span class="username">{username} </span>{userData.description}</p>:null
        const smallFontUserName  = username ? <h5 class="username">{username}</h5>:null
        const location =  userData?  <p style = {{fontSize: 13, paddingTop:4}}>{userData.location}</p>: null
        return (

           <div>
               <NavBar/>
            <div class="view-post-active" style = {{marginTop:'70px', marginLeft:'50px'}}>
			<div class="cancel-icon-white close-view" onClick = {this.handlePostModalClose}></div>
			
            <div class="post-view clearfix">
						{UserDetail}
						<div class="post-view-details">
                        <div class="header clearfix">
								<div class="avatar-medium user-image"></div>   
                                {smallFontUserName}
                                {location}
							</div>
							<div class="post-analysis clearfix">
                                {UserLike}
                                {datePosted}		
							</div>
                            {caption}
                           {Tags}
                           <br></br>
                           <br></br>
							<ul class="post-comments">
                                {CommentList}
                                {CommentImage}
							</ul>
							<div class="post-actions clearfix">
                            {liked === "true"? <LikeIcon onLikePost = {this.onLikePost}/>: <div class="like-icon like-post " onClick = {this.onLikePost}></div>}
                            <Comments id = {this.state.id} userPostDetails = {this.userPostDetails} onClickViewPost = {this.onClickViewPost} getAccountPicture = {this.getAccountPicture}/>
                            <i class="fas fa-images add-button" onClick={this.commentingWithPicture}></i>
							</div>
						</div>
					</div>
                    {activeCommentWithPictureModal==="activeCommentingWithPictureModal"?<CommentWithImage getComments = {this.getComments} id = {this.state.id} handleModalClose = {this.handleModalClose}/>:null}
		</div>
           </div>
        );
    }
}

export default CommentInfo;