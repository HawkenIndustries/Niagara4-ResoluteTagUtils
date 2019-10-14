package com.resolute.ResoluteTagUtils.services;

import com.resolute.ResoluteTagUtils.components.BTagImporter;

import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
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
/*@ $com.resolute.ResoluteTagUtils.services.BResoluteTagUtils(2544441235)1.0$ @*/
/* Generated Sat Oct 12 12:27:20 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

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
    }

    @Override
    public void atSteadyState(){
      logger.info("Resolute Tag Util Service is at steady state...");
    }

}
