import React, { Component } from 'react';
import NavBar from './NavBar'
import ViewComments from './ViewComments'
import axios from 'axios'
import LikeIcon from './LikeIcon'
import UserFollowersList from './UserFollowersList';


class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    componentWillMount(){
        this.checkLogin()
        this.getFollowersInfo()
        
    }

     
    getFollowersInfo=()=>{
        axios.get('http://13.82.84.219/feed').then(res=>{ 
            console.log(res)
            this.setState({followersData:res.data})
            for(var i = 0;i<res.data.length; i++){
                this.isLiked(res.data[i]._id)
            }
            
           }).catch(error=>{
   
   
           })  
       }

    checkLogin(){
        axios.get('http://13.82.84.219/loginstatus').then(res=>{
                this.setState({username:res.data.username})
            if(res.data.status === "notloggedin"){
                this.props.history.push("/login")
            }
        }).catch(error=>{
            console.log(error)
        })
    }
 
    render() {

    const {followersData,liked} = this.state
        console.log(followersData)
   
    const followers = followersData ? ( followersData.map(follower =>{    

      console.log(follower._id)
        return( 
           <UserFollowersList  getFollowersInfo = {this.getFollowersInfo} isLiked = {this.isLiked}  follower = {follower} keyy = {follower._id}/>
        )
        }) ): null

       
        return (
            <div class = "container">
                <NavBar ID = {this.props.ID} getFollowersInfo = {this.getFollowersInfo}   />
                <div id="body">
                    <ul class="post-list">
                        {followers}
                    </ul>
                </div>
            </div>
            
        );
    }
}

export default Home;