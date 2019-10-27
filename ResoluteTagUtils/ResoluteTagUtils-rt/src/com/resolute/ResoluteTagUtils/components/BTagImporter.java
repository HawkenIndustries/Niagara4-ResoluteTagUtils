package com.resolute.ResoluteTagUtils.components;

import com.resolute.ResoluteTagUtils.jobs.BBulkTagRemoveJob;
import com.resolute.ResoluteTagUtils.jobs.BFetchJob;
import com.resolute.ResoluteTagUtils.jobs.BTaggingJob;
import com.resolute.ResoluteTagUtils.utils.BJobInterlockWorker;

import javax.baja.file.BFileSystem;
import javax.baja.file.BIDirectory;
import javax.baja.file.BIFile;
import javax.baja.file.FilePath;
import javax.baja.job.BJobService;
import javax.baja.job.BSimpleJob;
import javax.baja.naming.BOrd;
import javax.baja.naming.OrdQuery;
import javax.baja.naming.UnresolvedException;
import javax.baja.nre.annotations.Facet;
import javax.baja.nre.annotations.NiagaraAction;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import javax.baja.util.IFuture;
import javax.baja.util.Invocation;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@NiagaraType

@NiagaraProperty(
        name = "tags",
        type = "baja:String",
        defaultValue = "BString.make(\"\")",
        facets = { @Facet(name = "BFacets.MULTI_LINE", value = "true") },
        flags = Flags.READONLY
)
@NiagaraProperty(
        name = "jsonVersion",
        type = "baja:Double",
        defaultValue = "BDouble.make(0.0)"
)

@NiagaraProperty(
        name = "importFile",
        type = "baja:Ord",
        defaultValue = "BOrd.make(\"file:^ResoluteImports/rbiTagImport.json\")",
        facets = {
                @Facet(name = "BFacets.TARGET_TYPE", value = "\"baja:IFile\"")
        },
        flags = Flags.SUMMARY
)

@NiagaraProperty(
        name = "deleteFilter",
        type = "baja:String",
        defaultValue = "BString.make(\"\")",
        flags = Flags.SUMMARY
)

@NiagaraProperty(
        name = "taggingFilter",
        type = "baja:String",
        defaultValue = "BString.make(\"\")",
        flags = Flags.SUMMARY
)

@NiagaraProperty(
        name = "jobInterlockWorker",
        type = "ResoluteTagUtils:JobInterlockWorker",
        defaultValue = "BJobInterlockWorker.make()",
        flags = Flags.SUMMARY
)

@NiagaraAction(
        name = "tagIt",
        parameterType = "baja:String",
        defaultValue = "BString.make(\"\")",
        flags = Flags.OPERATOR | Flags.SUMMARY
)

@NiagaraAction(
        name = "removeIt",
        parameterType = "baja:String",
        defaultValue = "BString.make(\"\")",
        flags = Flags.SUMMARY | Flags.OPERATOR
)

@NiagaraAction(
        name = "fetchIt",
        flags = Flags.OPERATOR | Flags.SUMMARY
)

//@NiagaraAction(
//        name = "checkRunningJobs",
//        flags = Flags.SUMMARY
//)

public class BTagImporter extends BComponent {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.components.BTagImporter(2730843638)1.0$ @*/
/* Generated Fri Oct 25 15:59:00 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Property "tags"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code tags} property.
   * @see #getTags
   * @see #setTags
   */
  public static final Property tags = newProperty(Flags.READONLY, BString.make(""), BFacets.make(BFacets.MULTI_LINE, true));
  
  /**
   * Get the {@code tags} property.
   * @see #tags
   */
  public String getTags() { return getString(tags); }
  
  /**
   * Set the {@code tags} property.
   * @see #tags
   */
  public void setTags(String v) { setString(tags, v, null); }

////////////////////////////////////////////////////////////////
// Property "jsonVersion"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code jsonVersion} property.
   * @see #getJsonVersion
   * @see #setJsonVersion
   */
  public static final Property jsonVersion = newProperty(0, ((BDouble)(BDouble.make(0.0))).getDouble(), null);
  
