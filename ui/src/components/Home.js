import React, { Component } from 'react';
import NavBar from './NavBar'
import axios from 'axios'
import UserFollowersList from './UserFollowersList';

axios.defaults.withCredentials = true;
class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    componentWillMount(){
        this.checkLogin()
        this.getFollowersInfo()
      //  this.getLocation()
        
    }

     
    getFollowersInfo=()=>{
        axios.get('http://localhost:8081/feed').then(res=>{ 
            this.setState({followersData:res.data})
            for(var i = 0;i<res.data.length; i++){
                this.isLiked(res.data[i]._id)
            }
            
           }).catch(error=>{

           })  
    }
    

    checkLogin(){
        axios.get('http://localhost:8081/loginstatus').then(res=>{
                this.setState({username:res.data.username})
            if(res.data.status === "notloggedin"){
                this.props.history.push("/login")
            }
        }).catch(error=>{
            console.log(error)
        })
    }
 
    render() {

    const {followersData} = this.state
    const followers = followersData ? ( followersData.map(follower =>{    
        return( 
           <UserFollowersList username ={this.state.username} getFollowersInfo = {this.getFollowersInfo} isLiked = {this.isLiked}  follower = {follower} keyy = {follower._id}/>
        )
        }) ): null
  
        return (
            <div class = "container">
                <NavBar ID = {this.props.ID} getFollowersInfo = {this.getFollowersInfo} />
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