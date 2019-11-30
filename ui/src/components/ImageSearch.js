import React, { Component } from 'react';
import SearchByLocation from './SearchByLocation'
import SearchAlreadyPosted from './SearchAlreadyPosted'
import SearchSimilarImages from './SearchSimilarImages'

class ImageSearch extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    componentWillMount (){
        this.onClickSimilarImages()
    }
    onClickSimilarImages= ()=>{
        this.setState({activeSearchByAlreadyPosted:false})
        this.setState({activeSearchByLocation:false})
        this.setState({activeSimilarImages:'activeSimilarImages'})
      
    }

    onSearchAlreadyPosted = ()=>{
        this.setState({activeSimilarImages:false})
        this.setState({activeSearchByLocation:false})
        this.setState({activeSearchByAlreadyPosted:'activeSearchByAlreadyPosted'})
    }

    onSearchByLocation = ()=>{
        this.setState({activeSearchByAlreadyPosted:false})
        this.setState({activeSimilarImages:false})
        this.setState({activeSearchByLocation:'activeSearchByLocation'}) 
        
    }
    handleModalClose = ()=>{
        this.props.handleModalClose()
    }

    render() {
        const {activeSimilarImages,activeSearchByAlreadyPosted,activeSearchByLocation} = this.state
        return (
            <div class = 'edit-profile-view active'>
            <div class="cancel-icon-white close-view" onClick={this.handleModalClose}></div>
              <div class = "edit-profile-form" style = {{height:'75',width:'75%'}}>
                  <ul class = "album-list">
                      <li onClick = {this.onClickSimilarImages} >Search Similar Images</li>
                      <li onClick = {this.onSearchByLocation}>Search By Location</li>
                      <li onClick = {this.onSearchAlreadyPosted}>Search Already Posted Image</li>
                      
                  </ul>
             { activeSearchByAlreadyPosted==='activeSearchByAlreadyPosted'? <SearchAlreadyPosted handleModalClose = {this.handleModalClose}/>:null}
             {activeSearchByLocation==='activeSearchByLocation'? <SearchByLocation handleModalClose = {this.handleModalClose}/>:null}
             {activeSimilarImages==='activeSimilarImages'? <SearchSimilarImages handleModalClose = {this.handleModalClose}/>:null}
              </div>
          </div>
        );
    }
}

export default ImageSearch;