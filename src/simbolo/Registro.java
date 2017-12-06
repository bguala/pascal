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
    //--- Constructores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Registro (){
        super();
        this.lista_campos=new ArrayList();
    }
    
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
    
    public String chequeo_de_tipos (Token nombre_registro, Token identificador){
        TipoDato tipo=this.obtener_campo(identificador);
        
        if(tipo == null){
            System.out.println("\nError Semantico : *** El identificador \""+identificador.get_lexema()+"\" no esta definido como un campo del registro \""+nombre_registro.get_lexema()+"\" *** Linea "+identificador.get_linea_programa());
            System.exit(1);
        }
        
        return tipo.get_nombre_tipo();
    }
    
    private TipoDato obtener_campo (Token identificador){
        int n=this.lista_campos.size();
        int i=0;
        boolean fin=true;
        TipoDato tipo=null;
        ArrayList<String> campos=null;
        int j, m;
        
        while(i<n && fin){
            //  Obtenemos una hipotetica lista de ids en un campo del registro.
            campos=this.lista_campos.get(i).get_parametro_formal();
            m=campos.size();
            j=0;
            while(j<m && fin){
                
                if(campos.get(j).equalsIgnoreCase(identificador.get_lexema())){
                    fin=false;
                    tipo=(TipoDato)this.lista_campos.get(i).get_tipo_dato();
                }
                
                j++;
            }
            
            i++;
        }
        
        //El campo de un registro solamente puede ser integer o boolean. Estos tipos si o si se guardan en un objeto TipoDato.
        return tipo;
    }
    
    /*
    * Esta funcion calcula el desplazamiento de un campo para generar codigo MEPA.
    */
    public int desplazamiento_campo (Token campo){
        //Variables de control para el primer while.
        int n=this.lista_campos.size();
        int i=0;
        //Desplazamiento del campo.
        int desplazamiento=0;
        boolean fin=true;
        ArrayList<String> campos=null;
        //Variables de control para el segundo while anidado.
        int j, m;
        
        while(fin && i<n){
            //Obtenemos una hipotetica lista de ids en un campo del registro.
            campos=this.lista_campos.get(i).get_parametro_formal();
            m=campos.size();
            j=0;
            while(fin && j<m){
                
                if(campos.get(j).equalsIgnoreCase(campo.get_lexema()))
                    fin=false;
                else
                    desplazamiento++;
                
                j++;
            }
            
            i++;
        }
        
        return desplazamiento;
    }
    
    /*
    * Esta funcion devuelve la cantidad de campos de un registro
    */
    public int longitud (){
        //Variables de control para el primer while.
        int n=this.lista_campos.size();
        int i=0;
        //Cantidad de campos del registro.
        int longitud=0;
        ArrayList<String> campos=null;
                
        while(i<n){
            //Obtenemos una hipotetica lista de ids en un campo del registro.
            campos=this.lista_campos.get(i).get_parametro_formal();
            
            longitud += campos.size();
            
            i++;
        }
        
        return longitud;
    }
    
}
