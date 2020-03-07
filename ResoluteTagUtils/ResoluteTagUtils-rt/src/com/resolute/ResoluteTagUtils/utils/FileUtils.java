package com.resolute.ResoluteTagUtils.utils;

import javax.baja.file.BDirectory;
import javax.baja.file.BFileSystem;
import javax.baja.file.BIFile;
import javax.baja.file.FilePath;
import javax.baja.naming.BOrd;
import javax.baja.naming.OrdQuery;
import javax.baja.sys.Sys;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class FileUtils {

    /***
     *
     * @param ord - BOrd
     * @return      boolean - true if the file exists in the station.
     */
    public static boolean fileExists(BOrd ord){
        try{
            BIFile file = (BIFile) ord.get(Sys.getStation());
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /***
     * Returns a baja file from an ord.
     * @param ord - BOrd
     * @return      BIFile
     */
    public static BIFile getFile(BOrd ord){
        return (BIFile) ord.get(Sys.getStation());
    }

    /***
     *
     * @param ord - BOrd
     * @return      BDirectory
     * @throws IOException
     */
    public static BDirectory mDir(BOrd ord) throws IOException {
        OrdQuery[] queries = ord.parse();
        FilePath path = (FilePath) queries[queries.length - 1];
        return BFileSystem.INSTANCE.makeDir(path);
    }

    /***
     *
     * @param ord - BOrd
     * @return      BIFile
     * @throws IOException
     */
    public static BIFile mkFile(BOrd ord) throws IOException {
        OrdQuery[] queries = ord.parse();
        FilePath path = (FilePath) queries[queries.length - 1];
        return BFileSystem.INSTANCE.makeFile(path);
    }

    /***
     * Writes content to a Niagara station file, can't append, only writes from file start,
     * so this operation doesn't append, only overwrites the file.
     * @param content - String
     * @param ord     - BOrd
     * @throws IOException
     */
    public static void write2File(String content, BOrd ord)
                                    throws IOException {
        if(fileExists(ord)){
            getFile(ord).write(content.getBytes());
        }
    }

    /***
     * Writes content to a file in the shared directory of the user home directory, it can
     * append or overwrite.
     * @param content - String
     * @param relPath - String
     * @param append  - Boolean
     * @throws IOException
     */
    public static void write2Shared(String content, String relPath, boolean append)
                                    throws IOException {
        if(!content.isEmpty()){
            String[] pathParts = relPath.split("/");
            AtomicReference<Path> path =
                    new AtomicReference<>(Paths
                                    .get(Sys.getNiagaraSharedUserHome()
                                    .getAbsolutePath()));
            Arrays.stream(pathParts).forEach(s -> {
               path.set(Paths.get(path.get().toString(), s));
            });

            File f = new File(path.toString());
            FileWriter fw = new FileWriter(f, append);
            fw.write(content);
            fw.close();
        }
    }


}
