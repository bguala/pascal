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
public class Funcion extends Simbolo {
    
    private ArrayList<Parametro> lista_parametros;
    private String pasaje_parametro;
    private Simbolo tipo_retorno; //Puede ser integer o boolean.
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Funcion (String lexema, int espacio_asignado){
        super(lexema,espacio_asignado);
        this.pasaje_parametro="valor";
        this.tipo_retorno=null;
        this.lista_parametros=new ArrayList();
    }
    
    public Funcion (String lexema, int espacio_asignado, Simbolo tipo_retorno, ArrayList<Parametro> params){
        super(lexema,espacio_asignado);
        this.pasaje_parametro="valor";
        this.tipo_retorno=tipo_retorno;
        this.lista_parametros=params;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public ArrayList<Parametro> get_lista_parametros (){
        return this.lista_parametros;
    }
    
    public String get_pasaje_parametro (){
        return this.pasaje_parametro;
    }
    
    public Simbolo get_tipo_retorno (){
        return this.tipo_retorno;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_lista_parametros (ArrayList<Parametro> lista_parametros){
        this.lista_parametros=lista_parametros;
    }
    
    public void set_pasaje_parametro (String pasaje_parametro){
        this.pasaje_parametro=pasaje_parametro;
    }
       
    public void set_tipo_retorno(Simbolo retorno){
        this.tipo_retorno=retorno;
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
        
        return super.a_cadena() + lista + " : " + this.tipo_retorno.a_cadena();
    }
    
    /*
    * id: contiene el lexema asociado a una funcion.
    * argumentos: contiene todos los argumentos presentes en una llamada a funcion.
    */
    public String chequeo_de_tipos (Token id, ArrayList<Parametro> argumentos){
        int n=cantidad_args(this.lista_parametros);
        int m=cantidad_args(argumentos);
        Strings error=new Strings(" ");
        
        if(n != m){
            System.out.println("\nError Semantico : *** La cantidad de argumentos especificados en \""+id.get_lexema()+"\" no coinciden con la cantidad de parametros formales presentes en su definicion : "+n+" *** Linea "+id.get_linea_programa());
            System.exit(1);
        }
        
        if(!coinciden_tipos(argumentos, error)){
            System.out.println(error.get_string()+id.get_linea_programa());
            System.exit(1);
        }
        
        return ((TipoDato)this.tipo_retorno).get_nombre_tipo();
    }
    
    private boolean coinciden_tipos (ArrayList<Parametro> argumentos, Strings error){
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
                TipoDato td=(TipoDato)this.lista_parametros.get(i).get_tipo_dato();
                
                String param=this.lista_parametros.get(i).get_parametro_formal().get(0);
                error.set_string("\nError Semantico: *** Tipos incompatibles, la funcion "+this.lexema+" posee un parametro formal "+param+" de tipo "+td.get_nombre_tipo()+", sin embargo en su llamada existe un argumento de tipo "+tipo_arg.get_nombre_tipo()+" en la misma posicion *** Linea ");
            }
            
            i++;
        }
        
        return fin;
    }
    
    private int cantidad_args (ArrayList<Parametro> argumentos){
        int n=0;
        int i;
        
        for(i=0; i<argumentos.size(); i++){
            n += argumentos.get(i).get_parametro_formal().size();
        }
        
        return n;
    }
}
