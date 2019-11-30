import React, { Component } from 'react';

class SearchAlreadyPosted extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    onSubmit = ()=>{

    }
    
    onChange = ()=>{

    }

    render() {
        return (
            <form class="" style = {{marginLeft:'400px',marginTop:'20px'}} onSubmit = {this.onSubmit}>
            <p style = {{padding:"30px 40px 20px "}}><h4>Search Already Posted Image</h4></p>
            <label for="username">photo</label>
            <input type="file" class="name text-field" name = "images" id="files" onChange = {this.onUpload}/>
        
            <input type="submit" name="submit" class="def-button submit" value="Submit"/>
            
           </form>
        );
    }
}

export default SearchAlreadyPosted;