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
      this.getProfile()
      this.getLocation()
      
    }

    checkLogin= ()=>{
        axios.get('http://localhost:8081/loginstatus').then(res=>{
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
        data.append("location",this.state.location)

        axios.post(`http://localhost:8081/imagepost`,data,{headers: {
            'Content-Type': 'multipart/form-data'
          }}).then(res=>{    
            console.log(res)
            this.props.getAccountPicture()
            this.props.getFollowersInfo()
        }).catch(error=>{

        })

        this.setState({description:"",selectedFile:null})
        
    }

    onUpload =(e)=>{
        this.setState({selectedFile:  e.target.files[0]})
    }

    onChange = (e)=>{
        this.setState({[e.target.name]:e.target.value})
    }

    getProfile = ()=>{
        const { username} = this.props
        axios.get(`http://localhost:8081/getuser?userid=${username}`).then(res=>{
            this.setState({profilepicture:res.data.profilepicture})
        })

    }

    getLocation=()=>{
        navigator.geolocation.getCurrentPosition((position)=>{
            axios.get(`https://us1.locationiq.com/v1/reverse.php?key=3858248baae9c2&lat=${position.coords.latitude}&lon=${position.coords.longitude}&format=json`,{
                withCredentials: false}).then(res=>{
                console.log(res.data.address)
                this.setState({address:res.data.address})
            })     
        })
    }

    render() {
        const {profilepicture,address}  = this.state
        console.log(address)
       
       console.log(this.state)
        const state = address ? <option name = "location" onChange = {this.onChange} value={address.state}>{address.state}</option>:null
        const country= address ? <option name = "location" onChange = {this.onChange} value={address.country}>{address.country}</option>:null
        const city = address ? <option name = "location" onChange = {this.onChange} value={address.city}>{address.city}</option>:null
        const county = address ? <option name = "location" onChange = {this.onChange} value={address.county}>{address.county}</option>:null
        const neighbourhood = address ? <option name = "location" onChange = {this.onChange} value={address.neighbourhood}>{address.neighbourhood}</option>:null
        const road = address ? <option name = "location" onChange = {this.onChange} value={address.road}>{address.road}</option>:null
    
        const  avatarMini = profilepicture ?  <div class="avatar-medium user-image" style={{backgroundImage : "url('" +profilepicture+ "')",backgroundSize : "cover",backgroundPosition : 'center'}}></div>:null
        return (
            <div class="edit-profile-view active">
            <div class="cancel-icon-white close-view" onClick={this.handleModalClose}></div>
            <form class="edit-profile-form"onSubmit = {this.onSubmit}>
                <div class="header clearfix">
                   {avatarMini}
                    <h2 class="username">{this.props.username}</h2>
                </div>
                <label for="name">Caption</label>
                <input type="text" name="description" class="name text-field" placeholder="enter your caption ..." onChange = {this.onChange} />
                <label for="username">Photo</label>
                <input type="file" class="name text-field" name = "images" id="files" onChange = {this.onUpload}/>
                <label for="name">Tags</label>
                <input type="text" name="tags" class="name text-field" placeholder="tag names ..." onChange = {this.onChange} />
                <select name = "location" onChange = {this.onChange}  class="name text-field">
                    <option selected>Select Your Location</option>
                   {state}
                   {country}
                   {city}
                   {county}
                   {neighbourhood}
                   {road}
                </select>
                <input type="submit" name="submit" class="def-button submit" value="Submit"/>
                
            </form>
            </div>


        );
    }
}

export default Post;