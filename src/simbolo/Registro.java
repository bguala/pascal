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
public class Registro extends Simbolo {
    
    private ArrayList<Parametro> lista_campos;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Registro (String lexema, int espacio_asignado){
        super(lexema, espacio_asignado);
        this.lista_campos=new ArrayList();
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public ArrayList get_lista_campos (){
        return this.lista_campos;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_lista_campos (ArrayList<Parametro> campos){
        this.lista_campos=campos;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void agregar_parametro (Parametro p){
        this.lista_campos.add(p);
    }
    
    public String a_cadena (){
        String str="";
        
        if(this.lista_campos.isEmpty()){
            str="registro vacio ) ";
        }else{
            String coma="";
            int i;
            int n=this.lista_campos.size();
            Parametro param;
            
            for(i=0; i<n; i++){
                param=this.lista_campos.get(i);
                coma=(i==(n-1)) ? " ) " : " , " ;
                str=str+param.a_cadena()+coma;
            }
        }
        
        return " ( "+str;
    }
    
}
