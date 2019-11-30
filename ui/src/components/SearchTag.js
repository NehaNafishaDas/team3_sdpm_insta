import React, { Component } from 'react';

class SearchTag extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    render() {
        return (
            <a href = {`/commentinfo/${this.props.id}/${this.props.data}`} >  <p onClick = {this.onClickUserProfile} class="notif-msg" >
            <span class="username" style = {{paddingTop:'10px'}}>#{this.props.tag}</span>
            </p></a>
        );
    }
}

export default SearchTag;