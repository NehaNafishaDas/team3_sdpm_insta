import React, { Component } from 'react';
import {Redirect,Link} from 'react-router-dom'
class SearchResult extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    onClickUserProfile=()=>{
        this.props.onClickUserProfile()
    }

    render() {
       
        return (
            
                <a href = {`/otherusers/${this.props.data}`} >  <p onClick = {this.onClickUserProfile} class="notif-msg" >
                <span class="username" >{this.props.data}</span>
                </p></a>
        
             
      
        );
    }
}

export default SearchResult;