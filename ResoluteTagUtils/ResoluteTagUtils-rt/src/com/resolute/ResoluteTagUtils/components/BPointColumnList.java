package com.resolute.ResoluteTagUtils.components;

import javax.baja.collection.Column;
import javax.baja.collection.ColumnList;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.BObject;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import java.util.ArrayList;

@NiagaraType

public class BPointColumnList extends BObject implements ColumnList {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.components.BPointColumnList(2979906276)1.0$ @*/
/* Generated Mon Oct 14 15:25:07 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BPointColumnList.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private final ArrayList<BPointColumn> columns = new ArrayList<>();

    public BPointColumnList(){
        columns.add(BPointColumn.make("pointName"));
        columns.add(BPointColumn.make("metricId"));
        columns.add(BPointColumn.make("tags"));
    }

    @Override
    public int size() {
        return columns.size();
    }

    @Override
    public Column get(int i) {
        try{
            return columns.get(i);
        }catch(IndexOutOfBoundsException ioobe){
            ioobe.printStackTrace();
            return null;
        }
    }

    @Override
    public Column get(String s) {
        for(BPointColumn pc : columns){
            if(pc.getTag().equals(s)){
                return pc;
            }
        }
        return null;
    }

    @Override
    public int indexOf(String s) {
        try{
            for(BPointColumn pc: columns){
                if(pc.getTag().equals(s)){
                    return pc.getOrdinal();
                }
            }
            return -1;
        }catch(IndexOutOfBoundsException ioobe){
            ioobe.printStackTrace();
            return -2;
        }
    }

    @Override
    public Column[] list() {
        return (Column[])columns.toArray();
    }

}
