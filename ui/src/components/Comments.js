import React, { Component } from 'react';
import axios from 'axios'

class Comments extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    handleKeyPress=(e)=>{
        console.log(e)
        console.log(this.props.id)
        if(e.key==='Enter'){
            e.preventDefault();
            axios.post(`http://13.82.84.219/writecomment?postid=${this.props.id}&comment=${e.target.value}`).then(res=>{
                console.log(res)
                this.props.userPostDetails(this.props.id)
                this.props.onClickViewPost(this.props.id)
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