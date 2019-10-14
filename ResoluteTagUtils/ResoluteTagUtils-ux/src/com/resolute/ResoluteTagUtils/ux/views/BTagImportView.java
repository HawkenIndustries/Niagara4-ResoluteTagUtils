package com.resolute.ResoluteTagUtils.ux.views;

import javax.baja.naming.BOrd;
import javax.baja.nre.annotations.AgentOn;
import javax.baja.nre.annotations.NiagaraSingleton;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.BSingleton;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.web.BIFormFactorMax;
import javax.baja.web.js.BIJavaScript;
import javax.baja.web.js.JsInfo;

@NiagaraType(
        agent = @AgentOn(
                types = {"ResoluteTagUtils:TagImporter"}
        )
)

@NiagaraSingleton

public class BTagImportView extends BSingleton implements BIJavaScript, BIFormFactorMax {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.ux.views.BTagImportView(3751082187)1.0$ @*/
/* Generated Mon Oct 14 08:58:29 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */
  
  public static final BTagImportView INSTANCE = new BTagImportView();

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BTagImportView.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    @Override
    public JsInfo getJsInfo(Context cx){ return jsInfo; }

    private static final JsInfo jsInfo =
            JsInfo.make(
                    BOrd.make(
                            "module://ResoluteTagUtils/ResoluteTagUtils-ux/res/js/TagImporterView.js"));
}
