import React, { Component } from 'react';
import ViewComments from './ViewComments'
import LikeIcon from './LikeIcon'
import {Link} from 'react-router-dom'

import axios from 'axios'
import CommentOnFeed from './CommentOnFeed';

class UserFollowersList extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    componentWillMount(){
        this.isLiked()
        
    }
       onLikePost=(e)=>{
            e.target.classList.toggle('active');
            axios.post(`http://13.82.84.219/liketoggle?postid=${this.props.follower._id}`).then(res=>{
                    this.props.getFollowersInfo()
            }).catch(error=>{
            }) 
       }
   
       
    isLiked=(id)=>{
        axios.get(`http://13.82.84.219/isliked?postid=${this.props.follower._id}&username=${this.props.username}`).then(res=>{
            this.setState({liked:res.data.liked})
        }).catch(error=>{

        })
    }
   
    render() {
        const {liked} = this.state
        return (
            <li class="post">
            <div class="post-header clearfix">
                <div class="user-image avatar-def img1"></div>
                <Link to={{pathname: '/otherusers',state: {username:this.props.follower.username}}} activeClassName="current"><h4 class="username">{this.props.follower.username}</h4></Link>
                <h4 class="post-time">{this.props.follower.date}</h4>
            </div>
            <div class="post-image"style={{backgroundImage : "url('" +this.props.follower.imageId+ "')",backgroundSize : "cover",backgroundPosition : 'center'}} ></div>
            <h4 class="post-likes">{this.props.follower.likes} likes</h4>
            <p class="post-caption"><span class="username">{this.props.follower.username} </span> {this.props.follower.description}</p>
            <ul class="post-comments">
               <ViewComments  comments = {this.props.follower.comments}/>
            </ul>
            <div class="post-actions clearfix">
            {liked === "true"? <LikeIcon onLikePost = {this.onLikePost}/>: <div class="like-icon like-post " onClick = {this.onLikePost}></div>}
                <CommentOnFeed getFollowersInfo = {this.props.getFollowersInfo} id = {this.props.follower._id} />
            </div>
          </li>
    
        );
    }
}

export default UserFollowersList;