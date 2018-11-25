/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.mathpar.students.ukma17i41.bosa.parallel.engine;
package com.mathpar.parallel.dap;

import java.util.ArrayList;

/**
 *
 * @author alla
 */
public class Pine {
    
    ArrayList<Amin> body;
   
    int state;
    
    public Pine(){
        body = new ArrayList();
    }
   
    public int CheckState(){
        for(int i=0; i<body.size(); i++){
            if(body.get(i).SetState()!=1){
              return 0;
            }
        }
        return 1;
    }  
    
    public int GetState(){
        return state;
    }
    
    public void SetState(int st){
        state=st;
    }
}
