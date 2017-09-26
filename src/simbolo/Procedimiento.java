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
public class Procedimiento extends Simbolo {
    
    private ArrayList<Parametro> lista_parametros;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Procedimiento (String lexema, int espacio_asignado){
        super(lexema,espacio_asignado);
        this.lista_parametros=new ArrayList ();
    }
    
    public Procedimiento (String lexema, int espacio_asignado, ArrayList<Parametro> params){
        super(lexema,espacio_asignado);
        this.lista_parametros=params;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public ArrayList<Parametro> get_lista_parametros (){
        return this.lista_parametros;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_lista_parametros (ArrayList<Parametro> lista_parametros){
        this.lista_parametros=lista_parametros;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void agregar_parametro (Parametro p){
        this.lista_parametros.add(p);
    }
    
    public String a_cadena (){
        String lista="";
        
        if(this.lista_parametros.isEmpty()){
            lista=" [ lista de parametros vacia ] ";
        }else{
            String str="", fin="";
            Parametro parametro=null;
            int i;
            int n=this.lista_parametros.size();

            for(i=0; i<n; i++){
                parametro=(Parametro)this.lista_parametros.get(i);
                fin=(i==(n-1)) ? ""  : " , " ;
                str=str + parametro.a_cadena() + fin;
            }
            
            lista=" [ " + str + " ] ";
        }
        
        return super.a_cadena() + lista;
    }
    
    public String chequeo_de_tipos (Token id, ArrayList<Parametro> argumentos){
        int n=this.lista_parametros.size();
        int m=argumentos.size();
        String error="";
        
        //  Verificamos si en la definicion de un procedimiento hay cero parametros y en su llamada n.
        if((n == 0) && (m > 0)){
            System.out.println("\nError Semantico : *** El procedimiento \""+id.get_lexema()+"\" no posee parametros formales en su definicion. Sin embargo en su llamada se especificaron "+m+" *** Linea "+id.get_linea_programa());
            System.exit(1);
        }
        
        if((m == 0) && (n > 0)){
            System.out.println("\nError Semantico : *** El procedimiento \""+id.get_lexema()+"\" posee "+n+" parametros formales en su definicion, "+this.a_cadena()+". Sin embargo en su llamada se especificaron 0 *** Linea "+id.get_linea_programa());
            System.exit(1);
        }
        
        if(n != m){
            System.out.println("\nError Semantico : *** La cantidad de argumentos especificados en \""+id.get_lexema()+"\" no coinciden con la cantidad de parametros formales presentes en su definicion : "+this.a_cadena()+" *** Linea "+id.get_lexema());
            System.exit(1);
        }
        
        if(!coinciden_tipos(argumentos, error)){
            System.out.println(error+id.get_linea_programa());
            System.exit(1);
        }
        
        return "void";
    }
    
    private boolean coinciden_tipos (ArrayList<Parametro> argumentos, String error){
        int n=argumentos.size();
        TipoDato tipo_param=null;
        TipoDato tipo_arg=null;
        int i=0;
        boolean fin=true;
        
        while(i<n && fin){
            tipo_arg=(TipoDato)(argumentos.get(i)).get_tipo_dato();
            tipo_param=(TipoDato)(this.lista_parametros.get(i)).get_tipo_dato();
            
            if(!(tipo_param.get_nombre_tipo().equalsIgnoreCase(tipo_arg.get_nombre_tipo()))){
                fin=false;
                error="\nError Semantico: *** Tipos incompatibles, "+this.lista_parametros.get(i).a_cadena()+", mientras que "+argumentos.get(i).a_cadena()+" *** Linea ";
            }
            
            i++;
        }
        
        return fin;
    }
    
}
