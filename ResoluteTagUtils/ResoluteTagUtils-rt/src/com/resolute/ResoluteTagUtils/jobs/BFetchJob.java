package com.resolute.ResoluteTagUtils.jobs;

import com.resolute.ResoluteTagUtils.components.BTagImporter;
import com.resolute.ResoluteTagUtils.services.BResoluteTagUtils;

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

        progress(0);
        BTagImporter tagImporter =
                ((BResoluteTagUtils)Sys.getService(BResoluteTagUtils.TYPE)).getTagImporter();

        tagImporter.setIsJobRunning(true);

        URL url = new URL("http://localhost:1880/ResoluteTagImport");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        int status = con.getResponseCode();

        logger.fine("HTTP Response Status: "+con.getResponseMessage());
        if(status == 200){
            int p = 25;
            progress(p);
            log().message("Connection found with HTTP Status ".concat(String.valueOf(status)));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
//            double addConstant = 50.0 / (double)in.lines().count();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
//                progress(p+=(int) addConstant);
            }
            in.close();
            logger.fine(content.toString());

            try{
                boolean importFileExists = false;
                BDirectory resoluteDir =
                        (BDirectory) BOrd.make("file:^ResoluteImports").get(Sys.getStation());
                double addK = 75 / (double)resoluteDir.listFiles().length;
                for(BIFile f : resoluteDir.listFiles()){
                    progress(p+=addK);
                    if(f.getFileName().equals("rbiTagImport.json")){
                        f.write("".getBytes());
                        f.write(content.toString().getBytes());
                        importFileExists = true;
                        progress(100);
                        break;
                    }
                }
                if(!importFileExists){
                    OrdQuery[] queries = tagImporter.getImportFile().parse();
                    FilePath fp = (FilePath)queries[queries.length-1];
                    BIFile file = BFileSystem.INSTANCE.makeFile(fp, null);
                    file.write(content.toString().getBytes());
                    progress(100);
                }
            }catch(UnresolvedException | IOException ue){
                log().message(ue.getMessage());
                ue.printStackTrace();
                tagImporter.setIsJobRunning(false);
            }
        }else{
            BufferedReader err = new BufferedReader(
                    new InputStreamReader(con.getErrorStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while((inputLine = err.readLine()) != null){
                content.append(inputLine);
            }
            logger.severe("http request failed with: ".concat(String.valueOf(status)));
            logger.severe(content.toString());
        }
        con.disconnect();
        tagImporter.setIsJobRunning(false);
    }


}
