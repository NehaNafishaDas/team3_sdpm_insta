import React, { Component } from 'react';
import axios from 'axios'

class Comments extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    handleKeyPress=(e)=>{
       
        if(e.key==='Enter'){
            e.preventDefault();
            axios.post(`http://localhost:8081/writecomment?postid=${this.props.id}&comment=${e.target.value}`).then(res=>{
                console.log(res)
                this.props.onClickViewPost(this.props.id)
                this.props.userPostDetails(this.props.id)
                this.props.getAccountPicture()
            }).catch(error=>{

            })
            e.target.value = ""
         }
    }

    render() {
        return (
            <input type="text" name="comment" class="comment text-field" placeholder="Add a comment..." onKeyDown={this.handleKeyPress}  handleKeyPress={this.handleKeyPress}/>
        );
    }
}

export default Comments;