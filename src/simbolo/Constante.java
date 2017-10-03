/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simbolo;

/**
 *
 * @author Bruno
 */
public class Constante extends Simbolo {
    
    private String valor;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Constante (String lexema, int espacio_asignado, String valor){
        super(lexema,espacio_asignado);
        this.valor=valor;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String get_valor (){
        return this.valor;
    }
    
    //--- Inferimos el tipo de la constante, puede ser integer o boolean ---
    public String get_tipo(){
        String tipo="";
        char c=this.valor.charAt(0);
        if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)))
            tipo="boolean";
        else
            tipo="integer";
        
        return tipo;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_valor (String val){
        this.valor=val;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        return super.a_cadena()+" , "+valor;
    }
    
}
