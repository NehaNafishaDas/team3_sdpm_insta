import React, { Component } from 'react';
import axios from 'axios'


class commentWithImage extends Component {
    constructor(props) {
        super(props);
        this.state = {  activePostModal:false, selectedFile:null };
    }
    handleModalClose = ()=>{
        this.props.handleModalClose()
    }

    onUpload = (e)=>{
        this.setState({selectedFile:  e.target.files[0]})
    }

    onSubmit= (e)=>{
        e.preventDefault()
        console.log(this.state.selectedFile)
        console.log(this.props.id)
        var data = new FormData()
        data.append("image",this.state.selectedFile)
        data.append("postid",this.props.id)
      
        axios.post(`http://13.82.84.219/writecommentimage`,data,{headers: {
            'Content-Type': 'multipart/form-data'
          }}).then(res=>{    
            console.log(res)
            this.props.getComments(this.props.id)
            this.handleModalClose() 
        }).catch(error=>{

        })
      
           
        
       
       

    }

    render() {
        return (
            <div class="edit-profile-view active">
            <div class="cancel-icon-white close-view" onClick={this.handleModalClose}></div>
            <form class="edit-profile-form"onSubmit = {this.onSubmit}>
                <div class="header clearfix">
                </div>
                
                <label for="username">Picture Commenting</label>
                <input type="file" class="name text-field" name = "image" id="files" onChange = {this.onUpload}/>
                
                <input type="submit" name="submit" class="def-button submit" value="Submit"/>
            </form>
            </div>

        );
    }
}

export default commentWithImage;