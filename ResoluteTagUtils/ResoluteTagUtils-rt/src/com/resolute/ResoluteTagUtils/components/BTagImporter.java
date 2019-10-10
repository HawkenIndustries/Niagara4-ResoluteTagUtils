package com.resolute.ResoluteTagUtils.components;

import com.resolute.ResoluteTagUtils.jobs.BTaggingJob;

import javax.baja.file.BFileSystem;
import javax.baja.file.BIDirectory;
import javax.baja.file.BIFile;
import javax.baja.file.FilePath;
import javax.baja.job.BJobService;
import javax.baja.naming.BOrd;
import javax.baja.naming.OrdQuery;
import javax.baja.naming.UnresolvedException;
import javax.baja.nre.annotations.Facet;
import javax.baja.nre.annotations.NiagaraAction;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;

import java.io.IOException;
import java.util.logging.Logger;

@NiagaraType

@NiagaraAction(
        name = "tagIt",
        flags = Flags.OPERATOR | Flags.SUMMARY
)

public class BTagImporter extends BComponent {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.components.BTagImporter(1334436307)1.0$ @*/
/* Generated Thu Oct 10 12:47:08 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Action "tagIt"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code tagIt} action.
   * @see #tagIt()
   */
  public static final Action tagIt = newAction(Flags.OPERATOR | Flags.SUMMARY, null);
  
  /**
   * Invoke the {@code tagIt} action.
   * @see #tagIt
   */
  public void tagIt() { invoke(tagIt, null, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BTagImporter.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    /***
     * Sample json import file
     * {
             "version": 1.0,
         "currentTagSet": [],
         "points": {
         "name": "BoolTestPoint0",
         "metricId": "f94937c7-bc4b-440e-aa85-9150ee615052./BoolTestPoint0",
         "tags": []
     }
     }***/

    private static Logger logger = Logger.getLogger("Resolute Tag Utils");

    @Override
    public void started(){
        logger.fine("TagImporter...started");

        /***
         * verify the resolute folder exists, if it doesn't try to create it
         */
        BOrd dirOrd = BOrd.make("\"file:^ResoluteImports\"");
        try{
            BIFile resDir = (BIFile)dirOrd.get(this);
            if(resDir.isDirectory()){
                try{
                    BIDirectory dir = (BIDirectory)resDir;
                    logger.finer("checking resolute directory for old tag import files...");
                    for(BIFile file : dir.listFiles()){
                        logger.finer(file.getFileName());
                        if(file.getFileName().equals("tagImport")){
                            BFileSystem.INSTANCE.delete(file.getFilePath());
                            logger.finer("found old tag import file @ " + file.getFilePath().toString());
                            logger.finer("deleting old resolute tag import file...");
                        }
                    }
                }catch(IOException ioe){
                    logger.severe(ioe.getMessage());
                    ioe.printStackTrace();
                }catch(Exception e){
                    logger.severe(e.getMessage());
                    e.printStackTrace();
                }
            }
        }catch(UnresolvedException ue){
            logger.warning("did not find resolute directory at start-up...!");
            logger.warning("creating resolute directory...");
            OrdQuery[] ordQuery = dirOrd.parse();
            FilePath fp = (FilePath)ordQuery[ordQuery.length-1];
            try{
                BFileSystem.INSTANCE.makeDir(fp);
            }catch(IOException ioe){}
        }
    }

    @Override
    public void atSteadyState(){ logger.fine("TagImporter...atSteadyState"); }

    public void doTagIt(Context cx){
        BJobService.getService().submit(new BTaggingJob(), cx);
    }

    public static BTagImporter make(){ return new BTagImporter(); }
}
