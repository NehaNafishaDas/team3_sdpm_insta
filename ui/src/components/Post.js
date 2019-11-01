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

    handleModalClose(){
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
            <div>
               <div className="custom-modal">
                <div className="modal-container">
                    <i class="fas fa-times fonts" onClick={this.handleModalClose.bind(this)}></i>
                    <form class="" onSubmit = {this.onSubmit}>
                    <div class="form-group">
                        <input type="text" class="form-control" name="descripton" onChange = {this.onChange} placeholder="caption"/>   
                    </div>
                    <div class="custom-file form-group">
                        <input type="file" class="custom-file-input" name = "images"  onChange = {this.onUpload} id="files"/>
                        <label class="custom-file-label" for="customFile">Choose file</label>
                    </div>
                    <button type="submit" name = "submit" class="btn btn-primary">Submit</button>
                    </form>
                   
                </div>
             </div>

            </div>
        );
    }
}

export default Post;