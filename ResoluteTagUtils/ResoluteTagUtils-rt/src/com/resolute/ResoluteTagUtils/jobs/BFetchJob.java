package com.resolute.ResoluteTagUtils.jobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.resolute.ResoluteTagUtils.components.BTagImporter;
import com.resolute.ResoluteTagUtils.services.BResoluteTagUtils;
import com.resolute.ResoluteTagUtils.utils.HttpClientConnection;
import com.resolute.ResoluteTagUtils.utils.HttpResponse;

import javax.baja.file.*;
import javax.baja.job.BSimpleJob;
import javax.baja.naming.BOrd;
import javax.baja.naming.OrdQuery;
import javax.baja.naming.UnresolvedException;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.logging.Logger;

@NiagaraType

public class BFetchJob extends BSimpleJob {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.jobs.BFetchJob(2979906276)1.0$ @*/
/* Generated Mon Oct 21 08:31:50 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BFetchJob.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private static Logger logger = Logger.getLogger("Resolute Tag Utils");

    @Override
    public void run(Context cx) throws  Exception {

        BTagImporter tagImporter =
                ((BResoluteTagUtils)Sys.getService(BResoluteTagUtils.TYPE)).getTagImporter();

        URL updateUrl = new URL("http://localhost:1880/Resolute/tagImport/fetch");
        URL versionUrl = new URL("http://localhost:1880/Resolute/tagImport/version");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        try{
            HttpClientConnection versionConn = HttpClientConnection.builder()
                    .withHttpMethod("GET")
                    .withHeaders(headers)
                    .withUrlTarget(versionUrl)
                    .build();
            HttpResponse response = versionConn.doGet();

            logger.info("Version HTTP call:");
            logger.info("HTTP Status: ".concat(String.valueOf(
                    response.getStatus())));
            logger.info("HTTP Msg: ".concat(String.valueOf(response.getMsg())));

            checkCachedVersion(response.getMsg(), tagImporter, updateUrl);

        }catch(Exception e){
            logger.severe(e.getMessage());
            e.printStackTrace();
            fetchFromServer(tagImporter, updateUrl);
        }



    }

    public void fetchFromServer(BTagImporter tagImporter, URL serverUrl){
        HttpURLConnection con;
        try{
            con = (HttpURLConnection) serverUrl.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();
            logger.fine("HTTP Response Status: "+con.getResponseMessage());

            if(status == 200){
                log().message("Connection found with HTTP Status ".concat(String.valueOf(status)));
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                String c = content.toString();
                writeToSharedFile(c);
                writeToStationFile(c, tagImporter);
                logger.fine(content.toString());

            }else{
                BufferedReader err = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while((inputLine = err.readLine()) != null){
                    content.append(inputLine);
                }
                err.close();
                logger.severe("http request failed with: ".concat(String.valueOf(status)));
                logger.severe(content.toString());
            }
            con.disconnect();
        }catch(Exception e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            try{
                File f = new File(Sys.getNiagaraSharedUserHome(), "rbiTagImport.json");

                StringBuilder content;
                try (BufferedReader in = new BufferedReader(new FileReader(f))) {
                    content = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                }
                writeToStationFile(content.toString(), tagImporter);
            }catch(FileNotFoundException fnfe){
                logger.severe("File not found in ".concat(Sys.getNiagaraSharedUserHome().getAbsolutePath()));
                logger.severe(fnfe.getMessage());
                fnfe.printStackTrace();
            }catch(Exception e1){
                logger.severe(e1.getMessage());
                e1.printStackTrace();
            }
        }
    }

    private void fetchFromCache(BTagImporter tagImporter){
        try{
            File f = new File(Sys.getNiagaraSharedUserHome(), "rbiTagImport.json");

            StringBuilder content;
            try (BufferedReader in = new BufferedReader(new FileReader(f))) {
                content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            writeToStationFile(content.toString(), tagImporter);
        }catch(FileNotFoundException fnfe){
            logger.severe("File not found in ".concat(Sys.getNiagaraSharedUserHome().getAbsolutePath()));
            logger.severe(fnfe.getMessage());
            fnfe.printStackTrace();
        }catch(Exception e1){
            logger.severe(e1.getMessage());
            e1.printStackTrace();
        }
    }

    private void checkCachedVersion(String jsonVersion, BTagImporter tagImporter, URL serverUrl){
        AccessController.doPrivileged((PrivilegedAction <Void>)() -> {
            try{
                Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                JsonObject fileContent = gson.fromJson(
                        new FileReader(
                                Paths.get(Sys.getNiagaraSharedUserHome().getAbsolutePath(),
                                        "rbiTagImport.json").toString()),
                        JsonObject.class);
                JsonObject version = gson.fromJson(jsonVersion, JsonObject.class);
                double latestVersion = Double.parseDouble(version.get("version").getAsString());
                double cacheVersion = Double.parseDouble(fileContent.get("version").getAsString());
                final String msg = "Cached Version: ".concat(String.valueOf(cacheVersion)) +
                                   "\nLatest Version: ".concat(String.valueOf(latestVersion));
                if(latestVersion == cacheVersion){
                    logger.info("EQUAL VERSIONS...");
                    logger.info(msg);
                    fetchFromCache(tagImporter);

                }else if(latestVersion > cacheVersion){
                    logger.info("CACHE NEEDS UPDATE...");
                    logger.info(msg);
                    fetchFromServer(tagImporter, serverUrl);

                }else{
                    logger.info("ERROR - CACHE VERSION HIGHER THAN SERVER...");
                    logger.info(msg);
                    fetchFromServer(tagImporter, serverUrl);
                }
            }catch(Exception e){
                logger.severe(e.getMessage());
                e.printStackTrace();
                fetchFromServer(tagImporter, serverUrl);
            }
            return null;
        });
    }

    private void writeToSharedFile(String content){
        try{
            if(!content.isEmpty()){
                File f = new File(Sys.getNiagaraSharedUserHome(), "rbiTagImport.json");
                FileWriter fw = new FileWriter(f);
                fw.write(content);
                logger.info("writing to cache...");
                fw.close();
            }else{
                logger.warning(
                        "fetchJob.writeToSharedFile() just tried to write empty to the cache file...!!!");
            }
        }catch(FileNotFoundException fnfe){
            logger.severe("File not found in "
                    .concat(Paths
                            .get(Sys.getNiagaraSharedUserHome()
                                    .getAbsolutePath(), "rbiTagImport.json").toString()));
            logger.severe(fnfe.getMessage());
            fnfe.printStackTrace();
        }catch(Exception e1){
            logger.severe(e1.getMessage());
            e1.printStackTrace();
        }
    }

    private void writeToStationFile(String content, BTagImporter tagImporter){
        try{
            boolean importFileExists = false;
            BDirectory resoluteDir =
                    (BDirectory) BOrd.make("file:^ResoluteImports").get(Sys.getStation());
            for(BIFile f : resoluteDir.listFiles()){
                if(f.getFileName().equals("rbiTagImport.json")){
                    f.write("".getBytes());
                    f.write(content.getBytes());
                    importFileExists = true;
                    progress(100);
                    break;
                }
            }
            if(!importFileExists){
                OrdQuery[] queries = tagImporter.getImportFile().parse();
                FilePath fp = (FilePath)queries[queries.length-1];
                BIFile file = BFileSystem.INSTANCE.makeFile(fp, null);
                file.write(content.getBytes());
                progress(100);
            }
        }catch(UnresolvedException | IOException ue){
            log().message(ue.getMessage());
            ue.printStackTrace();
        }
    }
}
