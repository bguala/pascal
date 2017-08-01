/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tipos;

import simbolo.Simbolo;

/**
 *
 * @author Bruno
 */
public abstract class Tipo  {
    
    private String nombre_tipo;
    
    //-----------------------------------------------------------------------------------
    //--- Constructores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Tipo (){
        this.nombre_tipo="";
    }
    
    public Tipo (String nombre_tipo){
        this.nombre_tipo=nombre_tipo;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String get_nombre_tipo (){
        return this.nombre_tipo;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_nombre_tipo (String nombre){
        this.nombre_tipo=nombre;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        return this.nombre_tipo;
    }
    
}
