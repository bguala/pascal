/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tipos;

/**
 *
 * @author Bruno
 */
public class Simple extends Tipo {
    
    private String tipo_base; //integer, boolean, registro, arreglo etc.
    private String caracteritica; //Primitivo o definido por el usuario.
        
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Simple (String nombre_tipo){
       super(nombre_tipo);
       this.tipo_base="";
       this.caracteritica="";
    }
    
    public Simple (String nombre_tipo, String tipo_base, String caracteristica){
        super(nombre_tipo);
        this.tipo_base=tipo_base;
        this.caracteritica=caracteristica;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String get_tipo_base (){
        return this.tipo_base;
    }
    
    public String get_caracteristica (){
        return this.caracteritica;
    }
        
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_tipo_base (String tipo_base){
        this.tipo_base=tipo_base;
    }
    
    public void set_caracteristica (String caracteristica){
        this.caracteritica=caracteristica;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
}
