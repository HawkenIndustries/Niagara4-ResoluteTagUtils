package com.resolute.ResoluteTagUtils.services;

import com.resolute.ResoluteTagUtils.components.BTagImporter;

import javax.baja.file.BFileSystem;
import javax.baja.file.BIDirectory;
import javax.baja.file.BIFile;
import javax.baja.file.FilePath;
import javax.baja.naming.BOrd;
import javax.baja.naming.OrdQuery;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import java.io.IOException;
import java.util.logging.Logger;

@NiagaraType

@NiagaraProperty(
        name = "TagImporter",
        type = "ResoluteTagUtils:TagImporter",
        defaultValue = "BTagImporter.make()",
        flags = Flags.OPERATOR
)

public class BResoluteTagUtils extends BAbstractService {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.services.BResoluteTagUtils(3842799873)1.0$ @*/
/* Generated Thu Oct 10 16:38:43 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Property "TagImporter"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code TagImporter} property.
   * @see #getTagImporter
   * @see #setTagImporter
   */
  public static final Property TagImporter = newProperty(Flags.OPERATOR, BTagImporter.make(), null);
  
  /**
   * Get the {@code TagImporter} property.
   * @see #TagImporter
   */
  public BTagImporter getTagImporter() { return (BTagImporter)get(TagImporter); }
  
  /**
   * Set the {@code TagImporter} property.
   * @see #TagImporter
   */
  public void setTagImporter(BTagImporter v) { set(TagImporter, v, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BResoluteTagUtils.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private static Type[] serviceTypes = new Type[] { TYPE };
    private static Logger logger = Logger.getLogger("Resolute Tag Utils");

    @Override
    public Type[] getServiceTypes(){ return serviceTypes; }

    @Override
    public void serviceStarted(){
      logger.fine("Resolute Tag Util Service is running...");
      /***
       * verify the resolute folder exists, if it doesn't try to create it
       * PROBLEM !!!!!!!!!!!!!!!
       * WARNING [11:30:07 11-Oct-19 EDT][Resolute Tag Utils] did not find resolute directory at start-up...!
       * WARNING [11:30:07 11-Oct-19 EDT][Resolute Tag Utils] creating resolute directory...
       * SEVERE [11:30:07 11-Oct-19 EDT][Resolute Tag Utils] access denied ("java.io.FilePermission" "c:\jci\fxworkbench-14.6" "read")
       * java.security.AccessControlException: access denied ("java.io.FilePermission" "c:\jci\fxworkbench-14.6" "read")
       *         at java.security.AccessControlContext.checkPermission(AccessControlContext.java:472)
       *         at java.security.AccessController.checkPermission(AccessController.java:884)
       *         at java.lang.SecurityManager.checkPermission(SecurityManager.java:549)
       *         at java.lang.SecurityManager.checkRead(SecurityManager.java:888)
       *         at java.io.File.exists(File.java:814)
       *         at javax.baja.file.BFileSystem.precheckAddEvent(BFileSystem.java:444)
       *         at javax.baja.file.BFileSystem.makeDir(BFileSystem.java:306)
       *         at javax.baja.file.BFileSpace.makeDir(BFileSpace.java:90)
       *         at com.resolute.ResoluteTagUtils.services.BResoluteTagUtils.serviceStarted(BResoluteTagUtils.java:98)
       *         at com.tridium.sys.service.ServiceManager.startService(ServiceManager.java:384)
       *         at com.tridium.sys.service.ServiceManager.startAllServices(ServiceManager.java:269)
       *         at com.tridium.sys.station.Station.initServices(Station.java:330)
       *         at com.tridium.sys.station.Station.bootStation(Station.java:108)
       *         at com.tridium.sys.station.Station.main(Station.java:1140)
       *         at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
       *         at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
       *         at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
       *         at java.lang.reflect.Method.invoke(Method.java:498)
       *         at com.tridium.sys.Nre.runClass(Nre.java:393)
       *         at com.tridium.sys.Nre.main(Nre.java:228)
       *         at com.tridium.sys.Nre.bootstrap(Nre.java:145)
       *         at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
       *         at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
       *         at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
       *         at java.lang.reflect.Method.invoke(Method.java:498)
       *         at com.tridium.nre.bootstrap.Bootstrap.Main(Bootstrap.java:104)
       */
//      try{
//        for(BIFile fileObj: BFileSystem.INSTANCE.listFiles()){
//
//          if(fileObj.isDirectory() &&
//                  fileObj.getFileName().equals("ResoluteImports")){
//            deleteOldFiles(fileObj.getAbsoluteOrd());
//
//            OrdQuery[] ordQueries = fileObj.getAbsoluteOrd().parse();
//            FilePath fp = (FilePath)ordQueries[ordQueries.length-1];
//
//            logger.fine("ResoluteImports:\n\tFolder Path:".concat(fp.toString()));
//            logger.fine("\tAbsolute Ord: ".concat(fileObj.getAbsoluteOrd().encodeToString()));
//            for(OrdQuery oq: ordQueries){
//              logger.fine(oq.toString());
//            }
//
//          }else{
//            logger.warning("did not find resolute directory at start-up...!");
//            logger.warning("creating resolute directory...");
//            OrdQuery[] ordQueries = fileObj.getAbsoluteOrd().parse();
//            FilePath fp = (FilePath)ordQueries[ordQueries.length-1];
//            try{
//              BFileSystem.INSTANCE.makeDir(fp);
//            }catch(IOException ioe){}
//          }
//        }
//      }catch(Exception e){
//        logger.severe(e.getMessage());
//        e.printStackTrace();
//      }

    }

    @Override
    public void atSteadyState(){
      logger.info("Resolute Tag Util Service is at steady state...");
    }

}
