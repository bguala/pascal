/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pascal;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import simbolo.Token;
import tabla_simbolos.TablaSimbolos;
import simbolo.*;

/**
 *
 * @author Bruno
 */
public class Semantico {
    
    private int preanalisis;
    private int id_entorno;
    private ArrayList<Token> tokens_sintacticos;
    private ArrayList<String> palabras_reservadas;
    private TablaSimbolos tabla_simbolos; //Contiene a la TS relacionada al programa principal.
    private String archivo;
    private Token inicio_exp;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Semantico (ArrayList<Token> tokens_sintacticos, ArrayList<String> palabras, String archivo){
        this.preanalisis=0;
        this.id_entorno=1;
        this.tokens_sintacticos=tokens_sintacticos;
        this.palabras_reservadas=palabras;
        this.archivo=archivo;
        this.inicio_exp=null;
        
        //Agregamos nuevas palabras reservadas.
        this.palabras_reservadas.add("and");
        this.palabras_reservadas.add("or");
        this.palabras_reservadas.add("not");
        
        this.tabla_simbolos=new TablaSimbolos(0,"",0);
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
    
    public void analisis_semantico (){
        mi_pascal();
    }
    
    private void match (String terminal){
        Token tk_actual=this.tokens_sintacticos.get(this.preanalisis);
        
        if(terminal.equalsIgnoreCase(tk_actual.get_lexema())){
            this.preanalisis++;
        }else{
            System.out.println("\nError Sintactico : *** Se esperaba el simbolo \""+terminal+"\" y se encontro \""+tk_actual.get_lexema()+"\" *** Linea "+tk_actual.get_linea_programa());
            System.exit(1);
        }
    }
    
    public void guardar_ts (){
        try{
            String nombre_archivo=this.archivo.substring(0, this.archivo.length()-4);
            PrintWriter salida=new PrintWriter(new FileOutputStream("ts_"+nombre_archivo+".txt"));
            salida.println(this.tabla_simbolos.mostrar_contenido());
            imprimir_ts(this.tabla_simbolos, salida);
            salida.close();
            
        }catch(FileNotFoundException ex){
            
            System.out.println(ex.getMessage());
            System.exit(1);
            
        }
    }
    
    private void imprimir_ts (TablaSimbolos ts, PrintWriter salida){
        ArrayList<TablaSimbolos> lista=ts.get_ts_inferiores();
        int i;
        int n=lista.size();
        if(!lista.isEmpty()){
            for(i=0; i<n; i++){
                salida.println(lista.get(i).mostrar_contenido());
                imprimir_ts(lista.get(i), salida);
            }
        }
    }
    
    //-----------------------------------------------------------------------------------
    //--- MiPascal ----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void mi_pascal (){
        int nivel_lexico=1;
        
        encabezado();
        //TS local al programa principal.
        definicion(this.tabla_simbolos);
        
        declaracion_subprogramas(this.tabla_simbolos,nivel_lexico);
        
        bloque(this.tabla_simbolos);
        
        //Fin del programa principal.
        int n=this.tokens_sintacticos.size()-1;
        if(this.preanalisis > n){
            System.out.println("\nError Sintactico : *** Se esperaba el simbolo \""+"."+"\" y se encontro \""+"\" *** Linea "+this.tokens_sintacticos.get(n).get_linea_programa());
            System.exit(1);
        }
        match(".");
    }
    
    //-----------------------------------------------------------------------------------
    //--- Encabezado --------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void encabezado (){
        match("program");
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        identificador(token); //No avanza preanalisis.
        
        //Configuramos la TS PRINCIPAL con el nombre del programa principal como propietario.
        this.tabla_simbolos.set_propietario(token.get_lexema());
        
