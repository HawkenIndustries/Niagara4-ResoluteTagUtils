package com.resolute.ResoluteTagUtils.components;

import com.resolute.ResoluteTagUtils.models.Point;

import javax.baja.collection.BITable;
import javax.baja.collection.ColumnList;
import javax.baja.collection.TableCursor;
import javax.baja.data.BIDataValue;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NiagaraType

public class BPointTable implements BITable<BPoint> {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.components.BPointTable(2979906276)1.0$ @*/
/* Generated Mon Oct 14 14:40:39 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BPointTable.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private Set<BPoint> pointTable;

    private BPointTable(){}

    public static BPointTable make(){ return new BPointTable(); }

    public Set<BPoint> get(){ return pointTable; }
    public void set(HashSet<Point> points){
        if(points != null){
            pointTable =
                    Collections.synchronizedSet(
                            new HashSet<BPoint>(points.size(), 100));

            points.forEach( point -> {
                pointTable.add(
                        BPoint.make(point.getName(), point.getMetricId(), point.getTags()));
            });
        }
    }

    @Override
    public TableCursor<BPoint> cursor() {
        return new BPointTableCursor(this);
    }

    @Override
    public ColumnList getColumns() {
        return new BPointColumnList();
    }

    @Override
    public BFacets getTableFacets() {
        return null;
    }

    @Override
    public BObject asObject() {
        return this.asObject();
    }

    /***
     *
     * @param aClass
     * @param <T>
     * @return null - not implemented
     */
    @Override
    public <T extends BIObject> T as(Class<T> aClass) {
        return null;
    }

    @Override
    public BIDataValue toDataValue() {
        return this.asObject().toDataValue();
    }

    @Override
    public String toString(Context context) {
        try {
            return this.toDataValue().encodeToString();
        } catch (IOException e) {
            e.printStackTrace();
            return "null";
        }
    }

    @Override
    public boolean equivalent(Object o) {
        return this.asObject().equivalent(o);
    }
}
