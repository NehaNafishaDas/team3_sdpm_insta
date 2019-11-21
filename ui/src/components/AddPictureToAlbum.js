import React, { Component } from 'react';
import axios from 'axios';

class AddPictureToAlbum extends Component {
    constructor(props) {
        super(props);
        this.state = { selectedFile:null, album:[] };
    }

    componentDidMount(){
        this.getAlbum()
    }

    getAlbum = () =>{
        axios.get('http://localhost:8081/getallalbums').then(res=>{
             this.setState({album:Object.values(res.data)})

       }).catch(error=>{

        })
    }

    onSubmit = (e)=>{
        e.preventDefault()

        let data = new FormData()

        data.append("image", this.state.selectedFile)

        axios.post(`http://localhost:8081/addimagetoalbum?album=${this.state.albumname}`,data,{headers: {
            'Content-Type': 'multipart/form-data'
          }}).then(res=>{
            console.log(res)
        }).catch(error=>{

        })

        this.props.handleModalClose()
        
    }

    onUpload = (e)=>{
        this.setState({selectedFile: e.target.files[0]})
    }

    onChange = (e) =>{
        this.setState({[e.target.name] : e.target.value})
    }
    render() {
        const {album} = this.state

      console.log(this.state)
      
        const AlbumList = album.length ? (album.map(albums=>{
           
            return (
               
                <option name = "album" value = {albums.name} onChange = {this.onChange} >{albums.name}</option> 
             
            )
        })):null
    

        return (
            <form class = "" style = {{marginLeft:'430px',marginTop:'20px'}} onSubmit = {this.onSubmit}>
            <label for="name">Picture Name</label>
            <input type="file" class="name text-field" name = "image" id="files" onChange = {this.onUpload}/>
            <label for="name">Album Name</label>
            <select name = "albumname"  onChange = {this.onChange} class="name text-field">
               <option selected>Select The Album</option>
               {AlbumList}
            </select>
            <input type="submit" name="submit" class="def-button submit" value="Submit"/>
            </form>
        );
    }
}

export default AddPictureToAlbum;