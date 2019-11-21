import React, { Component } from 'react';
import axios from 'axios'

class CreateAlbum extends Component {
    constructor(props) {
        super(props);
        this.state = { name:null  };
        
    }

    onSubmit= (e)=>{
        e.preventDefault()

        axios.post(`http://localhost:8081/createalbum?name=${this.state.name}`).then(res=>{
            console.log(res)
        }).catch(error=>{
            console.log(error)
        })

        this.setState({name:''})

    }

   

    onChange = (e)=>{
       this.setState({ [e.target.name] : e.target.value})
    }


    render() {
        console.log(this.state)
        return (
            <form class = "" style = {{marginLeft:'400px',marginTop:'20px'}} onSubmit = {this.onSubmit}>
            <label for="name">Album Name</label>
            <input type="text" name="name" class="name text-field" placeholder="Album Name" onChange = {this.onChange} />  
            <input type="submit" name="submit" class="def-button submit" value="Submit"/>
            </form>
        );
    }
}

export default CreateAlbum;