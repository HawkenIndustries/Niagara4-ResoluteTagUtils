package com.resolute.ResoluteTagUtils.components;

import javax.baja.collection.BITable;
import javax.baja.collection.Column;
import javax.baja.collection.Row;
import javax.baja.collection.TableCursor;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

@NiagaraType

public class BPointTableCursor extends BObject implements TableCursor {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.components.BPointTableCursor(2979906276)1.0$ @*/
/* Generated Mon Oct 14 14:40:39 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BPointTableCursor.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private BPointTable t;
    private Iterator<BPoint> it;
    private BPoint currentPoint;
    private Row<BPoint> row;

    public BPointTableCursor(BPointTable table){
        t = table;
    }

    @Override
    public BITable getTable() {
        return t;
    }

    @Override
    public Row row() {
        row = new BPointRow(currentPoint, this.t);
        return row;
    }

    @Override
    public BIObject cell(Column column) {
        return row.cell(column);
    }

    @Override
    public Context getContext() {
        return this.getContext();
    }

    @Override
    public boolean next() {
        return it.hasNext();
    }

    @Override
    public Object get() {
        currentPoint = it.next();
        return currentPoint;
    }

    @Override
    public void close() {}

    @Override
    public Iterator iterator() {
        return it;
    }

    @Override
    public void forEach(Consumer action) {}

    @Override
    public Spliterator spliterator() {
        return t.get().spliterator();
    }

    @Override
    public Stream stream() {
        return null;
    }
}
