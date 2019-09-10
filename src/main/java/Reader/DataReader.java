package Reader;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Reads and writes Files
 * @author Timon Kayser
 */
public class DataReader {

    /**
     * Name of the File
     */
    private String name;
    /**
     * File path as URL of resource file potentially inside a jar
     */
    private final URL path;
    /**
     * File path of the File if inside a jar
     */
    private Path pathOutside = null;
    /**
     * Determines if we should use the outside or inside File
     */
    private boolean doOutside;

    /**
     * File contents
     */
    private ArrayList<String> inputByLine;

    public DataReader(String Name) {
        name = Name;
        path = this.getClass().getClassLoader().getResource(Name);
        inputByLine = new ArrayList<>();
        URL pathName = getClass().getProtectionDomain().getCodeSource().getLocation();

        try {
            pathOutside = Paths.get(pathName.toURI());
            pathOutside = pathOutside.getParent().resolve(name);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        doOutside = Files.exists(pathOutside);

    }

    /**
     * @return return File contents of file
     */
    private ArrayList<String> read() {
        try {
            Scanner reader = null;
            //This should always retain some info
            if (!doOutside) reader = new Scanner(new BufferedReader(new InputStreamReader(path.openStream())));
            //This is the file created if this is run from inside a jar
            if (doOutside) reader = new Scanner(new BufferedReader(new FileReader(pathOutside.toFile())));

            inputByLine.clear();
            while (reader.hasNext()) {
                inputByLine.add(reader.next());
            }
            return inputByLine;
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param inputByLine Strings to write to file
     * @return Return if we wrote successfully
     */
    private boolean write(ArrayList<String> inputByLine) {
        Path pathOutside = this.pathOutside;

        if (!doOutside) {
            try {
                pathOutside = Paths.get(path.toURI());
            } catch (Exception e) {
                //File probably inside jar as ressource
                System.err.println("URL leading inside jar?");
                System.err.println("Copying file to outside and trying to write again");
                //now we should be able to read
                doOutside = true;
                //and write
                pathOutside = this.pathOutside;
            }
        }

        try {
            //Try to write
            Files.write(pathOutside, inputByLine);
            //Verify if we wrote correctly (might be broken)
            return verify(getContent(), inputByLine);

        } catch (NoSuchFileException e) {
            try {
                //Try to create file
                Files.createFile(pathOutside);
            } catch (IOException ex) {
                ex.addSuppressed(e);
                ex.printStackTrace();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param v1 Strings to compare to each other
     * @param v2 Strings to compare to each other
     * @return Return true if v1 and v2 are the same
     */
    private boolean verify(ArrayList<String> v1, ArrayList<String> v2) {
        if (v1.size() != v2.size()) {
            System.err.println("Something went wrong");
            return false;
        } else {
            for (int i = 0; i < v2.size(); i++) {
                if (!v1.get(i).equals(v2.get(i))) return false;
            }
        }
        return true;
    }

    /**
     * Wrapper for {@link #read()} function
     *
     * @return return File contents of file
     */
    public ArrayList<String> getContent() {
        return read();
    }

    /**
     * Wrapper for {@link #write(ArrayList)} function
     *
     * @param inputByLine Strings to write to file
     * @return Return if we wrote successfully
     */
    public boolean setContent(ArrayList<String> inputByLine) {
        return write(inputByLine);
    }

}
