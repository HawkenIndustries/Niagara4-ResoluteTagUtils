package com.resolute.ResoluteTagUtils.ui.components;

import com.tridium.kitpx.BWbCommandButton;

import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.ui.Command;

@NiagaraType

public class BImportButton extends BWbCommandButton {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.ui.components.BImportButton(2979906276)1.0$ @*/
/* Generated Wed Oct 09 09:30:23 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BImportButton.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/
    @Override
    public Command getWbCommand() {
        return null;
    }
}
