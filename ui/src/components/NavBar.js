import React, { Component } from 'react';
import {Link} from 'react-router-dom'
import image from '../img/album.png'
import imageSearch from '../icons/image.png'
import AlbumMenu from './AlbumMenu'
import axios from "axios"
import SearchResult from './SearchResult'
import SearchDescription from './SearchDescription'
import Post from './Post'
import SearchTag from './SearchTag'
import  ImageSearch from './ImageSearch'


class NavBar extends Component {
    constructor(props) {
        super(props);
        this.state = { activePostModal:false, activeNotificationModal:false,activeAlbumModal:false, searchResult:[],activeImageSearchModal:false };
        this.notificationList = React.createRef();
        this.search = React.createRef();
        this.onInputSearch = this.onInputSearch.bind(this)
    }

    componentWillMount(){
    //   console.log(this.search)
     
    }

    onClickPost = ()=>{
        this.setState({activePostModal:"activePostModal"})
    }

    onInputSearch(e){
        this.notificationList.current.classList.add("notif-list-active");

        if(e.target.value === ""){
            this.setState({searchResult:[]})
        }

        let value = e.target.value;

        axios.get(`http://localhost:8081/search?query=${value}`).then(res=>{
            console.log(res)
            this.setState({searchResult:res.data.username})
            const description = Object.values(res.data.description)
            this.setState({description:description});
            const tag = Object.values(res.data.tag)
            this.setState({tag:tag});
        })

    }

    handleModalClose = ()=>{
        this.setState({activePostModal:false,activeAlbumModal:false,activeImageSearchModal:false})
    }

    onClickAlbumMenu = ()=>{
        this.setState({activeAlbumModal:'activeAlbumModal'})
    }

    onClickUserProfile= ()=>{
        this.notificationList.current.classList.remove("notif-list-active");
    }

    onImageSearch = ()=>{
        this.setState({activeImageSearchModal:"activeImageSearchModal"})
    }


    


    render() {
        const {activePostModal,activeAlbumModal,searchResult,description,tag,activeImageSearchModal} = this.state

        const descriptionSearch =  description ?( description.map(description=>{    
          
             if(description.description)
            return( 
                <li class="notif clearfix"> 
                <div class="notif-info">
                     <SearchDescription id = {description._id} data = {description.username} description= {description.description} onClickUserProfile = {this.onClickUserProfile}/>    
                 </div>
                 </li>
            )
            }) ):null

            const tagSearch =  tag ?( tag.map(tag=>{    
                console.log(tag.tags)
                
                  
                    return( 
                        <li class="notif clearfix"> 
                        <div class="notif-info">
                             <SearchTag id = {tag.id} data = {tag.username} tag = {tag.tag} onClickUserProfile = {this.onClickUserProfile}/>    
                         </div>
                         </li>
                    )
                 
                
                }) ):null
    
       

        const accountNames  = searchResult ?  searchResult.map(search=>{
          
            if(search.name)
            return(
                <li class="notif clearfix">
                <div class="avatar-def user-image" style={{backgroundImage : "url('" + search.profilepicture + "')",backgroundSize : "cover",backgroundPosition : 'center'}} ></div>
                <div class="notif-info">
                <SearchResult  data = {search.name} onClickUserProfile = {this.onClickUserProfile} />
                </div>
            
                
                </li>
                
               
            )
        }) : null

        return (
            <div id="top">
            <div class="topbar clearfix">
                <Link to  = {{pathname: '/home'}}>
                    <ul class="brands clearfix">
                        <li class="insta-logo"></li>
                        <li class="insta-logo-type"></li>
                    </ul>
                </Link>
                <input type="text" class="text-field search"  ref = {this.search} onInput = {this.onInputSearch} placeholder="Search"/>
               
                <ul class="links">
                   <img src = {imageSearch} style = {{maxWidth:16,marginLeft:'-330px'}} onClick = {this.onImageSearch} alt = "..." />
                    <li class="link explore-icon explore" onClick = {this.onClickPost}></li>
                    <li class="link notifications" >
                        <ul class="notif-list" ref = {this.notificationList}>
                         
                          <li class="notif clearfix" >
                                   {accountNames}  
                                   {descriptionSearch}     
                                   {tagSearch}                 
                          </li>
                        </ul>
                    </li>
   
                    <Link to={{pathname: '/profile',state: {username:this.props.username}}} username = {this.props.username} activeClassName="current"><li class="link user-icon profile"></li></Link>
                    <li class = "link" onClick = {this.onClickAlbumMenu}><img src = {image} style = {{maxWidth:26}} onClick = {this.onClickAlbumMenu} alt = "..." /></li>
                </ul>

            </div>
            {activePostModal === "activePostModal"? <Post ID={this.props.ID} avatarMedium = {this.avatarMedium} username = {this.props.username} getFollowersInfo = {this.props.getFollowersInfo} getAccountPicture = {this.props.getAccountPicture} handleModalClose={this.handleModalClose.bind(this)} /> : null}

            {activeAlbumModal==='activeAlbumModal'?<AlbumMenu handleModalClose = {this.handleModalClose}/>:null}
            {activeImageSearchModal==='activeImageSearchModal'?<ImageSearch handleModalClose = {this.handleModalClose}/>:null}
           
        </div>
        );
    }
}

export default NavBar;