  /**
   * Get the {@code jsonVersion} property.
   * @see #jsonVersion
   */
  public double getJsonVersion() { return getDouble(jsonVersion); }
  
  /**
   * Set the {@code jsonVersion} property.
   * @see #jsonVersion
   */
  public void setJsonVersion(double v) { setDouble(jsonVersion, v, null); }

////////////////////////////////////////////////////////////////
// Property "importFile"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code importFile} property.
   * @see #getImportFile
   * @see #setImportFile
   */
  public static final Property importFile = newProperty(Flags.SUMMARY, BOrd.make("file:^ResoluteImports/rbiTagImport.json"), BFacets.make(BFacets.TARGET_TYPE, "baja:IFile"));
  
  /**
   * Get the {@code importFile} property.
   * @see #importFile
   */
  public BOrd getImportFile() { return (BOrd)get(importFile); }
  
  /**
   * Set the {@code importFile} property.
   * @see #importFile
   */
  public void setImportFile(BOrd v) { set(importFile, v, null); }

////////////////////////////////////////////////////////////////
// Property "deleteFilter"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code deleteFilter} property.
   * @see #getDeleteFilter
   * @see #setDeleteFilter
   */
  public static final Property deleteFilter = newProperty(Flags.SUMMARY, BString.make(""), null);
  
  /**
   * Get the {@code deleteFilter} property.
   * @see #deleteFilter
   */
  public String getDeleteFilter() { return getString(deleteFilter); }
  
  /**
   * Set the {@code deleteFilter} property.
   * @see #deleteFilter
   */
  public void setDeleteFilter(String v) { setString(deleteFilter, v, null); }

////////////////////////////////////////////////////////////////
// Property "taggingFilter"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code taggingFilter} property.
   * @see #getTaggingFilter
   * @see #setTaggingFilter
   */
  public static final Property taggingFilter = newProperty(Flags.SUMMARY, BString.make(""), null);
  
  /**
   * Get the {@code taggingFilter} property.
   * @see #taggingFilter
   */
  public String getTaggingFilter() { return getString(taggingFilter); }
  
  /**
   * Set the {@code taggingFilter} property.
   * @see #taggingFilter
   */
  public void setTaggingFilter(String v) { setString(taggingFilter, v, null); }

////////////////////////////////////////////////////////////////
// Property "jobInterlockWorker"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code jobInterlockWorker} property.
   * @see #getJobInterlockWorker
   * @see #setJobInterlockWorker
   */
  public static final Property jobInterlockWorker = newProperty(Flags.SUMMARY, BJobInterlockWorker.make(), null);
  
  /**
   * Get the {@code jobInterlockWorker} property.
   * @see #jobInterlockWorker
   */
  public BJobInterlockWorker getJobInterlockWorker() { return (BJobInterlockWorker)get(jobInterlockWorker); }
  
  /**
   * Set the {@code jobInterlockWorker} property.
   * @see #jobInterlockWorker
   */
  public void setJobInterlockWorker(BJobInterlockWorker v) { set(jobInterlockWorker, v, null); }

////////////////////////////////////////////////////////////////
// Action "tagIt"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code tagIt} action.
   * @see #tagIt(BString parameter)
   */
  public static final Action tagIt = newAction(Flags.OPERATOR | Flags.SUMMARY, BString.make(""), null);
  
  /**
   * Invoke the {@code tagIt} action.
   * @see #tagIt
   */
  public void tagIt(BString parameter) { invoke(tagIt, parameter, null); }

////////////////////////////////////////////////////////////////
// Action "removeIt"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code removeIt} action.
   * @see #removeIt(BString parameter)
   */
  public static final Action removeIt = newAction(Flags.SUMMARY | Flags.OPERATOR, BString.make(""), null);
  
