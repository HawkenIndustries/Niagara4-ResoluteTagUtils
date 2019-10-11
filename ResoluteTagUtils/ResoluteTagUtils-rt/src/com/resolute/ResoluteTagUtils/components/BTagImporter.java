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

@NiagaraProperty(
        name = "importFile",
        type = "baja:Ord",
        defaultValue = "BOrd.make(\"file:^ResoluteImports/tagImport.json\")",
        facets = {
                @Facet(name = "BFacets.TARGET_TYPE", value = "\"baja:IFile\"")
        },
        flags = Flags.OPERATOR | Flags.SUMMARY
)

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

@NiagaraAction(
        name = "tagIt",
        flags = Flags.OPERATOR | Flags.SUMMARY
)

public class BTagImporter extends BComponent {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.components.BTagImporter(3633830379)1.0$ @*/
/* Generated Fri Oct 11 09:13:08 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Property "importFile"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code importFile} property.
   * @see #getImportFile
   * @see #setImportFile
   */
  public static final Property importFile = newProperty(Flags.OPERATOR | Flags.SUMMARY, BOrd.make("file:^ResoluteImports/tagImport.json"), BFacets.make(BFacets.TARGET_TYPE, "baja:IFile"));
  
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
   *
   {
     "version": 1.0,
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

  @Override
  public void started(){
      logger.fine("TagImporter...started");
  }

  @Override
  public void atSteadyState(){

    logger.fine("TagImporter...atSteadyState");

    try{
      BIFile file = (BIFile)getImportFile().get(Sys.getStation());
      logger.info(file.getFilePath().toString().concat(" folder already present in the station..."));

      String ord = getImportFile().encodeToString();
      BOrd resDir = BOrd.make((ord.split("/"))[0]);
      OrdQuery[] dirQueries = resDir.parse();
      OrdQuery[] fileQueries = getImportFile().parse();
      FilePath dp = (FilePath)dirQueries[dirQueries.length-1];
      FilePath fp = (FilePath)fileQueries[fileQueries.length-1];
      BIDirectory resoluteDir = (BIDirectory)resDir.get(Sys.getStation());
      try{
        logger.info("Checking for old tag import files in the Resolute folder...");
        for(BIFile f: resoluteDir.listFiles()){
          if(file.getFileName().equals("rbiTagImport.json")){
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

  public void doTagIt(Context cx){
      BJobService.getService().submit(new BTaggingJob(), cx);
  }

  public static BTagImporter make(){ return new BTagImporter(); }

}
