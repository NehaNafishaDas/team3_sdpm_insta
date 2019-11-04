import React, {Component} from 'react'

import '../styles/index.css'

class InputField extends Component {

 
 render(){

      var error = false;
      var errors = {
          fieldName : {
              
          }
      }

    // console.log(this.props.errors)
    // for(var i = 0; i<this.props.errors.length ; i++){
    //         console.log(this.props.errors[i])
    //     if(this.props.name === this.props.errors[i].fieldName){
    //       error = this.props.errors[i];
    //     break;
    //     }else{
    //     continue;
    //    }
    // }
            return(
                    <div className="col s6" >

                    {this.props.children}

                    {
                        error ? <p className="custom-input-error">{error}</p> : null
                    }
                    </div>
                

            )
  }

}
  
 export default InputField