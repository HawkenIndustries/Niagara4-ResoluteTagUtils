package com.resolute.ResoluteTagUtils.components;

import com.resolute.ResoluteTagUtils.utils.PointUtils;

import javax.baja.naming.BOrd;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;

@NiagaraType

@NiagaraProperty(
        name = "enable",
        type = "baja:Boolean",
        defaultValue = "BBoolean.make(true)",
        flags = Flags.SUMMARY
)

public class BPointScanner extends BComponent {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.components.BPointScanner(3605011373)1.0$ @*/
/* Generated Sat Nov 30 22:12:11 EST 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Property "enable"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code enable} property.
   * @see #getEnable
   * @see #setEnable
   */
  public static final Property enable = newProperty(Flags.SUMMARY, ((BBoolean)(BBoolean.make(true))).getBoolean(), null);
  
  /**
   * Get the {@code enable} property.
   * @see #enable
   */
  public boolean getEnable() { return getBoolean(enable); }
  
  /**
   * Set the {@code enable} property.
   * @see #enable
   */
  public void setEnable(boolean v) { setBoolean(enable, v, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BPointScanner.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    /**
     * Retrieve current point status, and proxy extension status if proxy is not null.
     * Retrieve point extension list and extension status (ie. history ext, alarm ext, etc...)
     * Run and generate a csv report highlighting data point issues causing data flow disruption
     * */

    Runnable scanner = () -> {

    };

}
