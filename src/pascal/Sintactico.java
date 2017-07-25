/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pascal;

import java.util.ArrayList;
import simbolo.Token;
import tabla_simbolos.TablaSimbolos;
import simbolo.*;
/**
 *
 * @author Bruno
 */
public class Sintactico {
    
    private int preanalisis;
    private int id_entorno;
    private ArrayList<Token> tokens_sintacticos;
    private ArrayList<String> palabras_reservadas;
    private TablaSimbolos tabla_simbolos; //Contiene a la TS relacionada al programa principal.
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Sintactico (ArrayList<Token> tokens_sintacticos, ArrayList<String> palabras){
        this.preanalisis=0;
        this.id_entorno=0;
        this.tokens_sintacticos=tokens_sintacticos;
        this.palabras_reservadas=palabras;
        
        //Agregamos nuevas palabras reservadas.
        this.palabras_reservadas.add("and");
        this.palabras_reservadas.add("or");
        this.palabras_reservadas.add("not");
        
        this.tabla_simbolos=new TablaSimbolos(0);
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public ArrayList<Token> get_tokens_sintacticos (){
        return this.tokens_sintacticos;
    }
    
    public int get_id_entorno (){
        return this.id_entorno;
    }
    
    public TablaSimbolos get_tabla_simbolos (){
        return this.tabla_simbolos;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_tokens_sintacticos (ArrayList<Token> ts){
        this.tokens_sintacticos=ts;
    }
    
    public void set_id_entorno (int id){
        this.id_entorno=id;
    }
    
    public void set_tabla_simbolos (TablaSimbolos ts){
        this.tabla_simbolos=ts;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void analisis_sintactico (){
        mi_pascal();
    }
    
    private void match (String terminal){
        Token tk_actual=this.tokens_sintacticos.get(this.preanalisis);
        
        if(terminal.equalsIgnoreCase(tk_actual.get_lexema())){
            this.preanalisis++;
        }else{
            System.out.println("\nError Sintactico : *** Se esperaba el simbolo "+terminal+" *** Linea "+tk_actual.get_linea_programa());
            System.exit(1);
        }
    }
    
    //-----------------------------------------------------------------------------------
    //--- MiPascal ----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void mi_pascal (){
        encabezado();definicion(this.tabla_simbolos);
        //declaracion_subprogramas();bloque(this.tabla_simbolos);
    }
    
    //-----------------------------------------------------------------------------------
    //--- Encabezado --------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void encabezado (){
        match("program");identificador();match(";");
    }
    
    private boolean identificador (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        String id=token.get_lexema();
        
        return (!this.palabras_reservadas.contains(id)) ? true : false ;
        
    }
    
    //-----------------------------------------------------------------------------------
    //--- Definicion --------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void definicion (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            
            case "const" : this.preanalisis++;//C();
                           token=this.tokens_sintacticos.get(this.preanalisis);
                           switch(token.get_lexema()){
                               
                               case "type" : this.preanalisis++;//type_def();
                                             token=this.tokens_sintacticos.get(this.preanalisis);
                                             if(token.get_lexema().equalsIgnoreCase("var")){
                                                 this.preanalisis++;//var_def();
                                             }
                                             break;
                               case "var" : this.preanalisis++;//var_def();
                                            token=this.tokens_sintacticos.get(this.preanalisis);
                                            if(token.get_lexema().equalsIgnoreCase("type")){
                                                this.preanalisis++;//type_def();
                                            }
                                            break;
                           }
                           break;
            case "type" : this.preanalisis++;//type_def();
                          token=this.tokens_sintacticos.get(this.preanalisis);
                          switch(token.get_lexema()){
                              case "const" : this.preanalisis++;//const_def();
                                             token=this.tokens_sintacticos.get(this.preanalisis);
                                             if(token.get_lexema().equalsIgnoreCase("var")){
                                                 this.preanalisis++;//var_def();
                                             }
                                             break;
                              case "var" : this.preanalisis++;//var_def();
                                           token=this.tokens_sintacticos.get(this.preanalisis);
                                           if(token.get_lexema().equalsIgnoreCase("const")){
                                               this.preanalisis++;//const_def();
                                           }
                                           break;
                          }
                          break;
            case "var" : this.preanalisis++;//var_def();
                         token=this.tokens_sintacticos.get(this.preanalisis);
                         switch(token.get_lexema()){
                             case "const" : this.preanalisis++;//const_def();
                                            token=this.tokens_sintacticos.get(this.preanalisis);
                                            if(token.get_lexema().equalsIgnoreCase("type")){
                                                this.preanalisis++;//type_def();
                                            }
                                            break;
                             case "type" : this.preanalisis++;//type_def();
                                           token=this.tokens_sintacticos.get(this.preanalisis);
                                           if(token.get_lexema().equalsIgnoreCase("const")){
                                               this.preanalisis++;//type_def();
                                           }
                                           break;
                         }
                         break;
            
        }
    }
    
    private boolean identificador (Token token){
        boolean r=false;
        String id=token.get_lexema();
        char c=id.charAt(0);
        
        if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(id))
            r=true;
        else{
            System.out.println("Error Sintactico : *** Simbolo "+id+" inesperado *** Linea "+token.get_linea_programa());
            System.exit(1);
        }
        
        return r;
    }
    
    private ArrayList secuencia_ids (){
        boolean fin=false;
        ArrayList ids=new ArrayList();
        
        while(!fin && identificador(this.tokens_sintacticos.get(this.preanalisis))){
            ids.add(this.tokens_sintacticos.get(this.preanalisis).get_lexema());
            this.preanalisis++;
            
            if(this.tokens_sintacticos.get(this.preanalisis).get_lexema().equalsIgnoreCase(","))
                this.preanalisis++;
            else
                fin=true;
        }
        
        return ids;
    }
    
    private void const_def (TablaSimbolos ts){
        constante(ts);match(";");c1(ts);
    }
    
    private void c1 (TablaSimbolos ts){
        if(this.palabras_reservadas.contains(this.tokens_sintacticos.get(this.preanalisis)))
            ; //Presencia de cadena nula.
        else
            const_def(ts);
    }
    
    private void constante (TablaSimbolos ts){
        ArrayList ids=secuencia_ids();
        match("=");
        String dato=this.tokens_sintacticos.get(this.preanalisis).get_lexema();
        
        //Verifiamos que dato sea un numero entero, true o false mediante un esquema de traduccion.
        
        int i;
        int n=ids.size();
        String id="";
        for(i=0; i<n; i++){
            id=(String)ids.get(i);
            
            ts.insertar(id, new Constante(id,1,dato));
        }
    }
    
    //-----------------------------------------------------------------------------------
    //--- Declaracion de Subprogramas ---------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    //-----------------------------------------------------------------------------------
    //--- Bloque ------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    //-----------------------------------------------------------------------------------
    //--- Expresiones -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void expresion (){
        T();E1();
    }
    
    private void T (){
        F();T1();
    }
    
    private void F (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase("not")){
            this.preanalisis++;G();
        }else
            G();
    }
    
    private void G (){
        H();G1();
    }
    
    private void H (){
        I();H1();
    }
    
    private void I (){
        J();I1();
    }
    
    private void J (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase("-")){
            this.preanalisis++;K();
        }else
            K();
    }
    
    private void I1 (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        switch(token.get_lexema()){
            case "+" :
            case "-" : this.preanalisis++;
                       J();
                       I1();
                       break;
            default : ; //Presencia de cadenanula.
        }
    }
    
    private void H1 (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        switch(token.get_lexema()){
            case "*" :
            case "/" : this.preanalisis++;
                       I();
                       H1();
                       break;
            default : ; //Presencia de cadena nula.
        }
    }
    
    private void G1 (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        switch(token.get_lexema()){
            case ">"  :
            case "<"  :
            case ">=" :
            case "<=" :
            case "="  :
            case "<>" : this.preanalisis++;
                        H();
                        G1();
            default : ; //Presencia de cadena nula.
        }
    }
    
    private void T1 (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase("and")){
            this.preanalisis++;
            F();
            T1();
        }else
            ; //Presencia de cadena nula.
    }
    
    private void E1 (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase("or")){
            this.preanalisis++;
            T();
            E1();
        }else
            ; //Presencia de cadena nula.
    }
    
    private void K (){
        
    }
    
}
