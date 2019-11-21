import React, { Component } from 'react';
import axios from 'axios'

import {Link} from 'react-router-dom'

class AlbumList extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    componentWillMount(){
        this.getAlbum()
    }

    getAlbum = () =>{
        axios.get('http://localhost:8081/getallalbums').then(res=>{
            console.log(res)
             this.setState({album:Object.values(res.data)})
    }).catch(error=>{

        })
    }
    
    render() {
        const {album} = this.state
      
            const AlbumList = album ? album.map(album=>{
               
                return (
                    <Link to  = {{pathname: '/album', state:{keyy : album.name }}}>
                    <li>{album.name}</li>
                   </Link>
                )
            }):null
        
       
        return (
           <div class="list4" style = {{marginLeft:'430px',marginTop:'20px'}}>
                <ul>
                   {AlbumList}
                </ul>
          </div>
        );
    }
}

export default AlbumList;