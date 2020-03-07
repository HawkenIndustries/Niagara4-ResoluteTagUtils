package com.resolute.ResoluteTagUtils.utils;

import javax.baja.collection.BITable;
import javax.baja.collection.TableCursor;
import javax.baja.control.BBooleanPoint;
import javax.baja.control.BEnumPoint;
import javax.baja.control.BNumericPoint;
import javax.baja.control.BStringPoint;
import javax.baja.naming.BOrd;
import javax.baja.sys.BComponent;
import javax.baja.sys.Sys;
import java.util.HashSet;
import java.util.function.Predicate;

public class PointUtils {

    /***
     * Pass a fully qualified BQL query to resolve a number of baja components.
     * @param bql   BOrd
     * @return      HashSet of BComponents
     * @throws Exception
     */
    public static HashSet<BComponent> resolveBql(BOrd bql) throws Exception {
        BITable<BComponent> t = (BITable) bql.resolve(Sys.getStation()).get();
        HashSet<BComponent> refs = new HashSet<>(
                                        (int) t.cursor().stream().count(),
                                        100);

        TableCursor<BComponent> c = t.cursor();
        while(c.next()){
            refs.add(c.get());
        }
        return refs;
    }

    /***
     * Returns true if the baja component evaluated is a point type of bool, num, enum
     * or string.
     */
    Predicate<BComponent> isPoint = (comp) -> {
        try{
            BBooleanPoint bp = (BBooleanPoint) comp;
            return true;
        }catch(Exception e){}
        try{
            BNumericPoint np = (BNumericPoint) comp;
            return true;
        }catch(Exception e){}
        try{
            BEnumPoint ep = (BEnumPoint) comp;
        }catch(Exception e){}
        try{
            BStringPoint sp = (BStringPoint) comp;
        }catch(Exception e){}
        return false;
    };



}