        this.preanalisis++;
        match(";");
    }
    
    //-----------------------------------------------------------------------------------
    //--- Definicion --------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void definicion (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            
            case "const" : this.preanalisis++;
                           const_def(ts);
                           token=this.tokens_sintacticos.get(this.preanalisis);
                           switch(token.get_lexema()){
                               
                               case "type" : this.preanalisis++;
                                             type_def(ts);
                                             token=this.tokens_sintacticos.get(this.preanalisis);
                                             if(token.get_lexema().equalsIgnoreCase("var")){
                                                 this.preanalisis++;
                                                 var_def(ts);
                                             }
                                             break;
                               case "var" : this.preanalisis++;
                                            var_def(ts);
                                            token=this.tokens_sintacticos.get(this.preanalisis);
                                            if(token.get_lexema().equalsIgnoreCase("type")){
                                                this.preanalisis++;
                                                type_def(ts);
                                            }
                                            break;
                           }
                           break;
            case "type" : this.preanalisis++;
                          type_def(ts);
                          token=this.tokens_sintacticos.get(this.preanalisis);
                          switch(token.get_lexema()){
                              case "const" : this.preanalisis++;
                                             const_def(ts);
                                             token=this.tokens_sintacticos.get(this.preanalisis);
                                             if(token.get_lexema().equalsIgnoreCase("var")){
                                                 this.preanalisis++;
                                                 var_def(ts);
                                             }
                                             break;
                              case "var" : this.preanalisis++;
                                           var_def(ts);
                                           token=this.tokens_sintacticos.get(this.preanalisis);
                                           if(token.get_lexema().equalsIgnoreCase("const")){
                                               this.preanalisis++;
                                               const_def(ts);
                                           }
                                           break;
                          }
                          break;
            case "var" : this.preanalisis++;
                         var_def(ts);
                         token=this.tokens_sintacticos.get(this.preanalisis);
                         switch(token.get_lexema()){
                             case "const" : this.preanalisis++;
                                            const_def(ts);
                                            token=this.tokens_sintacticos.get(this.preanalisis);
                                            if(token.get_lexema().equalsIgnoreCase("type")){
                                                this.preanalisis++;
                                                type_def(ts);
                                            }
                                            break;
                             case "type" : this.preanalisis++;
                                           type_def(ts);
                                           token=this.tokens_sintacticos.get(this.preanalisis);
                                           if(token.get_lexema().equalsIgnoreCase("const")){
                                               this.preanalisis++;
                                               const_def(ts);
                                           }
                                           break;
                         }
                         break;
            default : //Segmento de definicion vacio.
            
        }
    }
    
    private boolean identificador (Token token){
        boolean r=false;
        String id=token.get_lexema();
        char c=id.charAt(0);
        
        if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(id))
            r=true;
        else{
            System.out.println("\nError Sintactico : *** Simbolo "+id+" incompatible, se espera un identificador *** Linea "+token.get_linea_programa());
            System.exit(1);
        }
        
        return r;
    }
    
    private ArrayList<String> secuencia_ids (){
        boolean fin=false;
        ArrayList<String> ids=new ArrayList();
        
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
        constante(ts);match(";");const_fin(ts);
    }
    
    private void const_fin (TablaSimbolos ts){
        if(this.palabras_reservadas.contains(this.tokens_sintacticos.get(this.preanalisis).get_lexema()))
            ; //Presencia de cadena nula.
        else
            const_def(ts);
    }
    
    private void constante (TablaSimbolos ts){
        ArrayList ids=secuencia_ids();
        match("=");
        String dato=this.tokens_sintacticos.get(this.preanalisis).get_lexema();
        this.preanalisis++;
        //Verifiamos que dato sea un numero entero, true o false mediante un esquema de traduccion.
        
        int i;
        int n=ids.size();
        String id="";
        for(i=0; i<n; i++){
            id=(String)ids.get(i);
            ts.insertar(id, new Constante(id,1,dato));
        }
    }
    
    private void type_def (TablaSimbolos ts){
        tipo(ts);match(";");type_fin(ts);
    }
    
    private void tipo (TablaSimbolos ts){
        ArrayList<String> ids=secuencia_ids();
        match("=");
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            //--- Tipos Primitivos ---
            case "integer" : this.preanalisis++;
                             //Tenemos una lista de ids, que representan nuevos tipos integer.
                             //Ej: tipo1, tipo2 = integer;
                             insertar_tipos(ts,ids,new TipoDato("",1,"integer","definido"));
                             break;
            case "boolean" : this.preanalisis++;
                             //Tenemos una lista de ids, que representan nuevos tipos boolean.
                             //Ej: tipo1, tipo2 = boolean;
                             insertar_tipos(ts,ids,new TipoDato("",1,"boolean","definido"));
                             break;
            //--- Tipos Estructurados ---
            case "("       : this.preanalisis++;
                             //--- Enumeracion ---
                             enum_(ts,ids);
                             break;
            case "array"   : this.preanalisis++;
                             Arreglo a=new Arreglo();
                             //Agregamos datos necesarios para hacer chequeos de tipo.
                             completar_arreglo(a);
                             //Pueden haber definiciones recursivas de arreglos.
                             agregar_tipo(a);
                             //Guardamos los nuevos tipos en la ts.
                             insertar_tipos(ts,ids,a);
                             break;
            case "record"  : this.preanalisis++;
                             reg(ts,ids);                             
                             break;
            default : //--- Subrango ---
                      //No avanzamos preanalisis porque debemos verificar que el token actual sea digito.
                      //Si el token actual es digito posiblemente estamos en presencia de un subrango.
                      sub(ts,ids);
        }
    }
    
    //--- Para reusar en seccion var ---
    
    private void enum_ (TablaSimbolos ts, ArrayList<String> ids){
        ArrayList<String> cuerpo_enum=secuencia_ids();
        match(")");
                             
        insertar_tipos(ts,ids, new Enumeracion("",1,cuerpo_enum));
    }
    
    private void sub (TablaSimbolos ts, ArrayList ids){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        int lis=0;
        int mul_lis=1;
        int lss=0;
        int mul_lss=1;
        //--- Digito ---
        if(token.get_lexema().equalsIgnoreCase("-")){//Presencia de numero negativo.
            this.preanalisis++;
            mul_lis=-1;
            token=this.tokens_sintacticos.get(this.preanalisis);
        }
        
        if(digito(token)){
            this.preanalisis++;
            lis=Integer.parseInt(token.get_lexema());
        }else{
            //Podemos tener cualquier otro caracter.
            System.out.println("\nError Sintactico : *** Simbolo "+token.get_lexema()+" inesperado, los limites de un subrango se construyen a partir de numeros enteros *** linea "+token.get_linea_programa());
            System.exit(1);
        }

        match("..");

        token=this.tokens_sintacticos.get(this.preanalisis);
        //--- Digito ---
        if(token.get_lexema().equalsIgnoreCase("-")){//Presencia de numero negativo.
            this.preanalisis++;
            mul_lss=-1;
            token=this.tokens_sintacticos.get(this.preanalisis);
        }
        
        if(digito(token)){
            this.preanalisis++;
            lss=Integer.parseInt(token.get_lexema());
        }else{
            System.out.println("\nError Sintactico : *** Simbolo "+token.get_lexema()+" inesperado, los limites de un subrango se construyen a partir de numeros enteros *** linea "+token.get_linea_programa());
            System.exit(1);
        }

        insertar_tipos(ts,ids,new Subrango("",1,mul_lis*lis,mul_lss*lss));
    }
    
    private void completar_arreglo (Arreglo a){
        match("[");
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        int li=0;
        int mul_li=1;
        int ls=0;
        int cantidad=0;
        int mul_ls=1;
        
        if(token.get_lexema().equalsIgnoreCase("-")){//Presencia de numero negativo.
            this.preanalisis++;
            mul_li=-1;
            token=this.tokens_sintacticos.get(this.preanalisis);
        }
        
        //Verificamos si el token actual es digito.
        if(digito(token)){
            this.preanalisis++;//Avanzamos preanalisis porque se produce un match.
            li=Integer.parseInt(token.get_lexema());
        }else{
            //Podemos tener cualquier otro caracter.
            System.out.println("\nError Sintactico : *** Simbolo "+token.get_lexema()+" inesperado, los limites de un subrango se construyen a partir de numeros enteros *** linea "+token.get_linea_programa());
            System.exit(1);
        }
        
        match("..");
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("-")){//Presencia de numero negativo.
            this.preanalisis++;
            mul_ls=-1;
            token=this.tokens_sintacticos.get(this.preanalisis);
        }
        
        //Nuevamente verificamos si el token actual es digito.
        if(digito(token)){
            this.preanalisis++;
            ls=Integer.parseInt(token.get_lexema());
        }else{
            //Podemos tener cualquier otro caracter.
            System.out.println("\nError Sintactico : *** Simbolo "+token.get_lexema()+" inesperado, los limites de un subrango se construyen a partir de numeros enteros *** linea "+token.get_linea_programa());
            System.exit(1);
        }
        
        match("]");match("of");//Despues de esta ultima llamada el simbolo de preanalisis queda posicionado en el tipo del arreglo.

        //Completamos parte del arreglo.
        a.set_limite_inferior(mul_li*li);
        a.set_limite_superior(mul_ls*ls);
        
        if(li==0)
            cantidad=ls+1;
        
        a.set_cantidad_elementos(cantidad);
        
    }
    
    //Agregamos el tipo de dato asociado a un arreglo. Antes de conocer el tipo de un arreglo pueden aparecer
    //otras definiciones de arreglo.
    //Ej: array[ 1..10 ] of array[ 1..10 ] of integer.
    private void agregar_tipo (Arreglo a){
        //Ahora analizamos el tipo de dato de los eltos del arreglo.
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        switch(token.get_lexema()){
            //--- Tipos Primitivos ---
            case "integer" : this.preanalisis++;
                             a.set_tipo_dato(new TipoDato("integer",1,"integer","primitivo"));
                             break;
            case "boolean" : this.preanalisis++;
                             a.set_tipo_dato(new TipoDato("boolean",1,"boolean","primitivo"));
                             break;
            //--- Tipos Estructurados ---
            case "("       : this.preanalisis++;
                             //--- Enumeracion ---
                             ArrayList<String> cuerpo_enum=secuencia_ids();
                             match(")");
                             a.set_tipo_dato(new Enumeracion("",1,cuerpo_enum));
                             break;
            case "array"   : this.preanalisis++;
                             //--- El arreglo posee definiciones recursivas a derecha ---
                             match("[");
                             token=this.tokens_sintacticos.get(this.preanalisis);
                             int li=0;
                             int mul_li=1;
                             int ls=0;
                             int mul_ls=1;
                             
                             if(token.get_lexema().equalsIgnoreCase("-")){//Presencia de numero negativo.
                                 this.preanalisis++;
                                 mul_li=-1;
                                 token=this.tokens_sintacticos.get(this.preanalisis);
                             }
                             
                             //Verificamos si el token actual es digito.
                             if(digito(token)){
                                 this.preanalisis++;//Avanzamos preanalisis porque se produce un match.
                                 li=Integer.parseInt(token.get_lexema());
                             }else{
                                 System.out.println("\nError Sintactico : *** Simbolo "+token.get_lexema()+" inesperado, los limites de un subrango se construyen a partir de numeros enteros *** linea "+token.get_linea_programa());
                                 System.exit(1);
                             }
                             
                             match("..");
                             
                             token=this.tokens_sintacticos.get(this.preanalisis);
                             if(token.get_lexema().equalsIgnoreCase("-")){//Presencia de numero negativo.
                                 this.preanalisis++;
                                 mul_ls=-1;
                                 token=this.tokens_sintacticos.get(this.preanalisis);
                             }
                             
                             
                             //Nuevamente verificamos si el token actual es digito.
                             if(digito(token)){
                                 this.preanalisis++;
                                 ls=Integer.parseInt(token.get_lexema());
                             }else{
                                 System.out.println("\nError Sintactico : *** Simbolo "+token.get_lexema()+" inesperado, los limites de un subrango se construyen a partir de numeros enteros *** linea "+token.get_linea_programa());
                                 System.exit(1);
                             }
                             
                             match("]");match("of");
                             
                             //Creamos un nuevo arreglo y lo guardamos en la lista de definiciones rec.
                             a.set_definicion_recursiva(new Arreglo(0,mul_li*li,mul_ls*ls));
                             
                             //LLamamos recursivamente para saber si existen nuevas definiciones recursivas. Caso
                             //contrario agregamos el tipo de dato al arreglo.
                             agregar_tipo(a);
                             
                             break;
            case "record"  : this.preanalisis++;//En este momento nos debemos encontar con un identificador.
                             Registro registro=new Registro();
                             
                             //r agrega los campos al objeto registro. 
                             r(registro);

                             match("end");
                             
                             //El nuevo registro creado representa el tipo asociado a los eltos. del arreglo.
                             a.set_tipo_dato(registro);
                             break;
            default : //No avanzamos preanalisis porque debemos verificar que el token actual sea identificador.
                      //--- Tipo definido por el usuario ---
                      char c=token.get_lexema().charAt(0);
                      
                      if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(token.get_lexema())){
                          this.preanalisis++;
                          a.set_tipo_dato(new TipoDato(token.get_lexema(),1,"","definido"));
                      }else{
                          //--- Subrango ---
                          //No avanzamos preanalisis porque debemos verificar que el token actual sea digito o menos.
                          token=this.tokens_sintacticos.get(this.preanalisis);
                          int lis=0;
                          int mul_lis=1;
                          int lss=0;
                          int mul_lss=1;
                          
                          if(token.get_lexema().equalsIgnoreCase("-")){//Presencia de digito negativo.
                              this.preanalisis++;
                              mul_lis=-1;
                              token=this.tokens_sintacticos.get(this.preanalisis);
                          }

                          if(digito(token)){
                              lis=Integer.parseInt(token.get_lexema());
                              this.preanalisis++;
                          }else{
                              System.out.println("\nError Sintactico : *** Simbolo "+token.get_lexema()+" inesperado, los limites de un subrango se construyen a partir de numeros enteros *** linea "+token.get_linea_programa());
                              System.exit(1);
                          }
                          
                          match("..");

                          token=this.tokens_sintacticos.get(this.preanalisis);
                          if(token.get_lexema().equalsIgnoreCase("-")){//Presencia de numero negativo.
                              this.preanalisis++;
                              mul_lss=-1;
                              token=this.tokens_sintacticos.get(this.preanalisis);
                          }
                          
                          if(digito(token)){
                              lss=Integer.parseInt(token.get_lexema());
                              this.preanalisis++;
                          }else{
                              System.out.println("\nError Sintactico : *** Simbolo "+token.get_lexema()+" inesperado, los limites de un subrango se construyen a partir de numeros enteros *** linea "+token.get_linea_programa());
                              System.exit(1);
                          }
                          
                          a.set_tipo_dato(new Subrango("",1,mul_lis*lis,mul_lss*lss));
                      }
        }
    }
    
    private void reg (TablaSimbolos ts, ArrayList<String> ids){
        //Creamos un objeto Registro para guardar en el mismo todos sus campos, que pueden ser de tipo
        //integer o boolean.
        Registro reg_principal=new Registro();
        
        r(reg_principal);
        
        match("end");
        
        insertar_tipos(ts,ids,reg_principal);
        
    }
    
    //--- Para reusar en seccion var ---
    
    private void insertar_tipos (TablaSimbolos ts, ArrayList<String> ids, Simbolo s){
        int i;
        int n=ids.size();
        String lex="";
        for(i=0; i<n; i++){
            lex=ids.get(i);
            //s.set_lexema(lex);
            ts.insertar(lex, s);
        }
    }
    
    private boolean digito (Token token){
        String lex=token.get_lexema();
        char c=lex.charAt(0);
        return ((int)c>=48 && (int)c<=57);
    }
    
    private void type_fin (TablaSimbolos ts){
        if(this.palabras_reservadas.contains(this.tokens_sintacticos.get(this.preanalisis).get_lexema()))
            ; //Presencia de cadena nula si el simbolo actual es una palabra reservada. Despues de la 
        //seccion type pueden aparecer var, const, function, procedure o begind. En caso de error 
        //sintactico puede aparecer cualquier otra palabra. De todas formas debemos finalizar la ejecucion de 
        //type_def. No es responsabilidad de type_def emitir un mensaje de error.
        else
            type_def(ts);
    }
    
    private void r (Registro reg_principal){
        campo(reg_principal);R(reg_principal);
    }
    
    private void campo (Registro reg_principal){
        ArrayList<String> ids=secuencia_ids();
        match(":");
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        //Ahora viene el tipo de dato asociado al campo del registro.
        switch(token.get_lexema()){
            case "integer" : this.preanalisis++;
                             reg_principal.agregar_parametro(new Parametro(ids,new TipoDato("integer",1,"integer","primitivo")));
                             break;
            case "boolean" : this.preanalisis++;
                             reg_principal.agregar_parametro(new Parametro(ids,new TipoDato("boolean",1,"boolean","primitivo")));
                             break;
            case "("       : //this.preanalisis++;
                             //ArrayList<String> cuerpo_enum=secuencia_ids();
                             //match(")");
                             //reg_principal.agregar_parametro(new Parametro(ids,new Estructurado("",new Enumeracion("",1,cuerpo_enum))));
                             System.out.println("\nError Sintactico : *** No se admiten campos de tipo Enumeracion *** Linea "+token.get_linea_programa());
                             System.exit(1);
                             break;
            case "array"   : 
                             //break;
            case "record"  : //this.preanalisis++;
                             //Registro reg_anidado=new Registro();
                             //r(reg_anidado);
                             //reg_principal.agregar_parametro(new Parametro(ids,new Estructurado("",reg_anidado)));
                             System.out.println("\nError Sintactico : *** No se admiten campos de tipo "+token.get_lexema()+" *** Linea "+token.get_linea_programa());
                             System.exit(1);
                             break;
            default : //--- Subrango ---
                      if(token.get_lexema().equalsIgnoreCase("-")){
                          System.out.println("\nError Sintactico : *** No se admiten campos de tipo Subrango *** Linea "+token.get_linea_programa());
                          System.exit(1);
                      }
                      
                      if(digito(token)){
//                          token=this.tokens_sintacticos.get(this.preanalisis);
//                          String li=token.get_lexema();
//                          this.preanalisis++;
//                          match("..");
//                          
//                          if(digito(token)){
//                              token=this.tokens_sintacticos.get(this.preanalisis);
//                              String ls=token.get_lexema();
//                              this.preanalisis++;
//                              reg_principal.agregar_parametro(new Parametro(ids,new Estructurado("",new Subrango("",1,li,ls))));
//                          }else{
//                              System.out.println("Error Sintactico : *** ***");
//                              System.exit(1);
//                          }
                            System.out.println("\nError Sintactico : *** No se admiten campos de tipo Subrango *** Linea "+token.get_linea_programa());
                            System.exit(1);
                      }else{
                          //--- Tipo definido por el usuario ---
                          if(identificador(token)){
                              System.out.println("\nError Sintactico : *** Tipo de dato "+token.get_lexema()+" inesperado. No se admiten tipos definidos por el usuario *** Linea "+token.get_linea_programa());
                              System.exit(1);
                          }
                      }
        }
    }
    
    private void R (Registro reg_principal){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase(";")){//Es la coma que separa los campos del registro.
            this.preanalisis++;
            
            //Despues de r debe venir un identificador, si no es asi, hacemos uso de la cadena nula.
            token=this.tokens_sintacticos.get(this.preanalisis);
            char c=token.get_lexema().charAt(0);
            //Si el simbolo actual es un identificador y no se corresponde con una palabra reservada
            //significa que estamos en presencia de un nuevo campo del registro.
            if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(token.get_lexema())){
                //Si avanzamos preanalisis estamos perdiendo un identificador. Todos los ids se obtienen en
                //campo(reg).
                r(reg_principal);
            }else
                ; //Presencia de cadena nula. La proxima palabra reservada debe ser 'end'.
        }else
            ;//Presencia de cadena nula. La proxima palabra reservada debe ser 'end'.
    }
        
    private void var_def (TablaSimbolos ts){
        variable(ts);match(";");var_fin(ts);
    }
    
    private void variable (TablaSimbolos ts){
        Strings conflicto=new Strings("");
        ArrayList<String> ids=secuencia_ids();
        
        //Chequeo de unicidad.
        if(ts.chequeo_de_unicidad(ids,conflicto)){
            Token tk=this.tokens_sintacticos.get(this.preanalisis);
            System.out.println("\nError Semantico : *** Error de unicidad, el identificador \""+conflicto.get_string()+"\" ya se encuentra definido en el ambiente actual *** Linea "+tk.get_linea_programa());
            System.exit(1);
        }
        
        match(":");
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            //--- Primitivos ---
            case "integer" : this.preanalisis++;
                             insertar_tipos(ts,ids,new Variable(new TipoDato("integer",1,"integer","primitivo")));
                             break;
            case "boolean" : this.preanalisis++;
                             insertar_tipos(ts,ids,new Variable(new TipoDato("boolean",1,"boolean","primitivo")));
                             break;
            //--- Estructurados ---
            case "("       : this.preanalisis++;
                             ArrayList<String> cuerpo_enum=secuencia_ids();
                             match(")");
                             insertar_tipos(ts,ids,new Variable(new Enumeracion(cuerpo_enum)));
                             break;
            case "array"   : this.preanalisis++;
                             Arreglo a=new Arreglo();
                             //Agrega datos necesarios para hacer chequeos de tipo.
                             completar_arreglo(a);
                             //Pueden haber definiciones recursivas de arreglos.
                             agregar_tipo(a);
                             //Guardamos los nuevos tipos en la ts.
                             insertar_tipos(ts,ids,new Variable(a));
                             break;
            case "record"  : this.preanalisis++;
                             Registro registro=new Registro();
                             r(registro);
                             match("end");
                             insertar_tipos(ts,ids,new Variable(registro));
                             break;
            default : 
                      //--- Tipo definido por el usuario ---
                      char c=token.get_lexema().charAt(0);
                      
                      if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(token.get_lexema())){
                          this.preanalisis++;
                          insertar_tipos(ts,ids,new Variable(new TipoDato(token.get_lexema(),1,"","definido")));
                      }else{
                          //--- Subrango ---
                          int lis=0;
                          int mul_lis=1;
                          int lss=0;
                          int mul_lss=1;

                          if(token.get_lexema().equalsIgnoreCase("-")){//Presencia de numero negativo.
                              this.preanalisis++;
                              mul_lis=-1;
                              token=this.tokens_sintacticos.get(this.preanalisis);
                          }

                          if(digito(token)){
                              lis=Integer.parseInt(token.get_lexema());
                              this.preanalisis++;
                          }
                          
                          match("..");

                          token=this.tokens_sintacticos.get(this.preanalisis);
                          if(token.get_lexema().equalsIgnoreCase("-")){
                              this.preanalisis++;
                              mul_lss=-1;
                              token=this.tokens_sintacticos.get(this.preanalisis);
                          }
                          
                          if(digito(token)){
                              lss=Integer.parseInt(token.get_lexema());
                              this.preanalisis++;                              
                          }
                          
                          insertar_tipos(ts,ids,new Variable(new Subrango(mul_lis*lis,mul_lss*lss)));
                          
                      }
        }
    }
    
    private void var_fin (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        //Las palabras reservadas pueden ser const, type, function, procedure o begin.
        if(this.palabras_reservadas.contains(token.get_lexema()))
            ;//Presencia de cadena nula.
        else
            var_def(ts);
    }
    
    //-----------------------------------------------------------------------------------
    //--- Declaracion de Subprogramas ---------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void declaracion_subprogramas (TablaSimbolos ts_superior, int nivel_lexico){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            case "function"   : this.preanalisis++;
                                funcion(ts_superior,null,nivel_lexico);
                                declaracion_subprogramas(ts_superior,nivel_lexico);
                                break;
            case "procedure"  : this.preanalisis++;
                                procedimiento(ts_superior,null,nivel_lexico);
                                declaracion_subprogramas(ts_superior,nivel_lexico);
                                break;
            default : ; //Presencia de cadena nula. Significa que no hay definicion de subprogramas.
        }
    }
    
    private void funcion (TablaSimbolos ts_superior, TablaSimbolos ts_local, int nivel_lexico){
        Token token;
        String id="";
        //Para guardar el entorno local del subprograma.
        ts_local=new TablaSimbolos(this.id_entorno,"",nivel_lexico);
        this.id_entorno++;
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(identificador(token)){
            this.preanalisis++;
            id=token.get_lexema();
        }
        
        //Configuramos la TS LOCAL con el nombre de la funcion como propietario.
        ts_local.set_propietario(id);
        
        match("(");
        
        ArrayList<Parametro> parametro_formal=new ArrayList();
        param(parametro_formal);
        
        match(")");match(":");
        
        String tipo_retorno=this.tokens_sintacticos.get(this.preanalisis).get_lexema();
        switch(tipo_retorno){
            case "integer" :
            case "boolean" : this.preanalisis++;
                             break;
            default : System.out.println("\nError Sintactico : *** Tipo de dato incompatible "+tipo_retorno+" *** Linea "+this.tokens_sintacticos.get(this.preanalisis).get_linea_programa());
                      System.exit(1);
        }
                
        match(";");
        
        ts_local.insertar(id, new Funcion(id,1,new TipoDato(tipo_retorno,1,"",""),parametro_formal,""));
        
        //Esta funcion tambien forma parte de las definiciones locales de la ts_superior.
        ts_superior.insertar(id, new Funcion(id,1,new TipoDato(tipo_retorno,1,"",""),parametro_formal,""));
        
        definicion(ts_local);
        
        //Construimos la cadena estatica.
        ts_local.set_ts_superior(ts_superior);
        ts_superior.set_ts_inferior(ts_local);
        
        //Para subprogramas anidados.
        declaracion_subprogramas(ts_local,nivel_lexico++);
        
        bloque(ts_local);
        
        match(";");
    }
    
    private void param (ArrayList<Parametro> parametro_formal){
        TipoDato tipo=null;
        ArrayList<String> ids=secuencia_ids();
        
        match(":");
        
        String tipo_dato=this.tokens_sintacticos.get(this.preanalisis).get_lexema();
        this.preanalisis++;
        
        switch(tipo_dato){
            case "integer" : 
                             
            case "boolean" : tipo=new TipoDato(tipo_dato,1,tipo_dato,"primitivo");
                             break;
            default : //tipo=new TipoDato(tipo_dato,1,"","definido");
                      System.out.println("\nError Sintactico : *** Tipo de dato incompatible "+tipo_dato+" *** Linea "+this.tokens_sintacticos.get(this.preanalisis-1).get_linea_programa());
                      System.exit(1);
        }
        
        parametro_formal.add(new Parametro(ids,tipo));
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase(",")){
            this.preanalisis++;
            param(parametro_formal);
        }else
            ; //Presencia de cadena nula. Representa caso base.
    }
    
    private void procedimiento (TablaSimbolos ts_superior, TablaSimbolos ts_local, int nivel_lexico){
        Token token;
        String id="";
        
        ts_local=new TablaSimbolos(this.id_entorno,"",nivel_lexico);
        this.id_entorno++;
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(identificador(token)){
            this.preanalisis++;
            id=token.get_lexema();
        }
        
        //Configuramos la TS LOCAL con el nombre del procedimiento como propietario.
        ts_local.set_propietario(id);
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase(";")){//En este caso se omiten parametros formales.
            this.preanalisis++;
            
            //  Enviamos un ArrayList vacio para evitar null_pointer_excepton.
            ts_local.insertar(id, new Procedimiento(id,1,new ArrayList(),""));
            ts_superior.insertar(id, new Procedimiento(id,1,new ArrayList(),""));
            
        }else{
            match("(");

            ArrayList<Parametro> parametro_formal=new ArrayList();
            param(parametro_formal);

            match(")");

            match(";");

            ts_local.insertar(id, new Procedimiento(id,1,parametro_formal,""));
            ts_superior.insertar(id, new Procedimiento(id,1,parametro_formal,""));
        }
        
        definicion(ts_local);
        
        //Construimos la cadena estatica.
        ts_local.set_ts_superior(ts_superior);
        ts_superior.set_ts_inferior(ts_local);
        
        //Consideramos subprogramas anidados.
        declaracion_subprogramas(ts_local,nivel_lexico++);
        
        bloque(ts_local);
        
        match(";");
    }
    
    //-----------------------------------------------------------------------------------
    //--- Bloque ------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    /*
     * ts puede ser una tabla de simbolos local a un subprograma o la tabla de simbolos principal.
    */
    private void bloque (TablaSimbolos ts){
        match("begin");
        S(ts);
        match("end");
    }
    
    private void S (TablaSimbolos ts){
        sentencia(ts);S1(ts);
    }
    
    private void S1 (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase(";")){
            this.preanalisis++;
            token=this.tokens_sintacticos.get(this.preanalisis);
            
            if(token.get_lexema().equalsIgnoreCase("end"))
                ; //Presencia de cadena nula. S11 deriva en landa.
            else
                S(ts);
        }else
            ; //Presencia de cadena nula. S1 deriva en landa.
    }
    
    private void sentencia (TablaSimbolos ts){
        Strings sentencia_tipo=new Strings("");
        Strings succ_tipo=new Strings("");
        Strings pred_tipo=new Strings("");
        String var="";
        String tipo="";
        Simbolo simbolo=null;
        Token tk=null;
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        this.inicio_exp=token;
        switch(token.get_lexema()){
            case "if"     : this.preanalisis++;
                            alternativa(ts);
                            break;
            case "while"  : this.preanalisis++;
                            repetitiva(ts);
                            break;
            case "case"   : this.preanalisis++;
                            seleccion_multiple(ts);
                            break;
            case "read"   : this.preanalisis++;
                            read(ts);
                            break;
            case "write"  : this.preanalisis++;
                            write(ts);
                            break;
            case "succ"   : this.preanalisis++;
                            succ(ts, succ_tipo);
                            sentencia_tipo.set_string(succ_tipo.get_string());
                            break;
            case "pred"   : this.preanalisis++;
                            pred(ts, pred_tipo);
                            sentencia_tipo.set_string(pred_tipo.get_string());
                            break;
            //--- Identificador ---
            default : 
                    //La llamada a identificador(token) generaba un error si el bloque no tenia sentencias.
                    char c=token.get_lexema().charAt(0);
                    if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(token.get_lexema())){
                        tk=token;
                        this.preanalisis++;
                        token=this.tokens_sintacticos.get(this.preanalisis);
                        
                        ArrayList<Parametro> argumentos=new ArrayList();
                        
                        switch(token.get_lexema()){
                            case "."  : //--- Acceso a registro, asignacion ---
                                        break;
                            case "["  : //--- Acceso a arreglo, asignacion ---
                                        if(!(simbolo instanceof Variable)){
                                            System.out.println("\nError Semantico : *** El identificador \""+tk.get_lexema()+"\" no se corresponde con una definicion de ARREGLO *** Linea "+tk.get_linea_programa());
                                            System.exit(1);
                                        }
                                        
                                        Simbolo td=((Variable)simbolo).get_tipo_dato();
                                                   
                                        //Significa que estamos en presencia de un tipo definido por el usuario.
                                        if(td instanceof TipoDato){
                                            String id_a=((TipoDato)td).get_nombre_tipo();
                                            td=this.obtener_valor(ts, new Token(id_a));
                                        }

                                        //   Verificamos si el identificador se encuentra definido como Arreglo.
                                        if(!(td instanceof Arreglo)){
                                            System.out.println("\nError Semantico : *** El identificador \""+tk.get_lexema()+"\" debe estar definido como ARREGLO *** Linea "+tk.get_linea_programa());
                                            System.exit(1);
                                        }
                                
                                        this.preanalisis++;
                                        
                                        Strings tipo_syn=new Strings("");
                                        expresion(ts, tipo_syn);

                                        if(!(tipo_syn.get_string().equalsIgnoreCase("integer"))){
                                            System.out.println("\nError Semantico : *** Tipos incompatibles, el indice de un arreglo debe ser una variable o expresion de tipo integer *** Linea "+tk.get_linea_programa());
                                            System.exit(1);
                                        }
                                        
                                        match("]");
                                        match(":=");
                                        
                                        String tipo_dato_arreglo=((Arreglo)td).chequeo_de_tipos(tk);
                                        
                                        //argumento internamente llama a expresion(ts, exp.tipo, exp.cod) que genera el codigo MEPA correspondiente al lado derecho de la asignacion.
                                        argumento(ts, argumentos);
                                        
                                        tipo=((TipoDato)argumentos.get(0).get_tipo_dato()).get_nombre_tipo();
                                                
                                        if(tipo_dato_arreglo.equalsIgnoreCase(tipo)){
                                            sentencia_tipo.set_string(tipo);
                                        }else{
                                            System.out.println("\nError Semantico : *** Tipos incompatibles, esta intentando asignar una expresion de tipo "+tipo+", en una variable de tipo "+tipo_dato_arreglo+" *** Linea "+tk.get_linea_programa());
                                            System.exit(1);
                                        }
                                        
                                        break;
                            case ":=" : //--- Asignacion ---
                                        this.preanalisis++;
                                        //  Podemos asignar true, false o una expresion que puede ser acceso a arreglo-registro, llamada a subprograma
                                        //  llamada a funciones predefinidas succ-pred etc.
                                        argumento(ts, argumentos);
                                        
                                        //--- Esquema de traduccion ---
                                        simbolo=this.obtener_valor(ts, tk);
                                        if(simbolo != null){
                                            if(simbolo instanceof Variable){
                                                TipoDato tipo_dato=(TipoDato)((Variable)simbolo).get_tipo_dato();
                                                tipo=((TipoDato)argumentos.get(0).get_tipo_dato()).get_nombre_tipo();
                                                
                                                if(tipo_dato.get_nombre_tipo().equalsIgnoreCase(tipo)){
                                                    sentencia_tipo.set_string(tipo);
                                                }else{
                                                    System.out.println("\nError Semantico : *** Tipos incompatibles, esta intentando asignar una expresion de tipo "+tipo+", en una variable de tipo "+tipo_dato.get_nombre_tipo()+" *** Linea "+tk.get_linea_programa());
                                                    System.exit(1);
                                                }
                                            }else{
                                                if(simbolo instanceof TipoDato){
                                                    tipo=((TipoDato)argumentos.get(0).get_tipo_dato()).get_nombre_tipo();
                                                    String tipo_izq=((TipoDato)simbolo).get_nombre_tipo();
                                                    if(tipo.equalsIgnoreCase(tipo_izq))
                                                        sentencia_tipo.set_string(tipo);
                                                    else{
                                                        System.out.println("\nError Semantico : *** Tipos incompatibles, esta intentando asignar una expresion de tipo "+tipo+", en una variable de tipo "+tipo_izq+" *** Linea "+tk.get_linea_programa());
                                                        System.exit(1);
                                                    }
                                                }else
                                                    if(simbolo instanceof Constante){
                                                        System.out.println("\nError Semantico : *** No es posible modificar el contenido de una CONSTANTE en tiempo de ejecucion *** Linea "+token.get_linea_programa());
                                                        System.exit(1);
                                                    }else{
                                                        if(simbolo instanceof Funcion){
                                                            if(ts.get_propietario().equalsIgnoreCase(tk.get_lexema())){
                                                                tipo=((TipoDato)argumentos.get(0).get_tipo_dato()).get_nombre_tipo();
                                                                String tipo_retorno=((TipoDato)(((Funcion)simbolo).get_tipo_retorno())).get_nombre_tipo();
                                                                
                                                                if(tipo_retorno.equalsIgnoreCase(tipo))
                                                                    sentencia_tipo.set_string(tipo_retorno);
                                                                else{
                                                                    System.out.println("\nError Semantico : *** Tipos incompatibles, esta intentando asignar una expresion de tipo "+tipo+", en la variable \""+tk.get_lexema()+"\" que se corresponde con el nombre de una FUNCION que posee como retorno un elemento de tipo "+tipo_retorno+" *** Linea "+tk.get_linea_programa());
                                                                    System.exit(1);
                                                                }
                                                            }else{
                                                                System.out.println("\nError Semantico : *** No es posible asignar un valor en la variable \""+tk.get_lexema()+"\" porque la misma se corresponde con una FUNCION y no estamos en el ambiente adecuado *** Linea "+tk.get_linea_programa());
                                                                System.exit(1);
                                                            }
                                                                                                                        
                                                        }else{
                                                            if(simbolo instanceof Registro)
                                                                var="REGISTRO";
                                                            if(simbolo instanceof Procedimiento)
                                                                var="PROCEDIMIENTO";
                                                            if(simbolo instanceof Enumeracion)
                                                                var="ENUMERACION";
                                                            if(simbolo instanceof Subrango)
                                                                var="SUBRANGO";

                                                            System.out.println("\nError Semantico : *** No es posible realizar asignaciones en identificadores de tipo "+var+" *** Linea "+tk.get_linea_programa());
                                                            System.exit(1);
                                                        }
                                                    }
                                            }
                                        }else{
                                            System.out.println("\nError Semantico : *** El identificador \""+tk.get_lexema()+"\" no se encuentra definido *** Linea "+tk.get_linea_programa());
                                            System.exit(1);
                                        }
                                        
                                                                                                                        
                                        break;
                            case "("  : //--- Llamada a Subprograma ---
                                
                                        simbolo=this.obtener_valor(ts, tk);
                                        //  Verificamos si el identificador se encuentra definido em la TS.
                                        if(simbolo == null){
                                            System.out.println("\nError Semantico : *** El identificador \""+tk.get_lexema()+"\" no se encuentra definido *** Linea "+tk.get_linea_programa());
                                            System.exit(1);
                                        }
                                        //   Verificamos si el identificador se encuentra definido como Funcion o Procedimiento.     
                                        if(!((simbolo instanceof Funcion) || (simbolo instanceof Procedimiento))){
                                           System.out.println("\nError Semantico : *** El identificador \""+tk.get_lexema()+"\" debe estar definido como FUNCION o PROCEDIMIENTO *** Linea "+tk.get_linea_programa());
                                           System.exit(1);
                                        }
                                         
                                        this.preanalisis++;
                                        
                                        
                                        args(ts, argumentos);
                                        
                                        //   Verificamos cantidad y tipos de parametros para una Funcion o Procedimiento.
                                        if(simbolo instanceof Funcion)
                                            sentencia_tipo.set_string(((Funcion) simbolo).chequeo_de_tipos(tk, argumentos));
                                        else
                                            sentencia_tipo.set_string(((Procedimiento) simbolo).chequeo_de_tipos(tk, argumentos));
                                        
                                        
                                        match(")");
                                        
                                        token=this.tokens_sintacticos.get(this.preanalisis);
                                        //   Verificamos si un procedimiento no esta incluido en una expresion.
                                        if(!token.get_lexema().equalsIgnoreCase(";") && (simbolo instanceof Procedimiento)){
                                            System.out.println("\nError Semantico : *** No se puede utilizar un Procedimiento como operando en una expresion aritmetica, relacional o booleana *** linea "+tk.get_linea_programa());
                                            System.exit(1);
                                        }
                                        
                                        break;
                        }
                    }//Podriamos tener cero sentencias???. Si se aceptan cero sentencias no debemos generar error en la llamada a identificador(token).
                    else
                        ; //Presencia de cadena nula. En este caso tenemos cero sentencias en un bloque.
                
        }
    }
    
    private void alternativa (TablaSimbolos ts){
        Strings alternativa_tipo=new Strings("");
        Strings expresion_tipo=new Strings("");
        Token token=null;
        
        expresion(ts, expresion_tipo);
        
        //--- Esquema de traduccion ---
        if(expresion_tipo.get_string().equalsIgnoreCase("boolean"))
            alternativa_tipo.set_string("void");
        else{
            token=this.tokens_sintacticos.get(this.preanalisis);
            System.out.println("\nError Semantico : *** Tipos incompatibles, la estructura \"if\" requiere una expresion de tipo boolean. Se encontro una expresion de tipo "+expresion_tipo.get_string()+" *** Linea "+token.get_linea_programa());
            System.exit(1);
        }
        
        match("then");
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("begin")){ //Presencia de un bloque.
            //this.preanalisis++;
            bloque(ts);
        }else
            sentencia(ts);
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("else")){
            this.preanalisis++;
            token=this.tokens_sintacticos.get(this.preanalisis);
            
            if(token.get_lexema().equalsIgnoreCase("begin")){
                //this.preanalisis++;
                bloque(ts);
            }else
                sentencia(ts);
        }else
            ; //Presencia de cadena nula. Significa que no hay bloque else.
    }
    
    private void repetitiva (TablaSimbolos ts){
        Strings expresion_tipo=new Strings("");
        Strings repetitiva_tipo=new Strings("");
        Token token=null;
        
        expresion(ts, expresion_tipo);
        
        //--- Esquema de traduccion ---
        if(expresion_tipo.get_string().equalsIgnoreCase("boolean"))
            repetitiva_tipo.set_string("void");
        else{
            token=this.tokens_sintacticos.get(this.preanalisis);
            System.out.println("\nError Semantico : *** Tipos incompatibles, la estructura \"while\" requiere una expresion de tipo boolean. Se encontro una expresion de tipo "+expresion_tipo.get_string()+" *** Linea "+token.get_linea_programa());
            System.exit(1);
        }
        
        match("do");
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("begin")){
            //this.preanalisis++;
            bloque(ts);
        }else
            sentencia(ts);
    }
    
    private void seleccion_multiple (TablaSimbolos ts){
        Strings seleccion_multiple=new Strings("");
        Strings expresion_tipo=new Strings("");
        Token token;
//        Token token=this.tokens_sintacticos.get(this.preanalisis);
//        
//        if(digito(token))
//            this.preanalisis++;
//        else//podriamos usar expresion(ts, expresion_tipo)
//            if(identificador(token)){
//                
//                //--- Esquema de traduccion ---
//                Variable variable=(Variable)this.obtener_valor(ts, token);
//                if(variable != null){
//                    TipoDato tipo_dato=(TipoDato)variable.get_tipo_dato();
//                    if(tipo_dato.get_nombre_tipo().equalsIgnoreCase("integer"))
//                        seleccion_multiple="void";
//                    else{
//                        System.out.println("\nError Semantico : *** Tipos incompatibles, la estructura \"case\" requiere una expresion de tipo integer. Se encontro una expresion de tipo "+tipo_dato.get_nombre_tipo()+" *** Linea "+token.get_linea_programa());
//                        System.exit(1);
//                    }
//                }else{
//                    System.out.println("\nError Semantico : *** El identificador \""+token.get_lexema()+"\" no se encuentra definido *** Linea "+token.get_linea_programa());
//                    System.exit(1);
//                }
//                
//                this.preanalisis++;
//            }
        expresion(ts, expresion_tipo);
        
        //--- ESquema de traduccion ---
        if(expresion_tipo.get_string().equalsIgnoreCase("integer"))
            seleccion_multiple.set_string("void");
        else{
            token=this.tokens_sintacticos.get(this.preanalisis);
            System.out.println("\nError Semantico : *** Tipos incompatibles, la estructura \"case\" requiere una expresion de tipo integer. Se encontro una expresion de tipo "+expresion_tipo.get_string()+" *** Linea "+token.get_linea_programa());
            System.exit(1);
        }
        
        match("of");
        
        opciones(ts);
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("else")){
            this.preanalisis++;
            token=this.tokens_sintacticos.get(this.preanalisis);
            
            if(token.get_lexema().equalsIgnoreCase("begin")){
                //this.preanalisis++;
                bloque(ts);
            }else
                sentencia(ts);
        }else
            ; //Presencia de cadena nula. No hay bloque else.
        
        match("end");
    }
    
    private void opciones (TablaSimbolos ts){
        opcion(ts);op(ts);
    }
    
    private void opcion (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        //if(digito(token) || identificador(token))
            //this.preanalisis++;
        char c=token.get_lexema().charAt(0);
        if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(token.get_lexema()))
            this.preanalisis++;
        else{
            //--- Digito ---
            if(token.get_lexema().equalsIgnoreCase("-")){//Presencia de numero negativo.
                this.preanalisis++;
                token=this.tokens_sintacticos.get(this.preanalisis);
            }

            if(digito(token)){
                this.preanalisis++;
            }else{
                //Podemos tener cualquier otro caracter.
                System.out.println("\nError Sintactico : *** Simbolo "+token.get_lexema()+" inesperado, los limites de un subrango se construyen a partir de numeros enteros *** linea "+token.get_linea_programa());
                System.exit(1);
            }

            //match("..");
            token=tokens_sintacticos.get(this.preanalisis);
            if(token.get_lexema().equalsIgnoreCase("..")){
                this.preanalisis++;
                token=this.tokens_sintacticos.get(this.preanalisis);
                //--- Digito ---
                if(token.get_lexema().equalsIgnoreCase("-")){//Presencia de numero negativo.
                    this.preanalisis++;
                    token=this.tokens_sintacticos.get(this.preanalisis);
                }

                if(digito(token)){
                    this.preanalisis++;
                }else{
                    System.out.println("\nError Sintactico : *** Simbolo "+token.get_lexema()+" inesperado, los limites de un subrango se construyen a partir de numeros enteros *** linea "+token.get_linea_programa());
                    System.exit(1);
                }
            }
        }
        
        match(":");
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("begin")){
            //this.preanalisis++;
            bloque(ts);
        }else
            sentencia(ts);
    }
    
    private void op (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase(";")){
            this.preanalisis++;
            token=this.tokens_sintacticos.get(this.preanalisis);
            
            //---ERROR SI HAY UN ELSE ---
            if(token.get_lexema().equalsIgnoreCase("else")){
                System.out.println("\nError Sintactico : *** No esta permitido el uso de ; antes de la palabra reservada else *** linea "+token.get_linea_programa());
                System.exit(1);
            }
            
            char c=token.get_lexema().charAt(0);
            //Podemos tener un digito, identificador o subrango.
            if(token.get_lexema().equalsIgnoreCase("-") || digito(token) || ((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(token.get_lexema()))){
                opciones(ts);
            }else              
                ; //Presencia de cadena nula. Determina el fin de las opciones del case con punto y coma.
        }else
            ; //Presencia de cadena nula. Determina el fin de las opciones del case sin punto y coma.
    }
    
    private void read (TablaSimbolos ts){
        Strings procedimiento_es=new Strings("");
        String tipo="";
        
        match("(");
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(identificador(token))
            this.preanalisis++;
        
        //--- Esquema de traduccion ---
        Simbolo simbolo=this.obtener_valor(ts, token);
        if(simbolo != null){
            if(simbolo instanceof Variable){
                TipoDato tipo_dato=(TipoDato)((Variable)simbolo).get_tipo_dato();
                if(tipo_dato.get_nombre_tipo().equalsIgnoreCase("integer"))
                    procedimiento_es.set_string("");
                else{
                    System.out.println("\nError Semantico : *** Tipos incompatibles, el precedimiento de entrada-salida \"read\" requiere como argumento una variable de tipo integer. Se encontro una variable de tipo "+tipo_dato.get_nombre_tipo()+" *** Linea "+token.get_linea_programa());
                    System.exit(1);
                }
            }else{
                if(simbolo instanceof Constante){
                    System.out.println("\nError Semantico : *** No es posible modificar el contenido de una CONSTANTE en tiempo de ejecucion *** Linea "+token.get_linea_programa());
                    System.exit(1);
                }else{
                    if(simbolo instanceof Funcion){
                        if(ts.get_propietario().equalsIgnoreCase(token.get_lexema())){
                            String tipo_retorno=((TipoDato)(((Funcion)simbolo).get_tipo_retorno())).get_nombre_tipo();

                            if(tipo_retorno.equalsIgnoreCase("integer"))
                                procedimiento_es.set_string(tipo_retorno);
                            else{
                                System.out.println("\nError Semantico : *** Tipos incompatibles, esta intentando asignar una expresion de tipo "+"integer"+", en la variable \""+token.get_lexema()+"\" que se corresponde con el nombre de una FUNCION que posee como retorno un elemento de tipo "+tipo_retorno+" *** Linea "+token.get_linea_programa());
                                System.exit(1);
                            }
                        }else{
                            System.out.println("\nError Semantico : *** No es posible asignar un valor en la variable \""+token.get_lexema()+"\" porque la misma se corresponde con una FUNCION y no estamos en el ambiente adecuado *** Linea "+token.get_linea_programa());
                            System.exit(1);
                        }
                    }else{             
                        if(simbolo instanceof Registro)
                            tipo="REGISTRO";
                        if(simbolo instanceof Procedimiento)
                            tipo="PROCEDIMIENTO";
                        if(simbolo instanceof Subrango)
                            tipo="SUBRANGO";
                        if(simbolo instanceof Enumeracion)
                            tipo="ENUEMRACION";
                        if(simbolo instanceof TipoDato)
                            tipo="TIPO DEFINIDO POR EL USUARIO";

                        System.out.println("\nError Semantico : *** El procedimiento de entrada-salida \"read\" requiere unicamente variables de tipo integer. Se encontro un elemento de tipo "+tipo+" *** Linea "+token.get_linea_programa());
                        System.exit(1);
                    }
                }
            }
        }else{
            System.out.println("\nError Semantico : *** El identificador \""+token.get_lexema()+"\" no se encuentra definido *** Linea "+token.get_linea_programa());
            System.exit(1);
        }
        
        match(")");
    }
    
    private void write (TablaSimbolos ts){
        match("(");
        
        Strings expresion_tipo=new Strings("");
        expresion(ts, expresion_tipo);
        
        //--- Esquema de traduccion ---
        
        match(")");
    }
        
    private void succ (TablaSimbolos ts, Strings succ_tipo){
        Token token=null;
        
        match("(");
        
        Strings expresion_tipo=new Strings("");
        expresion(ts, expresion_tipo);
        
        //--- Esquema de traduccion ---
        if(expresion_tipo.get_string().equalsIgnoreCase("integer") || expresion_tipo.get_string().equalsIgnoreCase("boolean"))
            succ_tipo.set_string(expresion_tipo.get_string());
        else{
            token=this.tokens_sintacticos.get(this.preanalisis);
            System.out.println("\nError Semantico : *** Tipos incompatibles, la funcion \"succ\" requiere un argumento de tipo integer o boolean. Se encontro un elemento de tipo "+expresion_tipo.get_string().toUpperCase()+" *** Linea "+token.get_linea_programa());
            System.exit(1);
        }
        
        match(")");
    }
    
    private void pred (TablaSimbolos ts, Strings pred_tipo){
        Token token=null;
        
        match("(");
        
        Strings expresion_tipo=new Strings("");
        expresion(ts, expresion_tipo);
        
        //--- Esquema de traduccion ---
        if(expresion_tipo.get_string().equalsIgnoreCase("integer") || expresion_tipo.get_string().equalsIgnoreCase("boolean"))
            pred_tipo.set_string(expresion_tipo.get_string());
        else{
            token=this.tokens_sintacticos.get(this.preanalisis);
            System.out.println("\nError Semantico : *** Tipos incompatibles, la funcion \"pred\" requiere un argumento de tipo intege o boolean. Se encontro un elemento de tipo "+expresion_tipo.get_string().toUpperCase()+" *** Linea "+token.get_linea_programa());
            System.exit(1);
        }
        
        match(")");
    }
    
    private void args (TablaSimbolos ts, ArrayList<Parametro> argumentos){
        Token token;
        argumento(ts, argumentos);
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase(",")){
            this.preanalisis++;
            args(ts, argumentos);
        }else
            ;
    }
    
    /*
    * argumento sirve para procesar argumentos en llamadas a subprogramas y para asignar expresiones de relacionales,
    * booleanas y aritmeticas a variables;
    */
    private void argumento (TablaSimbolos ts, ArrayList<Parametro> argumentos){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            //--- Constante Booleana ---
            case "true"    :
            case "false"   : //this.preanalisis++;
                             Strings expresion_tipo_1=new Strings("");
                             expresion(ts, expresion_tipo_1);
                             argumentos.add(new Parametro("expresion compuesta", new TipoDato(expresion_tipo_1.get_string(),1,expresion_tipo_1.get_string(),"no se")));
                             //argumentos.add(new Parametro(token.get_lexema(), new TipoDato("boolean",1,"boolean","primitivo")));
                             break;
            
            default : //exp();
                      //    En esta seccion solamnete nos interesa en tipo de cada argumento.
                      Strings expresion_tipo=new Strings("");
                      expresion(ts, expresion_tipo);
                      
                      //--- Esquema de traduccion ---
                      argumentos.add(new Parametro("expresion compuesta", new TipoDato(expresion_tipo.get_string(),1,expresion_tipo.get_string(),"no se")));
        }
    }
    
    //-----------------------------------------------------------------------------------
    //--- Expresiones -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void expresion (TablaSimbolos ts, Strings expresion_tipo){
        Strings t_tipo=new Strings("");
        Strings e1_tipo=new Strings("");
        
        T(ts, t_tipo);E1(ts, e1_tipo);
        
        //--- Esquema de traduccion ---
        if(e1_tipo.get_string().equalsIgnoreCase("void"))
            expresion_tipo.set_string(t_tipo.get_string());
        else
            if(t_tipo.get_string().equalsIgnoreCase(e1_tipo.get_string()))
                expresion_tipo.set_string(t_tipo.get_string());
            else{
                System.out.println("\nError Semantico : *** Tipos incompatibles, no es posible comparar el tipo "+t_tipo.get_string()+" con el tipo "+e1_tipo.get_string()+" *** Linea "+this.inicio_exp.get_linea_programa());
                System.exit(1);
            }
        
        //System.out.println("En expresion se sintetitiza el tipo expresion_tipo : "+expresion_tipo.get_string());
    }
    
    private void T (TablaSimbolos ts, Strings t_tipo){
        Strings f_tipo=new Strings("");
        Strings f_tipo_bis=new Strings("");
        Strings t1_tipo=new Strings("");
        
        F(ts, f_tipo);T1(ts, f_tipo_bis, t1_tipo);
        
        //--- Esquema de traduccion ---
        if(t1_tipo.get_string().equalsIgnoreCase("void"))
            t_tipo.set_string(f_tipo.get_string());
        else
            if(f_tipo.get_string().equalsIgnoreCase(t1_tipo.get_string()))
                t_tipo.set_string(f_tipo.get_string());
            else{
                System.out.println("\nError Semantico : *** Tipos incompatibles, no es posible comparar el tipo "+f_tipo.get_string()+" con el tipo "+t1_tipo.get_string()+" *** Linea "+this.inicio_exp.get_linea_programa());
                System.exit(1);
            }
        
        //System.out.println("En T, f_tipo : "+f_tipo.get_string()+" y t1_tipo : "+t1_tipo.get_string());
        //System.out.println("En T, se sintetitiza el tipo t_tipo : "+t_tipo.get_string());
    }
    
    private void F (TablaSimbolos ts, Strings f_tipo){
        Strings g_tipo=new Strings("");
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("not")){
            this.preanalisis++;G(ts, g_tipo);
            
            //--- Esquema de traduccion ---
            if(g_tipo.get_string().equalsIgnoreCase("boolean"))
                f_tipo.set_string("boolean");
            else{
                System.out.println("\nError Semantico : *** Tipos incompatibles, el operador \"not\" requiere un operando de tipo boolean. Precede a un operando de tipo "+g_tipo.get_string()+" *** Linea "+token.get_linea_programa());
                System.exit(1);
            }
        }else{
            G(ts, g_tipo);
            
            f_tipo.set_string(g_tipo.get_string());
        }
        
        //System.out.println("En F, el tipo g_tipo : "+g_tipo.get_string());
        //System.out.println("En F, se sintetitiza el tipo f_tipo : "+f_tipo.get_string());
    }
    
    private void G (TablaSimbolos ts, Strings g_tipo){
        Strings h_tipo=new Strings("");
        Strings h_tipo_bis=new Strings("");//Guarda el tipo que sintetiza la funcion H que se llama dentro de G1.
        Strings g1_tipo=new Strings("");
        //El problema esta en no replicar h_tipo, por la rama H queda en boolean pero como es el mismo objeto el
        //que pasa por la rama G1 se vuelve a modificar con integer.
        
        H(ts, h_tipo);System.out.println("En G pasa esto, h_tipo despues de H es : "+h_tipo.get_string());
        G1(ts, h_tipo_bis, g1_tipo);System.out.println("mientras que despues de G1 el valor de h_tipo_bis es : "+h_tipo_bis.get_string());
        System.out.println("Y el valor de G1 es : "+g1_tipo.get_string());
        //System.exit(1);
        //--- Esquema de traduccion ---
        if(g1_tipo.get_string().equalsIgnoreCase("void"))
            g_tipo.set_string(h_tipo.get_string());
        else
            if(h_tipo.get_string().equalsIgnoreCase(g1_tipo.get_string()) && h_tipo.get_string().equalsIgnoreCase("integer"))
                g_tipo.set_string("boolean");
            else{
                System.out.println("\nError Semantico : *** Tipos incompatibles, no es posible comparar el tipo "+h_tipo.get_string()+" con el tipo "+g1_tipo.get_string()+" *** Linea "+this.inicio_exp.get_linea_programa());
                System.exit(1);
            }
        
        System.out.println("En G, el tipo h_tipo : "+h_tipo.get_string()+" y g1_tipo : "+g1_tipo.get_string());
        System.out.println("En G, se sintetiza el tipo g_tipo : "+g_tipo.get_string()+" G1 es "+g1_tipo.get_string());
    }
    
    private void H (TablaSimbolos ts, Strings h_tipo){
        Strings i_tipo=new Strings("");
        Strings i_tipo_bis=new Strings("");
        Strings h1_tipo=new Strings("");
        
        I(ts, i_tipo);H1(ts, i_tipo_bis, h1_tipo);
        
        //--- Esquema de traduccion ---
        if(h1_tipo.get_string().equalsIgnoreCase("void"))
            h_tipo.set_string(i_tipo.get_string());
        else
            if(i_tipo.get_string().equalsIgnoreCase(h1_tipo.get_string()) && i_tipo.get_string().equalsIgnoreCase("integer"))
                h_tipo.set_string(i_tipo.get_string());
            else{
                System.out.println("\nError Semantico : *** Tipos incompatibles, no es posible comparar el tipo "+i_tipo.get_string()+" con el tipo "+h1_tipo.get_string()+" *** Linea "+this.inicio_exp.get_linea_programa());
                System.exit(1);
            }
        
        System.out.println("En H, el tipo i_tipo : "+i_tipo.get_string()+" y h1_tipo : "+h1_tipo.get_string());
        System.out.println("En H, se sintetitiza el tipo h_tipo : "+h_tipo.get_string());
    }
    
    private void I (TablaSimbolos ts, Strings i_tipo){
        Strings j_tipo=new Strings("");
        Strings j_tipo_bis=new Strings("");
        Strings i1_tipo=new Strings("");
        
        J(ts, j_tipo);I1(ts, j_tipo_bis, i1_tipo);
        
        //--- Esquema de traduccion ---
        if(i1_tipo.get_string().equalsIgnoreCase("void"))
            i_tipo.set_string(j_tipo.get_string());
        else
            if(j_tipo.get_string().equalsIgnoreCase(i1_tipo.get_string()))
                i_tipo.set_string(j_tipo.get_string());
            else{
                System.out.println("\nError Semantico : *** Tipos incompatibles, no es posible comparar el tipo "+j_tipo.get_string()+" con el tipo "+i1_tipo.get_string()+" *** Linea "+this.inicio_exp.get_linea_programa());
                System.exit(1);
            }
        
        System.out.println("En I, el tipo j_tipo : "+j_tipo.get_string()+" y i1_tipo : "+i1_tipo.get_string());
        System.out.println("En I, se sintetitiza el tipo i_tipo : "+i_tipo.get_string());
    }
    
    private void J (TablaSimbolos ts, Strings j_tipo){
        //--- Para sintetizar el tipo de un operando de expresion ---
        Strings k_tipo=new Strings("");
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase("-")){
            this.preanalisis++;K(ts, k_tipo);
            
            //--- Esquema de traduccion ---
            if(k_tipo.get_string().equalsIgnoreCase("integer"))
                j_tipo.set_string("integer");
            else{
                if(k_tipo.get_string().equalsIgnoreCase("boolean"))
                    j_tipo.set_string("boolean");
                else{
                    token=this.tokens_sintacticos.get(this.preanalisis);
                    System.out.println("\nError Semantico : *** El identificador \""+token.get_lexema()+"\" debe ser de tipo integer o boolean *** Linea "+token.get_linea_programa());
                    System.exit(1);
                }
            }
        }else{
            K(ts, k_tipo);
            //  Sintetizamos el tipo que contiene k_tipo.
            j_tipo.set_string(k_tipo.get_string());
        }
        
        System.out.println("En J, el tipo k_tipo : "+k_tipo.get_string());
        System.out.println("En J, se sintetitiza el tipo j_tipo : "+j_tipo.get_string());
    }
    
    private void I1 (TablaSimbolos ts, Strings j_tipo, Strings i1_tipo){
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        switch(token.get_lexema()){
            case "*" :
            case "/" : this.preanalisis++;
                       J(ts, j_tipo);
                       I1(ts, j_tipo, i1_tipo);
                       
                       //--- Esquema de traduccion ---
                       if(i1_tipo.get_string().equalsIgnoreCase("void")){
                           if(j_tipo.get_string().equalsIgnoreCase("integer"))
                               i1_tipo.set_string("integer");
                           else{
                               System.out.println("\nError Semantico : *** Tipos incompatibles, el operador "+token.get_lexema()+" requiere un operando de tipo integer, se sintetizo el tipo "+j_tipo.get_string()+" *** Linea "+token.get_linea_programa());
                               System.exit(1);
                           }
                       }
                       break;
            default : //; //Presencia de cadena nula.
                      i1_tipo.set_string("void");
        }
        
        //System.out.println("En I1, el tipo j_tipo : "+j_tipo.get_string());
        //System.out.println("En I1, se sintetitiza el tipo i1_tipo : "+i1_tipo.get_string());
    }
    
    private void H1 (TablaSimbolos ts, Strings i_tipo, Strings h1_tipo){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        switch(token.get_lexema()){
            case "+" :
            case "-" : this.preanalisis++;
                       I(ts, i_tipo);
                       H1(ts, i_tipo, h1_tipo);
                       
                       //--- Esquema de traduccion ---
                       if(h1_tipo.get_string().equalsIgnoreCase("void")){
                           if(i_tipo.get_string().equalsIgnoreCase("integer"))
                               h1_tipo.set_string("integer");
                           else{
                               System.out.println("\nError Semantico : *** Tipos incompatibles, el operador "+token.get_lexema()+" requiere operandos de tipo integer. Se encontro un operando de tipo "+i_tipo.get_string()+" *** Linea "+token.get_linea_programa());
                               System.exit(1);
                           }
                       }
                       
                       break;
            default : //; //Presencia de cadena nula.
                      h1_tipo.set_string("void");
        }
        
        //System.out.println("En H1, el tipo i_tipo : "+i_tipo.get_string());
        //System.out.println("En H1, se sintetitiza el tipo h1_tipo : "+h1_tipo.get_string());
    }
    
    private void G1 (TablaSimbolos ts, Strings h_tipo, Strings g1_tipo){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        switch(token.get_lexema()){
            case ">"  :
            case "<"  :
            case ">=" :
            case "<=" :
            case "="  :
            case "<>" : this.preanalisis++;
                        H(ts, h_tipo);
                        G1(ts, h_tipo, g1_tipo);
                        
                        //--- Esquema de traduccion ---
                        if(g1_tipo.get_string().equalsIgnoreCase("void")){
                            if(h_tipo.get_string().equalsIgnoreCase("integer"))
                                g1_tipo.set_string("integer");
                            else{
                                System.out.println("\nError Semantico : *** Tipos incompatibles, el operador relacional "+token.get_lexema()+" requiere operandos de tipo integer. Se encontro un operando de tipo "+h_tipo.get_string()+" *** Linea "+token.get_linea_programa());
                                System.exit(1);
                            }
                        }else
                            ;//Que pasa si g1_tipo no es void???. Esto no es posible porque solamente la regla G1 genera exp relacionales.
                        break;
            default : //; //Presencia de cadena nula.
                      g1_tipo.set_string("void");
        }
        
        System.out.println("En G1, el tipo h_tipo : "+h_tipo.get_string());
        System.out.println("En G1, se sintetitiza el tipo g1_tipo : "+g1_tipo.get_string());
    }
    
    private void T1 (TablaSimbolos ts, Strings f_tipo, Strings t1_tipo){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase("and")){
            this.preanalisis++;
            F(ts, f_tipo);
            T1(ts, f_tipo, t1_tipo);
            
            //--- Esquema de traduccion ---
            if(t1_tipo.get_string().equalsIgnoreCase("void")){
                if(f_tipo.get_string().equalsIgnoreCase("boolean"))
                    t1_tipo.set_string("boolean");
                else{
                    System.out.println("\nError Semantico : *** Tipos incompatibles, el operador \"and\" requiere operandos de tipo boolean. Se encontro un operando de tipo "+f_tipo.get_string()+" *** Linea "+token.get_linea_programa());
                    System.exit(1);
                }
            }
        }else//; //Presencia de cadena nula.
            t1_tipo.set_string("void");
        
        //System.out.println("En T1, el tipo f_tipo : "+f_tipo.get_string());
        //System.out.println("En T1, se sintetitiza el tipo t1_tipo : "+t1_tipo.get_string());
    }
    
    private void E1 (TablaSimbolos ts, Strings e1_tipo){
        Strings t_tipo=new Strings("");
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase("or")){
            this.preanalisis++;
            T(ts, t_tipo);
            E1(ts, e1_tipo);
            
            //--- Esquema de traduccion ---
            if(e1_tipo.get_string().equalsIgnoreCase("void")){
                if(t_tipo.get_string().equalsIgnoreCase("boolean"))
                    e1_tipo.set_string("boolean");
                else{
                    System.out.println("\nError Semantico : *** Tipos incompatibles, el operador \"or\" requiere operandos de tipo boolean. Se encontro un operando de tipo "+e1_tipo.get_string()+" *** Linea");
                    System.exit(1);
                }
            }
            
        }else{
            //; //Presencia de cadena nula.
            e1_tipo.set_string("void");
        }
        
        //System.out.println("En E1, el tipo t_tipo : "+t_tipo.get_string());
        //System.out.println("En E1, se sintetitiza el tipo e1_tipo : "+e1_tipo.get_string());
    }
    
    private void K (TablaSimbolos ts, Strings k_tipo){
        Simbolo simbolo=null;
        Token id=null;
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("(")){
            
            Strings tipo_syn=new Strings("");
            this.preanalisis++;
            expresion(ts, tipo_syn);
            //  Sintetizamos el tipo de expresion.
            k_tipo.set_string(tipo_syn.get_string());
            //System.out.println("\n contenido de k_tipo en exp "+k_tipo.get_string());
            match(")");
        }else{
            if(digito(token)){
                this.preanalisis++;
                k_tipo.set_string("integer");
            }else{
                
                switch(token.get_lexema()){
                
                    case "false"   :
                    case "true"    : this.preanalisis++;
                                     k_tipo.set_string("boolean");
                                     break;
                                     
                    case "maxint"  : this.preanalisis++;
                                     k_tipo.set_string("integer");
                                     break;
                                     
                    case "succ"    : this.preanalisis++;
                                     this.inicio_exp=token;
                                     Strings succ_tipo=new Strings("");
                                     
                                     succ(ts,succ_tipo);
                                     k_tipo.set_string(succ_tipo.get_string());
                                     
                                     break;
                                     
                    case "pred"    : this.preanalisis++;
                                     this.inicio_exp=token;
                                     Strings pred_tipo=new Strings("");
                                     
                                     pred(ts,pred_tipo);
                                     k_tipo.set_string(pred_tipo.get_string());
                                     
                                     break;
                    
                    default: 
                                if(identificador(token)){
                                    //id mantiene una copia del token que en principio se corresponde con un identificador.
                                    id=token;
                                    simbolo=this.obtener_valor(ts, token);
                                    //  Verificamos si el identificador se encuentra definido em la TS.
                                    if(simbolo == null){
                                        System.out.println("\nError Semantico : *** El identificador \""+id.get_lexema()+"\" no se encuentra definido *** Linea "+id.get_linea_programa());
                                        System.exit(1);
                                    }

                                    this.preanalisis++;
                                    token=this.tokens_sintacticos.get(this.preanalisis);

                                    switch(token.get_lexema()){
                                        //--- Llamada a Subprograma ---
                                        case "("   : //   Verificamos si el identificador se encuentra definido como Funcion o Procedimiento.     
                                                     if(!((simbolo instanceof Funcion) || (simbolo instanceof Procedimiento))){
                                                        System.out.println("\nError Semantico : *** El identificador \""+id.get_lexema()+"\" debe estar definido como FUNCION o PROCEDIMIENTO *** Linea "+id.get_linea_programa());
                                                        System.exit(1);
                                                     }

                                                     //   Estructura para guardar el tipo de los argumentos.
                                                     ArrayList<Parametro> argumentos=new ArrayList();
                                                     this.preanalisis++;
                                                     args(ts, argumentos);

                                                     //   Verificamos cantidad y tipos de parametros para una Funcion o Procedimiento.
                                                     if(simbolo instanceof Funcion)
                                                         k_tipo.set_string(((Funcion) simbolo).chequeo_de_tipos(id, argumentos));
                                                     else
                                                         k_tipo.set_string(((Procedimiento) simbolo).chequeo_de_tipos(id, argumentos));

                                                     match(")");

                                                     token=this.tokens_sintacticos.get(this.preanalisis);
                                                     //   Verificamos si un procedimiento no esta incluido en una expresion.
                                                     if(!token.get_lexema().equalsIgnoreCase(";") && (simbolo instanceof Procedimiento)){
                                                         System.out.println("\nError Semantico : *** No se puede utilizar un Procedimiento como operando en una expresion aritmetica, relacional o booleana *** linea "+id.get_linea_programa());
                                                         System.exit(1);
                                                     }
                                                     break;
                                        case "." : //--- Acceso a registro ---
                                                   Simbolo td_r=((Variable)simbolo).get_tipo_dato();
                                                   
                                                   //Significa que estamos en presencia de un tipo definido por el usuario.
                                                   if(td_r instanceof TipoDato){
                                                       String id_r=((TipoDato)td_r).get_nombre_tipo();
                                                       td_r=this.obtener_valor(ts, new Token(id_r));
                                                   }
                                                   
                                                   //   Verificamos si el identificador se encuentra definido como Regtistro.
                                                   if(!(td_r instanceof Registro)){
                                                       System.out.println("\nError Semantico : *** El identificador \""+id.get_lexema()+"\" debe estar definido como REGISTRO *** Linea "+id.get_linea_programa());
                                                       System.exit(1);
                                                   }

                                                   this.preanalisis++;
                                                   token=this.tokens_sintacticos.get(this.preanalisis);
                                                   if(identificador(token))
                                                       this.preanalisis++;

                                                   k_tipo.set_string(((Registro)td_r).chequeo_de_tipos(id, token));

                                                   break;
                                        case "[" : //--- Acceso a arreglo ---
                                                   Simbolo td=((Variable)simbolo).get_tipo_dato();
                                                   
                                                   //Significa que estamos en presencia de un tipo definido por el usuario.
                                                   if(td instanceof TipoDato){
                                                       String id_a=((TipoDato)td).get_nombre_tipo();
                                                       td=this.obtener_valor(ts, new Token(id_a));
                                                   }
                                                   
                                                   //   Verificamos si el identificador se encuentra definido como Arreglo.
                                                   if(!(td instanceof Arreglo)){
                                                       System.out.println("\nError Semantico : *** El identificador \""+id.get_lexema()+"\" debe estar definido como ARREGLO *** Linea "+id.get_linea_programa());
                                                       System.exit(1);
                                                   }

                                                   this.preanalisis++;
//                                                   token=this.tokens_sintacticos.get(this.preanalisis);
//
//                                                   int indice=0;
//                                                   if(digito(token)){
//                                                       this.preanalisis++;
//                                                       indice=Integer.parseInt(token.get_lexema());
//                                                   }else{
//                                                       if(identificador(token)){
//                                                           this.preanalisis++;
//
//                                                           Simbolo s=this.obtener_valor(ts, token);
//
//                                                           //--- Esquema de traduccion ---
//                                                           if(s == null){
//                                                               System.out.println("\nError Semantico : *** El identificador \""+token.get_lexema()+"\" no se encuentra definido *** Linea "+token.get_linea_programa());
//                                                               System.exit(1);
//                                                           }else{
//                                                               if(s instanceof Variable){
//                                                                   TipoDato tipo=(TipoDato) (((Variable)s).get_tipo_dato());
//                                                                   if(!tipo.get_nombre_tipo().equalsIgnoreCase("integer")){
//                                                                       System.out.println("\nError Semantico : *** Tipos incompatibles, el identificador \""+token.get_lexema()+"\" debe ser de tipo integer *** Linea "+token.get_linea_programa());
//                                                                       System.exit(1);
//                                                                   }else{
//                                                                       String dato=((Variable)s).get_dato();
//                                                                       if(dato.equalsIgnoreCase("")){
//                                                                           System.out.println("\nError Semantico : *** El identificador \""+token.get_lexema()+"\" no ha sido inicializado *** Linea "+token.get_linea_programa());
//                                                                           System.exit(1);
//                                                                       }else
//                                                                           indice=Integer.parseInt(dato);
//                                                                   }
//                                                               }else{
//                                                                    System.out.println("\nError Semantico : *** El indice \""+token.get_lexema()+"\" del arreglo \""+id.get_lexema()+"\" debe estar definido como una VARIABLE ENTERA *** Linea "+token.get_linea_programa());
//                                                                    System.exit(1);
//                                                               }
//                                                           }
//                                                       }
//                                                   }

                                                   Strings tipo_syn=new Strings("");
                                                   expresion(ts, tipo_syn);
                                                   
                                                   if(!(tipo_syn.get_string().equalsIgnoreCase("integer"))){
                                                       System.out.println("\nError Semantico : *** Tipos incompatibles, el indice de un arreglo debe ser una variable o expresion de tipo integer *** Linea "+id.get_linea_programa());
                                                       System.exit(1);
                                                   }
                                                   
                                                   match("]");

                                                   k_tipo.set_string(((Arreglo)td).chequeo_de_tipos(id));

                                                   break;
                                        default : //; //Solamente tenemos un identificador.
                                                  if(simbolo instanceof Variable){
                                                      TipoDato tipo=(TipoDato)((Variable)simbolo).get_tipo_dato();
                                                      k_tipo.set_string(tipo.get_nombre_tipo());
                                                      //System.out.println("\nEste es el tipo que sintetizamos : "+k_tipo.get_string()+" Token : "+id.get_lexema());
                                                  }else{
                                                      if(simbolo instanceof Constante){
                                                          k_tipo.set_string(((Constante)simbolo).get_tipo());
                                                      }else{
                                                          //Significa que se hace referencia a un parametro formal.
                                                          if(simbolo instanceof TipoDato){
                                                              k_tipo.set_string(((TipoDato)simbolo).get_nombre_tipo());
                                                          }else
                                                              if(simbolo instanceof Procedimiento){                                                         
                                                                  System.out.println("\nError Semantico : *** No se puede utilizar un Procedimiento como operando en una expresion aritmetica, relacional o booleana *** linea "+id.get_linea_programa());
                                                                  System.exit(1);
                                                              }
                                                      }
                                                  }
                                    }

                
                                }
                }//Fin del switch.
            }
        }
    }
    
    /*
    * Esta funcion recorre la cadena estatica en busca del valor asociado a un identificador. 
    * @token: contiene un identificador que representa la clave del hashmap que implementa la TS.
    */
    private Simbolo obtener_valor (TablaSimbolos ts, Token token){
        Simbolo valor=null;
        boolean fin=false;
        
        while(ts!=null && !fin){
            valor=ts.get(token.get_lexema());
            if(valor!=null)
                fin=true;
            else{
                //Buscamos el identificador en la lista de parametros formales del subprograma propietario de la TS LOCAL.
                valor=ts.obtener_parametro_formal(token);
                if(valor != null)
                    fin=true;
                else//Si no encontramos el identificador en la lista de parametros formales continuamos buscando en la cadena estatica.
                    ts=ts.get_ts_superior();
            }
        }
        
        return valor;
    }
    
}
