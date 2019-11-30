import React, { Component } from 'react';

class ViewPicture extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    onClickViewPost=()=> {
        if(this.props.onClickViewPost)
            this.props.onClickViewPost(this.props.keyy);
    }

    onMouseHoverView = (e)=>{
        e.target.classList.add('user-post-hover');
    }
    onMouseOut = (e)=>{
        e.target.classList.remove('user-post-hover');
    }
    
    render() {
        return (
            
           
            <li class="user-post"  style={{backgroundImage : "url('" + this.props.images + "')",backgroundSize : "cover",backgroundPosition : 'center'}}  onClick = {this.onClickViewPost} onMouseOut = {this.onMouseOut} onMouseOver = {this.onMouseHoverView}>
            <div class="post-details">
                <div class="details clearfix">
                    <h4 class="data likes">{this.props.likes}</h4>
                    <h4 class="data comments">{this.props.comment}</h4>
                </div>
            </div>
            </li>
       
        );
    }
}

export default ViewPicture;