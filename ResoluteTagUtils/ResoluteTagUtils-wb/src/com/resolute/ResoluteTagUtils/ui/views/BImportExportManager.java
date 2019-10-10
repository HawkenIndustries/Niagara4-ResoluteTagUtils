package com.resolute.ResoluteTagUtils.ui.views;

import javax.baja.naming.BOrd;
import javax.baja.nre.annotations.AgentOn;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.Context;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.ui.CommandArtifact;
import javax.baja.workbench.mgr.BAbstractManager;
import javax.baja.workbench.mgr.MgrColumn;
import javax.baja.workbench.mgr.MgrController;
import javax.baja.workbench.mgr.MgrModel;

@NiagaraType(
        agent = @AgentOn(
                types = {"ResoluteTagUtils:ResoluteTagManager"},
                defaultAgent = AgentOn.Preference.PREFERRED
        )
)

public class BImportExportManager extends BAbstractManager {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.ui.views.BImportExportManager(3429051195)1.0$ @*/
/* Generated Wed Oct 09 07:14:09 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BImportExportManager.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/


/***
 * Sample json import file
 * {
    "version": 1.0,
        "currentTagSet": [],
    "points": {
        "name": "BoolTestPoint0",
        "ord": "slot:/BoolTestPoint0",
        "tags": []
    }
}***/

    class Model extends MgrModel {
        private String[] tags;
        public Model(BImportExportManager manager){
            super(manager);
        }

        @Override
        protected MgrColumn[] makeColumns(){
            return super.makeColumns();
        }
    }

    @Override
    protected MgrModel makeModel(){
        return new Model(this);
    }

    @Override
    protected MgrController makeController(){
        return new MgrController(this);
    }

}
