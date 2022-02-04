import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Prueba extends SimpleFileVisitor<Path> {


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String name = file.toAbsolutePath().toString();

        FileReader fi = null;
        Set<String> Stop = new HashSet<String>(Arrays.asList("stop-word-list.txt"));
        Set<String> unicas = new HashSet<String>();
        Set<String> duplicadas = new HashSet<String>();

        int u=0;
        int i=0;
        int r=0;
        int t=0;

        try {
            fi = new FileReader(name);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }


        BufferedReader in = new BufferedReader(new FileReader(name));


        String linea = null;

        if (name.toLowerCase().endsWith(".txt")) {



            String delimiters = "\\s+|,\\s*|\\.\\s*|\\;\\s*|\\:\\s*|\\!\\s*|\\¡\\s*|\\¿\\s*|\\?\\s*|\\-\\s*"
                    + "|\\[\\s*|\\]\\s*|\\(\\s*|\\)\\s*|\\\"\\s*|\\_\\s*|\\%\\s*|\\+\\s*|\\/\\s*|\\#\\s*|\\$\\s*";


            // Lista con todas las palabras diferentes
            ArrayList<String> list = new ArrayList<String>();

            // Tiempo inicial
            long startTime = System.currentTimeMillis();

            try {
                while ((linea = in.readLine()) != null) {



                    if (linea.trim().length() == 0) {
                        continue; // la linea esta vacia, continuar
                    }

                    // separar las palabras en cada linea

                    String words[] = linea.split(delimiters);


                    for (String theWord : words) {

                        if (!Stop.contains(theWord)) {
                            u++;
                            if (!unicas.add(theWord)){
                                duplicadas.add(theWord);
                                r++;}
                            unicas.removeAll(duplicadas);
                        }
                        i =u-r;
                        t=(i*100)/u;

                        unicas.removeAll(duplicadas);

                    }

                }



                System.out.printf("En el directorio %-50s \n", name);
                System.out.println("Palabras unicas:    " + unicas);
                System.out.println("Palabras vacias:    " + duplicadas);


                System.out.println("El numero de palabras es " + u+ ",  de palabras unicas es "+i+",  de palabras vacias es "+r+" " +
                        "y el porcentaje de palabras utiles es "+ t+"%");

            } catch(IOException ex){
                System.out.println(ex.getMessage());

            }
        }
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.printf("No se puede procesar:%30s%n", file.toString()) ;
        return super.visitFileFailed(file, exc);
    }

    public static void main(String[] args)
            throws IOException {

        // /Users/rnavarro/datos
        if (args.length < 1) {
            System.exit(2);
        }

        // iniciar en este directorio
        Path startingDir = Paths.get(args[0]);

        // clase para procesar los archivos
        Prueba contadorLineas = new Prueba();

        // iniciar el recorrido de los archivos
        Files.walkFileTree(startingDir, contadorLineas);

    }}


