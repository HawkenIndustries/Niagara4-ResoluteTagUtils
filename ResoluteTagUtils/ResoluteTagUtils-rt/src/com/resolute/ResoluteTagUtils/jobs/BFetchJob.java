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

        Reader streamReader = null;
        BTagImporter tagImporter =
                ((BResoluteTagUtils)Sys.getService(BResoluteTagUtils.TYPE)).getTagImporter();

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
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            logger.fine(content.toString());

            try{
                boolean importFileExists = false;
                BDirectory resoluteDir =
                        (BDirectory) BOrd.make("file:^ResoluteImports").get(Sys.getStation());
                for(BIFile f : resoluteDir.listFiles()){
                    if(f.getFileName().equals("rbiTagImport.json")){
                        f.write("".getBytes());
                        f.write(content.toString().getBytes());
                        importFileExists = true;
                        break;
                    }
                }
                if(!importFileExists){
                    OrdQuery[] queries = tagImporter.getImportFile().parse();
                    FilePath fp = (FilePath)queries[queries.length-1];
                    BIFile file = BFileSystem.INSTANCE.makeFile(fp, null);
                    file.write(content.toString().getBytes());
                }
            }catch(UnresolvedException ue){
                log().message(ue.getMessage());
                ue.printStackTrace();
            }catch(IOException ioe){
                log().message(ioe.getMessage());
                ioe.printStackTrace();
            }

        }else{
            BufferedReader err = new BufferedReader(
                    new InputStreamReader(con.getErrorStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while((inputLine = err.readLine()) != null){
                content.append(inputLine);
            }
            logger.severe("http request failed with: "+String.valueOf(status));
            logger.severe(content.toString());
        }
        con.disconnect();
    }


}
