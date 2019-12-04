import React, { Component } from 'react';
import axios from 'axios';
import {Redirect} from 'react-router-dom'

class SearchByLocation extends Component {
    constructor(props) {
        super(props);
        this.state = { activeViewSimilarImages:false };
    }
    onChange = (e)=>{
      this.setState( {[e.target.name] : e.target.value})
    }

    onSubmit = (e)=>{
        e.preventDefault()
        axios.post(`http://localhost:8081/searchlocation?longitude=${this.state.longitude}&latitude=${this.state.latitude}`).then(res=>{
            console.log(res)
            this.setState({data:res.data,activeViewSimilarImages:"activeViewSimilarImages"})
        }).catch(error=>{
            console.log(error)
        })
    }
    render() {
        const {activeViewSimilarImages,data} = this.state
        const similarImages =  activeViewSimilarImages==="activeViewSimilarImages"?<Redirect to = {{pathname:"/similarimages", state:{data:data}}} /> : null
        return (
            <form class="" style = {{marginLeft:'400px',marginTop:'20px'}} onSubmit = {this.onSubmit}>
                <p style = {{padding:"30px 40px 20px "}}><h4>Search By Location</h4></p>
                <label for="name">Longitude</label>
                <input type="text" name="longitude" class="name text-field" placeholder="longitude" onChange = {this.onChange} />
                <label for="name">Latitude</label>
                <input type="text" name="latitude" class="name text-field" placeholder="latitude" onChange = {this.onChange} />
        
            <input type="submit" name="submit" class="def-button submit" value="Submit"/>
            {similarImages}
           </form>
        );
    }
}

export default SearchByLocation;