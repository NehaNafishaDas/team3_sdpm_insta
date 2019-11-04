import React, { Component } from 'react';
import {Link} from 'react-router-dom'

class ViewComents extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    render() {
        const {comments} = this.props
         const commentList = comments? ( comments.map(comment =>{    
             return( 
               
                 <li class="comment"> <Link to={{pathname: '/otherusers',state: {username:comment.username}}} activeClassName="current"><span class="username">{comment.username}  </span></Link>{comment.comment}</li>
             )
            }) ): null
        return (
            <ul class="post-comments">
                 {commentList}
            </ul>
          
          

        );
    }
}

export default ViewComents;