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
public class Token {
    
    private int linea_programa;
    private String lexema;
    private String nombre_token;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Token (String nombre_token, String lexema, int linea_programa){
        this.lexema=lexema;
        this.nombre_token=nombre_token;
        this.linea_programa=linea_programa;
    }
    
    public Token (String lexema){
        this.lexema=lexema;
        this.nombre_token="";
        this.linea_programa=0;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String get_lexema (){
        return this.lexema;
    }
    
    public String get_nombre_token (){
        return this.nombre_token;
    }
    
    public int get_linea_programa (){
        return this.linea_programa;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_lexema (String lexema){
        this.lexema=lexema;
    }
    
    public void set_nombre_token (String nombre){
        this.nombre_token=nombre;
    }
    
    public void set_linea_programa (int linea_programa){
        this.linea_programa=linea_programa;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        return "\n<"+this.nombre_token+", "+this.lexema+">\n";
    }
    
}
