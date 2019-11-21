import React, { Component } from 'react';
import NavBar from './NavBar'
import {Link} from 'react-router-dom'
import axios from 'axios';
class ViewAlbum extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    componentWillMount(){
        this.getAlbum()
    }

    getAlbum=()=>{
        const {keyy} = this.props.location.state
        axios.get(`http://localhost:8081/getalbum?name=${keyy}`).then(res=>{
            console.log(res.data)
            this.setState({images:res.data.images[0]})
            this.setState({data:res.data})
        })
    }

    render() {
        const {images,data} = this.state

        const {keyy} = this.props.location.state

        const imageList = images ? <li class="user-post"style={{backgroundImage : "url(" + images + ")",backgroundSize : "cover",backgroundPosition : 'center', marginTop:'150px', marginLeft:'50px'}}></li>:null
        const imageName = data ?  <p class  style = {{marginTop:'-190px', marginLeft :'90px', fontWeight:'350'}}>  {data.name} -{data.date}</p>:null
        return (

            <div >  
            <NavBar/>
            <ul class="user-post-list">
            <Link to  = {{pathname: '/picture',state:{keyy:keyy}}}>
                {imageList}
               
             </Link>    
            </ul> 
           {imageName}
            </div> 
        );
    }
}

export default ViewAlbum;