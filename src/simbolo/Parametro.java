/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simbolo;

import java.util.ArrayList;

/**
 *
 * @author Bruno
 */
public class Parametro {
    
    private ArrayList<String> parametro_formal;//Contiene listas de identifiadores id1, id2, id3 etc.
    private Simbolo tipo_dato;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Parametro (ArrayList<String> parametro_formal, Simbolo tipo_dato){
        this.parametro_formal=parametro_formal;
        this.tipo_dato=tipo_dato;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public ArrayList<String> get_parametro_formal (){
        return this.parametro_formal;
    }
        
    public Simbolo get_tipo_dato (){
        return this.tipo_dato;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_parametro_formal (ArrayList<String> parametros_formales){
        this.parametro_formal=parametros_formales;
    }
    
    public void set_tipo_dato (Simbolo tipo){
        this.tipo_dato=tipo;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        int n=this.parametro_formal.size();
        int i;
        String str="", fin="", param="";
        
        for(i=0; i<n; i++){
            param=this.parametro_formal.get(i);
            fin=(i==(n-1)) ? "" : "," ;
            str=str+param+fin;
        }
        
        return str+" : "+this.tipo_dato.a_cadena();
    }
    
}
