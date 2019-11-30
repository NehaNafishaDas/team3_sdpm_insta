import React, { Component } from 'react';
import axios from "axios"
class RemoveAlbum extends Component {
    constructor(props) {
        super(props);
        this.state = { album:[] };
    }

    componentWillMount(){
        this.getUser()
        this.getAlbum()
    }

    onSubmit = (e)=>{
        e.preventDefault()
        axios.delete(`http://localhost:8081/removeuserfromalbum?album=${this.state.albumname}&username=${this.state.followers}`).then(res=>{
            console.log(res)
        }).catch(error=>{
            console.log(error)
        })
        this.props.handleModalClose()
    }

    onChange = (e)=>{
        this.setState({ [e.target.name] : e.target.value})
     }
 
     getUser=()=>{
         axios.get('http://localhost:8081/getfollowers').then(res=>{
             console.log(Object.values(res.data))
             this.setState({users:Object.values(res.data)})
         }).then(error=>{
 
         })
     }

     getAlbum = () =>{
        axios.get('http://localhost:8081/getallalbums').then(res=>{
             this.setState({album:Object.values(res.data)})

       }).catch(error=>{

        })
    }

    render() {
        const {album,users} = this.state

        console.log(this.state)
        
          const AlbumList = album.length ? (album.map(albums=>{
             
              return (
                 
                  <option name = "albumname" value = {albums.name} onChange = {this.onChange} >{albums.name}</option> 
               
              )
          })):null

          const UsersList = users ? (users.map(user=>{
            return (
               
                <option name = "followers" value = {user} onChange = {this.onChange} >{user}</option> 
             
            )
        })):null
        return (
           <form class = "" style = {{marginLeft:'430px',marginTop:'20px'}} onSubmit = {this.onSubmit}>
            <label for="name">User Name</label>
            <select name = "followers" onChange = {this.onChange} class="name text-field">
               <option selected>Select Your User</option>
              {UsersList}
            </select>
            <label for="name">Album Name</label>
            <select name = "albumname" onChange = {this.onChange} class="name text-field">
               <option selected>Select The Album</option>
              {AlbumList}
            </select>
            <input type="submit" name="submit" class="def-button submit" value="Submit"/>
            </form> 
        );
    }
}

export default RemoveAlbum;