/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pascal;

/**
 *
 * @author Bruno
 */
public class Pascal {
    
    public static void main (String [] args){
        int longitud=args.length;
        String opcion="";
        String archivo="";
        
        switch(longitud){
            
            case 0 : //opcion=0, archivo=0
                     System.out.println("\nDebe especificar al menos una opcion!. Ejecute java -jar pascal.jar -b para acceder al manual de usuario.\n");
                     break;
            case 1 : opcion=args[0];
                     break;
            case 2 : opcion=args[0];
                     archivo=args[1];
                     break;
        }     
        
        //opcion=1, archivo=1
        if(!opcion.equalsIgnoreCase("") && !archivo.equalsIgnoreCase("")){
            Lexico lexico;
            if(opcion.length()==2){
                char accion=(opcion.toLowerCase()).charAt(1);
                switch(accion){
                    case 'l' : lexico=new Lexico(archivo);
                               lexico.analisis_lexico();
                               lexico.guardar_tokens();
                               System.out.println("\n *** Analisis lexico exitoso *** ");
                               break;
                    case 's' : lexico=new Lexico(archivo);
                               lexico.analisis_lexico();
                               lexico.guardar_tokens();
                                   
                               Sintactico sintactico=new Sintactico(lexico.get_tokens_sintacticos(), lexico.get_palabras_reservadas(), archivo);
                               sintactico.analisis_sintactico();
                               
                               sintactico.guardar_ts();
                               
                               System.out.println("\n *** Analisis sintactico exitoso *** ");
                               break;
                    case 'c' : 
                               lexico=new Lexico(archivo);
                               lexico.analisis_lexico();
                               lexico.guardar_tokens();
                                       
                               Semantico semantico=new Semantico (lexico.get_tokens_sintacticos(),lexico.get_palabras_reservadas(), archivo);
                               semantico.analisis_semantico();
                                   
                               System.out.println("\n *** Analisis semantico exitoso *** ");
                               
                               break;

                    default : System.out.println("\nOpcion incorrecta!. Ejecute java -jar pascal.jar -b para acceder al manual de usuario.\n");
                }
            }else
                System.out.println("\nOpcion incorrecta!. Ejecute java -jar pascal.jar -b para acceder al manual de usuario.\n");
        }else{
            //opcion=1, archivo=0
            if(!opcion.equalsIgnoreCase("") && archivo.equalsIgnoreCase("")){
                if(opcion.length()==2){
                    char c=(opcion.toLowerCase()).charAt(1);
                    switch(c){
                        case 'v' : System.out.println("\n\nVersion 1.1");
                                   break;
                        case 'a' : System.out.println("\nAutor: Bruno Guala, Facultad de Informatica --- Universidad Nacional del Comahue");
                                   break;
                        case 'b' : manual();
                                   break;
                        default : System.out.println("Opcion incorrecta!. Ejecute java -jar pascal.jar -b para acceder al manual de usuario.\n");
                    }
                }else
                    System.out.println("Opcion incorrecta!. Ejecute java -jar pascal.jar -b para acceder al manual de usuario.\n");
            }
        }
        
        
    }
    
    private static void manual (){
        System.out.println("\nNOMBRE \n");
        System.out.println("\t pascal.jar - compilador para un subconjunto de construcciones del \n\t lenguaje Pascal");
        System.out.println("\n\nSINOPSIS\n");
        System.out.println("\t java -jar pascal.jar [OPCION] [ARCHIVO]");
        System.out.println("\n\nDESCRIPCION\n");
        System.out.println("\t pascal.jar implementa el front-end de un compilador para un \n\t"
                           + " subconjunto de construcciones del lenguaje Pascal. El front-end \n\t"
                           + " se corresponde con la primer etapa de un proceso de compilación \n\t"
                           + " y comprende cuatro fases: analisis lexico, analisis sintactico, \n\t"
                           + " analisis semantico y generacion de codigo intermedio.");
        System.out.println("\n\n\t -l\n");
        System.out.println("\t analisis lexico, los tokens generados se guardan en el archivo \n\t tokens_sintacticos_ARCHIVO.txt.");
        System.out.println("\n\n\t -s\n");
        System.out.println("\t analisis sintactico. Produce dos archivos de texto, los tokens \n\t"
                           + " generados por el analizador lexico y las tablas de simbolos \n\t"
                           + " parciales propias de cada ambiente. Este ultimo archivo se crea \n\t"
                           + " para comprobar que la cadena estatica del programa fuente se \n\t"
                           + " crea correctamente y que cada tabla de simbolos contiene el \n\t"
                           + " entorno local de cada subprograma.");
        System.out.println("\n\n\t -c\n");
        System.out.println("\t analisis semantico. Implementa un componente verificador de tipos. \n\t"
                           + " Produce dos archivos de texto, los tokens generados por el analizador\n\t"
                           + " lexico y las tablas de simbolos con la informacion definitiva de cada \n\t ambiente.");
        System.out.println("\n\n\t -g\n");
        System.out.println("\t proceso de compilacion completo, incluye fase de generacion de codigo \n\t"
                           + " intermedio. Genera un archivo con extensión mep.");
        System.out.println("\n\n\t -v\n");
        System.out.println("\t version del compilador.");
        System.out.println("\n\n\t -a\n");
        System.out.println("\t Autor.");
    }
}
