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
                               System.out.println("\n\n Analisis lexico exitoso \n\n");
                               break;
                    case 's' : lexico=new Lexico(archivo);
                               lexico.analisis_lexico();
                               lexico.guardar_tokens();
                                   
//                               Sintactico sintactico=new Sintactico(lexico.get_tokens(), lexico.get_palabras_reservadas);
//                               sintactico.analisis_sintactico();
                               
                               System.out.println("\n\n Analisis sintactico exitoso \n\n");
                               break;
                    case 'c' : System.out.println("\nIniciando proceso de compilacion....\n\n");
                               lexico=new Lexico(archivo);
                               lexico.analisis_lexico();
                               lexico.guardar_tokens();
                                       
//                               Semantico semantico=new Semantico (lexico.get_tokens_sintacticos(),TS);
//                               semantico.analisis_semantico();
                                   
                               break;

                    default : System.out.println("\nOpcion incorrecta!. Ejecute java -jar pascal.jar -b para acceder al manual de usuario.\n");
                }
            }else
                System.out.println("Opcion incorrecta!. Ejecute java -jar pascal.jar -b para acceder al manual de usuario.\n");
        }else{
            //opcion=1, archivo=0
            if(!opcion.equalsIgnoreCase("") && archivo.equalsIgnoreCase("")){
                if(opcion.length()==2){
                    char c=(opcion.toLowerCase()).charAt(1);
                    switch(c){
                        case 'v' : System.out.println("\n\nVersion 1.1");
                                   break;
                        case 'a' : System.out.println("\nAutor: Bruno Guala");
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
        System.out.println("\t pascal - compilador para un subconjunto de construcciones Pascal");
        System.out.println("\n\nSINOPSIS\n");
        System.out.println("\t pascal [OPCION] [ARCHIVO]");
        System.out.println("\n\nDESCRIPCION\n");
        System.out.println("\t Esta es mi descripcion");
        System.out.println("\n\n\t -l\n");
        System.out.println("\t analisis lexico, los tokens generados se guardan en el archivo tokens.txt.");
        System.out.println("\n\n\t -s\n");
        System.out.println("\t analisis sintactico.");
        System.out.println("\n\n\t -c\n");
        System.out.println("\t proceso de compilacion completo, incluye fase de analisis semantico.");
        System.out.println("\n\n\t -v\n");
        System.out.println("\t version del compilador");
        System.out.println("\n\n\t -a\n");
        System.out.println("\t Autor");
    }
}
