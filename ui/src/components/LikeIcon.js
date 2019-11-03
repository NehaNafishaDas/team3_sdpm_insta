import React, { Component } from 'react';

class LikeIcon extends Component {
  onLikePost = (e)=>{
      this.props.onLikePost(e)
  }
    render() {
        return (
            <div class="like-icon like-post active " key = {this.props.keyy} onClick = {this.onLikePost}></div> 
        );
    }
}

export default LikeIcon;