  /**
   * Invoke the {@code removeIt} action.
   * @see #removeIt
   */
  public void removeIt(BString parameter) { invoke(removeIt, parameter, null); }

////////////////////////////////////////////////////////////////
// Action "fetchIt"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code fetchIt} action.
   * @see #fetchIt()
   */
  public static final Action fetchIt = newAction(Flags.OPERATOR | Flags.SUMMARY, null);
  
  /**
   * Invoke the {@code fetchIt} action.
   * @see #fetchIt
   */
  public void fetchIt() { invoke(fetchIt, null, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BTagImporter.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/


  /***
   * Sample json import file
   *
   {
     "version": 1.0,
     "jsonVersion": 1.0,
     "points": [
                {
                "name": "TestBoolPoint0",
                "metricId": "aadb5fe6-598d-4f72-b5d6-1c72b74a4211./TestBoolPoint0",
                "tags": ["boiler", "cmd"]
               },
               {
                "name": "TestNumPoint0",
                "metricId": "aadb5fe6-598d-4f72-b5d6-1c72b74a4211./TestNumPoint0",
                "tags": ["discharge", "water", "temp", "sensor"]
               },
               {
                "name": "TestNumPoint1",
                "metricId": "aadb5fe6-598d-4f72-b5d6-1c72b74a4211./TestNumPoint1",
                "tags": ["discharge", "air", "temp", "sensor"]
               },
               {
                "name": "TestNumPoint2",
                "metricId": "aadb5fe6-598d-4f72-b5d6-1c72b74a4211./TestNumPoint2",
                "tags": ["return", "air", "temp", "sensor"]
               },
               {
                "name": "TestNumPoint3",
                "metricId": "aadb5fe6-598d-4f72-b5d6-1c72b74a4211./TestNumPoint3",
                "tags": ["air", "mixed", "temp", "sensor"]
               },
               {
                "name": "TestBoolPoint1",
                "metricId": "aadb5fe6-598d-4f72-b5d6-1c72b74a4211./TestBoolPoint1",
                "tags": ["chiller", "cmd"]
               },
               {
                "name": "TestEnumPoint0",
                "metricId": "aadb5fe6-598d-4f72-b5d6-1c72b74a4211./TestEnumPoint0",
                "tags": ["relief", "fan", "speed", "sp"]
               },
               {
                "name": "TestStringPoint0",
                "metricId": "aadb5fe6-598d-4f72-b5d6-1c72b74a4211./TestStringPoint0",
                "tags": ["building", "zone"]
               }
     ],
     "currentTagSet": ["active\nair\navg\nboiler\nbuilding\nchilled\nchiller\ncirc\ncmd\nco2\ncondenser\ncool\ncooling\ncurrent\ndamper\ndeHumidify\ndelta\ndeviation\ndischarge\ndomestic\neconomize\neffective\nelec\nenable\nenergy\nentering\nexhaust\nfaceBypass\nfan\nfilter\nfinal\nflow\nfreezeStat\nfreq\ngas\nheat\nheating\nhigh\nhisTotalized\nhot\nhru\nhumidifier\nhumidity\nisolation\nleaving\nlevel\nload\nlow\nmag\nmax\nmed\nmin\nmixed\nocc\noccupied\noil\noutside\npf\nphaseA\nphaseAB\nphaseAN\nphaseB\nphaseBC\nphaseBN\nphaseC\nphaseCA\nphaseCN\npower\npre\npressure\npump\nreheat\nrelief\nreturn\nrun\nruntime\nsensor\nsp\nspeed\nsump\ntemp\ntotal\nunocc\nvalve\nvolt\nvolume\nwater\nzone\n"]
   }
   *
   ***/

  private static Logger logger = Logger.getLogger("Resolute Tag Utils");
  private AtomicReference<Boolean> isJobRunning = new AtomicReference<>(false);
  private BPointTable pointTable;
  private BSimpleJob runningJob;

  public BPointTable getPointTable(){ return pointTable; }
  public void setPointTable(BPointTable pointTable){
    this.pointTable = pointTable;
  }

  public BSimpleJob getRunningJob(){ return this.runningJob; }
  private void setRunningJob(BSimpleJob job){
    runningJob = job;
  }

  @Override
  public void started(){
      logger.fine("TagImporter...started");
    setImportFile(BOrd.make("file:^ResoluteImports/rbiTagImport.json"));
    try{
      BIFile file = (BIFile)getImportFile().get(Sys.getStation());
      logger.info(file.getFilePath().toString().concat(" folder already present in the station..."));

      String ord = getImportFile().encodeToString();
      BOrd resDir = BOrd.make((ord.split("/"))[0]);
      OrdQuery[] fileQueries = getImportFile().parse();
      FilePath fp = (FilePath)fileQueries[fileQueries.length-1];
      BIDirectory resoluteDir = (BIDirectory)resDir.get(Sys.getStation());
      try{
        logger.info("Checking for old tag import files in the Resolute folder...");
        for(BIFile f: resoluteDir.listFiles()){
          if(f.getFileName().equals(file.getFileName())){
            logger.warning("Found old copies of the tag import file in the Resolute folder...\nDeleting...");
            logger.info(file.getFileName());
            BFileSystem.INSTANCE.delete(fp, null);
          }
        }

      }catch(IOException ioe){
        logger.severe(ioe.getMessage());
        ioe.printStackTrace();
      }

    }catch(UnresolvedException ue){

      logger.warning(ue.getMessage());
      logger.warning("Resolute folder not found...\nCreating...");

      String ord = getImportFile().encodeToString();
      BOrd resDir = BOrd.make((ord.split("/"))[0]);
      OrdQuery[] dirQueries = resDir.parse();
      FilePath dp = (FilePath)dirQueries[dirQueries.length-1];

      try{
        BIDirectory resoluteDir = BFileSystem.INSTANCE.makeDir(dp,null);
        logger.info(resoluteDir.getNavName().concat(" created..."));
      }catch(IOException ioe){
        logger.severe(ioe.getMessage());
        ioe.printStackTrace();
      }
    }
  }

  @Override
  public void atSteadyState(){

    logger.fine("TagImporter...atSteadyState");
    setImportFile(BOrd.make("file:^ResoluteImports/rbiTagImport.json"));
    try{
      BIFile file = (BIFile)getImportFile().get(Sys.getStation());
      logger.info(file.getFilePath().toString().concat(" folder already present in the station..."));

      String ord = getImportFile().encodeToString();
      BOrd resDir = BOrd.make((ord.split("/"))[0]);
      OrdQuery[] fileQueries = getImportFile().parse();
      FilePath fp = (FilePath)fileQueries[fileQueries.length-1];
      BIDirectory resoluteDir = (BIDirectory)resDir.get(Sys.getStation());
      try{
        logger.info("Checking for old tag import files in the Resolute folder...");
        for(BIFile f: resoluteDir.listFiles()){
          if(f.getFileName().equals(file.getFileName())){
            logger.warning("Found old copies of the tag import file in the Resolute folder...\nDeleting...");
            logger.info(file.getFileName());
            BFileSystem.INSTANCE.delete(fp, null);
          }
        }

      }catch(IOException ioe){
        logger.severe(ioe.getMessage());
        ioe.printStackTrace();
      }

    }catch(UnresolvedException ue){

      logger.warning(ue.getMessage());
      logger.warning("Resolute folder not found...\nCreating...");

      String ord = getImportFile().encodeToString();
      BOrd resDir = BOrd.make((ord.split("/"))[0]);
      OrdQuery[] dirQueries = resDir.parse();
      FilePath dp = (FilePath)dirQueries[dirQueries.length-1];

      try{
        BIDirectory resoluteDir = BFileSystem.INSTANCE.makeDir(dp,null);
        logger.info(resoluteDir.getNavName().concat(" created..."));
      }catch(IOException ioe){
        logger.severe(ioe.getMessage());
        ioe.printStackTrace();
      }
    }
  }

  public void doTagIt(BString filter, Context cx){
    if(getRunningJob() != null){

      //TODO - do a fetch job before tagging

      if(getRunningJob().isAlive()){
        logger.info("Tried to run tagIt, but there's another job running...!");
      }else{
        BSimpleJob job = new BTaggingJob();
        if(!filter.getString().isEmpty()){
          try{
            setTaggingFilter(filter.encodeToString());
            BJobService.getService().submit(job, cx);
          }catch(IOException ioe){
            logger.severe(ioe.getMessage());
            ioe.printStackTrace();
          }
        }else{
          NullPointerException npe = new NullPointerException();
          logger.severe("[Bulk Tag Remove Op Error] - Tag filter field can't be empty...! "
                  + npe.getMessage());
          throw npe;
        }
        setRunningJob(job);
      }
    }else{
      BSimpleJob job = new BTaggingJob();
      if(!filter.getString().isEmpty()){
        try{
          setTaggingFilter(filter.encodeToString());
          BJobService.getService().submit(job, cx);
        }catch(IOException ioe){
          logger.severe(ioe.getMessage());
          ioe.printStackTrace();
        }
      }else{
        NullPointerException npe = new NullPointerException();
        logger.severe("[Bulk Tag Remove Op Error] - Tag filter field can't be empty...! "
                + npe.getMessage());
        throw npe;
      }
      setRunningJob(job);
    }
  }

  public void doRemoveIt(BString filter, Context cx){
    if(getRunningJob() != null){
      if(getRunningJob().isAlive()){
        logger.info("Tried to run tagIt, but there's another job running...!");
      }else{
        BSimpleJob job = new BBulkTagRemoveJob();
        if(!filter.getString().isEmpty()){
          try{
            setDeleteFilter(filter.encodeToString());
            BJobService.getService().submit(job, cx);
          }catch(IOException ioe){
            logger.severe(ioe.getMessage());
            ioe.printStackTrace();
          }
        }else{
          NullPointerException npe = new NullPointerException();
          logger.severe("[Bulk Tag Remove Op Error] - Tag filter field can't be empty...! "
                  + npe.getMessage());
          throw npe;
        }
        setRunningJob(job);
      }
    }else{
      BSimpleJob job = new BBulkTagRemoveJob();
      if(!filter.getString().isEmpty()){
        try{
          setDeleteFilter(filter.encodeToString());
          BJobService.getService().submit(job, cx);
        }catch(IOException ioe){
          logger.severe(ioe.getMessage());
          ioe.printStackTrace();
        }
      }else{
        NullPointerException npe = new NullPointerException();
        logger.severe("[Bulk Tag Remove Op Error] - Tag filter field can't be empty...! "
                + npe.getMessage());
        throw npe;
      }
      setRunningJob(job);
    }
  }

  public void doFetchIt(Context cx){
    if(getRunningJob() != null){
      if(getRunningJob().isAlive()){
        logger.info("Tried to run tagIt, but there's another job running...!");
      }else{
        BSimpleJob job = new BFetchJob();
        BJobService.getService().submit(job, cx);
        setRunningJob(job);
      }
    }else{
      BSimpleJob job = new BFetchJob();
      BJobService.getService().submit(job, cx);
      setRunningJob(job);
    }
  }

  public static BTagImporter make(){
    return new BTagImporter();
  }

  public IFuture post(Action a, BValue arg, Context cx){
    Invocation work = new Invocation(this, a, arg, cx);
    this.getJobInterlockWorker().postWork(work);
    return null;
  }
}
