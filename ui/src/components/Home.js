import React, { Component } from 'react';
import NavBar from './NavBar'
import axios from 'axios'
import UserFollowersList from './UserFollowersList';
import sort from '../icons/sort.png'
import SortModal from './SortModal'

axios.defaults.withCredentials = true;
class Home extends Component {
    constructor(props) {
        super(props);
        this.state = { activeSortModal:false};
    }

    componentWillMount(){
        this.checkLogin()
        this.getFollowersInfo()
      //  this.getLocation()
        
    }

    handleModalClose = ()=>{
        this.setState({activeSortModal:false})
    }

     
    getFollowersInfo=()=>{
       if(this.state.sort){
        axios.get(`http://localhost:8081/feed?sort=${this.state.sort}`).then(res=>{ 
            this.setState({followersData:res.data})
            for(var i = 0;i<res.data.length; i++){
                this.isLiked(res.data[i]._id)
            }
            
           }).catch(error=>{

           })  
       }else{
        axios.get(`http://localhost:8081/feed`).then(res=>{ 
            this.setState({followersData:res.data})
            for(var i = 0;i<res.data.length; i++){
                this.isLiked(res.data[i]._id)
            }
            
           }).catch(error=>{

           })  
       }
        
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

    handleSort = ()=>{
        this.setState({activeSortModal : "activeSortModal"})
    }

    onChange = (e) =>{
        this.setState({
            [e.target.name] : e.target.value
        })
       
    }

    onSubmit = (e)=>{
        e.preventDefault()
        this.handleModalClose()
        this.getFollowersInfo()
    }

    
 
    render() {

    const {followersData,username,activeSortModal} = this.state
    
    const followers = followersData ? ( followersData.map(follower =>{   
       
        return( 
           <UserFollowersList username ={this.state.username} getFollowersInfo = {this.getFollowersInfo} isLiked = {this.isLiked}  follower = {follower} keyy = {follower._id}/>
        )
        }) ): null
  
        return (
            <div class = "container">
                <NavBar ID = {this.props.ID} getFollowersInfo = {this.getFollowersInfo} username = {username} />
               
               
                
                <div id="body">
                <img src = {sort} style = {{maxWidth:35,paddingLeft:'1000px',margin:'5em', marginBottom:'-50px'}} onClick = {this.handleSort} alt = "..." />
                    <ul class="post-list">
                        {followers}
                    </ul>
                    
                </div>
                { activeSortModal === "activeSortModal" ?<SortModal onSubmit = {this.onSubmit} onChange = {this.onChange} handleModalClose = {this.handleModalClose}/> :null } 
               
            </div>
            
        );
    }
}

export default Home;