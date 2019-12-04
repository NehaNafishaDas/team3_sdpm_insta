import React from 'react';
import { BrowserRouter, Route } from 'react-router-dom'
import Layout from './components/Layout'
import Login from './components/Auth/Login'
import Home from './components/Home'
import Profile from './components/Profile'
import OtherUserProfiles from './components/OtherUserProfiles'
import ViewAlbum from './components/ViewAlbum'
import ViewPicturesInAlbum from './components/ViewPicturesInAlbum'
import CommentInfo from './components/CommentInfo'
import SimilarImages from './components/SimilarImages';
import SellImage from './components/SellImage';

function App() {
  return (
    <div className="container">
      <BrowserRouter>
       <Route exact path = "/" component = {Layout}/>
       <Route path  = "/login" component = {Login}/>
       <Route path  = "/home" component = {Home}/>
       <Route path  = "/profile" component = {Profile}/>
       <Route path  = "/otherusers/:username" component = {OtherUserProfiles}/>
       <Route path = "/album" component  = {ViewAlbum} />
       <Route path = "/picture" component  = {ViewPicturesInAlbum} />
       <Route path = "/commentinfo/:id/:username" component = {CommentInfo} />
       <Route path = "/similarimages" component  = {SimilarImages} />
       <Route path = "/sellimage" component  = {SellImage} />

       </BrowserRouter>
    </div>
  );
}

export default App;
