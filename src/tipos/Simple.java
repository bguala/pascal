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
    
    private String tipo_primitivo; //integer o boolean.
        
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Simple (String nombre, String tipo_primitivo){
        super(nombre);
        this.tipo_primitivo=tipo_primitivo;
        
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String get_tipo_primitivo (){
        return this.tipo_primitivo;
    }
        
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_tipo_primitivo (String tipo_primitivo){
        this.tipo_primitivo=tipo_primitivo;
    }
        
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
}
