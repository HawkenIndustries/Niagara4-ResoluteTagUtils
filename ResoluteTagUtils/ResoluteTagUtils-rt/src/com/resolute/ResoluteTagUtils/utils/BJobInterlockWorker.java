package com.resolute.ResoluteTagUtils.utils;

import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.NotRunningException;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.util.BWorker;
import javax.baja.util.CoalesceQueue;
import javax.baja.util.Worker;

@NiagaraType

public class BJobInterlockWorker extends BWorker {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.utils.BJobInterlockWorker(2979906276)1.0$ @*/
/* Generated Fri Oct 25 15:30:14 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BJobInterlockWorker.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private CoalesceQueue queue;
    private Worker worker;

    @Override
    public Worker getWorker(){
        if(null == worker){
            queue = new CoalesceQueue(100);
            worker = new Worker(queue);
        }
        return worker;
    }

    public void postWork(Runnable runnable){
        if(null == queue || !isRunning()) throw new NotRunningException();
        queue.enqueue(runnable);
    }
    public static BJobInterlockWorker make() { return new BJobInterlockWorker(); }
}
