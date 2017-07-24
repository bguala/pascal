/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simbolo;

import java.util.Vector;
import tipos.*;

/**
 *
 * @author Bruno
 */
public class Parametro {
    
    private Vector<String> parametro_formal;//Contiene listas de identifiadores id1, id2, id3 etc.
    private Tipo tipo_dato;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Parametro (Vector<String> parametro_formal, Tipo tipo_dato){
        this.parametro_formal=parametro_formal;
        this.tipo_dato=tipo_dato;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Vector<String> get_parametro_formal (){
        return this.parametro_formal;
    }
        
    public Tipo get_tipo_dato (){
        return this.tipo_dato;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_parametro_formal (Vector<String> parametros_formales){
        this.parametro_formal=parametros_formales;
    }
    
    public void set_tipo_dato (Tipo tipo){
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
            param=this.parametro_formal.elementAt(i);
            fin=(i==(n-1)) ? "" : "," ;
            str=str+param+fin;
        }
        
        return str+" : "+this.tipo_dato.a_cadena();
    }
    
}
