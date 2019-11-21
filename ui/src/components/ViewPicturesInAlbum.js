import React, { Component } from 'react';
import NavBar from './NavBar'
import axios from 'axios'
class ViewPicturesInAlbum extends Component {
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
            this.setState({images:res.data.images})
            this.setState({data:res.data})
        })
    }

    render() {
        const {images,data} = this.state
        const imageList = images ? images.map(image=>{
            return (
                <li class="user-post"style={{backgroundImage : "url(" + image + ")",backgroundSize : "cover",backgroundPosition : 'center', marginTop:'150px', marginLeft:'50px', marginRight:'350px'}}></li>
            )
        }):null
        const userName = data ?  <p class  style = {{marginTop:'-190px', marginLeft :'90px', fontWeight:'350'}}>  {data.creatorUsername}</p>:null
        return (
            <div >  
            <NavBar/>
            <div>
                 <ul class="user-post-list">
                {imageList}  
            </ul>
            {userName}
            </div>
           
            </div> 
        );
    }
}

export default ViewPicturesInAlbum;