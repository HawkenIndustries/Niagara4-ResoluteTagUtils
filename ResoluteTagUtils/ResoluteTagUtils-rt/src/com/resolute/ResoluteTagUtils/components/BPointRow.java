package com.resolute.ResoluteTagUtils.components;

import javax.baja.collection.BITable;
import javax.baja.collection.Column;
import javax.baja.collection.Row;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import java.util.concurrent.atomic.AtomicReference;

@NiagaraType

public class BPointRow extends BObject implements Row<BPoint> {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.components.BPointRow(2979906276)1.0$ @*/
/* Generated Mon Oct 14 14:40:39 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BPointRow.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private BPoint point;
    private BPointTable table;

    public BPointRow(BPoint point, BPointTable table){
        this.point = point;
        this.table = table;
    }

    @Override
    public BITable<BPoint> getTable() {
        AtomicReference<BITable> t = new AtomicReference<>(table);
        return t.get();
    }

    @Override
    public BPoint rowObject() {
        return point;
    }

    @Override
    public BIObject cell(Column column) {
        AtomicReference<BIObject> o = new AtomicReference<>(new BComponent());
        point.getProperties().forEach( p -> {
            if(p.getName().equals(column.getName())){
                o.set((BIObject) p);
            }
        });
        return o.get();
    }

    @Override
    public int getCellFlags(Column column) {
        return 0;
    }

    @Override
    public BFacets getCellFacets(Column column) {
        return null;
    }

    @Override
    public Row<BPoint> safeCopy() {
        AtomicReference<Row<BPoint>> r =
                new AtomicReference<>(this);
        return r.get();
    }
}
