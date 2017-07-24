/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tipos;

import simbolo.*;
/**
 *
 * @author Bruno
 */
public class Estructurado extends Tipo{
    
    private Simbolo tipo_estructurado;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Estructurado (String nombre_tipo, Simbolo s){
        super(nombre_tipo);
        this.tipo_estructurado=s;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Simbolo get_tipo_estructurado (){
        return this.tipo_estructurado;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_tipo_estructurado (Simbolo s){
        this.tipo_estructurado=s;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        return super.a_cadena()+" "+this.tipo_estructurado.a_cadena();
    }
    
}
