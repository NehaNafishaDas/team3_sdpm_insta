import React, { Component } from 'react';
import NavBar from './NavBar'
import {Link} from 'react-router-dom'

class SimilarImages extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    render() {
        const {data} = this.props.location.state
        console.log(data)
        const imageList = data ? data.map(data=>{
            console.log(data)
            return (
             <Link to = {{pathname:`/commentinfo/${data._id}/${data.username}`}} > <li class="user-post"style={{backgroundImage : "url(" + data.imageId + ")",backgroundSize : "cover",backgroundPosition : 'center', marginTop:'150px', marginLeft:'50px', marginRight:'350px'}}></li></Link>  
            )
        }):null
        return (
            <div >  
            <NavBar/>
            <div>
                 <ul class="user-post-list">
                {imageList}  
            </ul>
           
            </div>
           
            </div> 
        );
    }
}

export default SimilarImages;