import React, { Component } from 'react';
import axios from 'axios';

class SearchSimilarImages extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    onUpload = (e)=>{
        this.setState({selectedFile:e.target.files[0]})
    }


    onSubmit = (e)=>{
        e.preventDefault()
        var data = new FormData()
        data.append("image",this.state.selectedFile)
        axios.post('http://localhost:8081/duplicateimagesearch',data,{
            headers:{
                'Content-Type':'multipart/form-data'
            }
        }).then(res=>{
            console.log(res)
        }).catch(error=>{

        })
        this.props.handleModalClose()
    }
    render() {
        console.log(this.state)
        return (
            <form class="" style = {{marginLeft:'400px',marginTop:'20px'}} onSubmit = {this.onSubmit}>
                <p style = {{padding:"30px 40px 20px "}}><h4>Search Similar Images</h4></p>
            <label for="username">photo</label>
            <input type="file" class="name text-field" name = "images" id="files" onChange = {this.onUpload}/>
        
            <input type="submit" name="submit" class="def-button submit" value="Submit"/>
            
           </form>
        );
    }
}

export default SearchSimilarImages;