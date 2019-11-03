import React, { Component } from 'react';
import axios from 'axios';
axios.defaults.withCredentials = true;
class Post extends Component {
    constructor(props) {
        super(props);
        this.state = { selectedFile:null };
    }

    componentWillMount(){
      this.checkLogin()
      
    }


    checkLogin= ()=>{
        axios.get('http://13.82.84.219/loginstatus').then(res=>{
            if(res.data.status === "notloggedin"){
                console.log(res)
                this.props.history.push("/login")
            }
        })
    }

    handleModalClose=()=>{
        if(this.props.handleModalClose)
            this.props.handleModalClose()
    }

    onSubmit = (e)=>{
        e.preventDefault()

        var data = new FormData()
        data.append("images",this.state.selectedFile)
        data.append("description",this.state.description)

        axios.post(`http://13.82.84.219/imagepost`,data,{headers: {
            'Content-Type': 'multipart/form-data'
          }}).then(res=>{    
            console.log(res)
          console.log( this.props.getAccountPicture()) 
        }).catch(error=>{

        })

        

        this.setState({description:"",selectedFile:""})
        
    }

    onUpload =(e)=>{
        this.setState({selectedFile:  e.target.files[0]})
    }

    onChange = (e)=>{
        this.setState({[e.target.name]:e.target.value})
    }


    render() {
        return (
            <div class="edit-profile-view active">
            <div class="cancel-icon-white close-view" onClick={this.handleModalClose}></div>
            <form class="edit-profile-form"onSubmit = {this.onSubmit}>
                <div class="header clearfix">
                    <div class="avatar-medium user-image"></div>
                    <h2 class="username">ak_muheez</h2>
                </div>
                <label for="name">Caption</label>
                <input type="text" name="description" class="name text-field" placeholder="muiz" onChange = {this.onChange} />
                <label for="username">Photo</label>
                <input type="file" class="name text-field" name = "images" id="files" onChange = {this.onUpload}/>
                
                <input type="submit" name="submit" class="def-button submit" value="Submit"/>
            </form>
            </div>


        );
    }
}

export default Post;