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
    private String etiqueta;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Procedimiento (String lexema, int espacio_asignado){
        super(lexema,espacio_asignado);
        this.lista_parametros=new ArrayList ();
        this.etiqueta="";
    }
    
    public Procedimiento (String lexema, int espacio_asignado, ArrayList<Parametro> params, String etiqueta){
        super(lexema,espacio_asignado);
        this.lista_parametros=params;
        this.etiqueta=etiqueta;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public ArrayList<Parametro> get_lista_parametros (){
        return this.lista_parametros;
    }
    
    /*
    * Devuelve la etiqueta simbolica de una funcion para generar codigo MEPA.
    */
    public String get_etiqueta (){
        return this.etiqueta;
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
        this.lista_parametros=this.unificar_parametros_formales();
        
        //Por lo visto no es necesario usar una funcion para calcular la cantidad de parametros formales.
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
    
    /*
    * Guarda en un unico ArrayList todos los parametros formales de un subprograma. Esto se debe hacer asi porque las
    * definiciones a,b,c:integer pueden generar problemas.
    */
    private ArrayList<Parametro> unificar_parametros_formales (){
        ArrayList<Parametro> parametros=new ArrayList();
        //Contiene los parametros que debemos unificar en un solo ArrayList.
        ArrayList<String> params;
        int n=this.lista_parametros.size();
        int m=0;
        int i,j;
        //Para los parametros formales.
        for(i=0; i<n; i++){
            
            params=this.lista_parametros.get(i).get_parametro_formal();
            m=this.lista_parametros.get(i).get_parametro_formal().size();
            //Para los paramteros formales internos.
            for(j=0; j<m; j++){
                parametros.add(new Parametro(params.get(j),this.lista_parametros.get(i).get_tipo_dato()));
            }
            
        }
        
        return parametros;
    }
    
    /*
    * Esta funcion se utiliza para armar el codigo MEPA correspondiente a un parametro formal que se utiliza en el bloque
    * begin-end de un subprograma. Solamente devuelve el desplazamiento negativo, no podemos armar aqui mismo la 
    * instruccion MEPA porque no tenemos el nivel lexico.
    */
    public int calcular_desplazamiento (Token token){
        int i=0;
        int j=0;
        boolean fin=false;
        int n=this.lista_parametros.size();
        String param="";
                
        while(!fin && j<n){
            i++;
            param=this.lista_parametros.get(j).get_parametro();
            
            if(token.get_lexema().equalsIgnoreCase(param))
                fin=true;
            
            j++;
        }
        
        return -1*(n +3 -i);
    }
    
}
