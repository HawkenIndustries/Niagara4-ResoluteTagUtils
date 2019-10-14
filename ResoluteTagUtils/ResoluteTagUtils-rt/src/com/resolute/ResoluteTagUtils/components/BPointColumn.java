package com.resolute.ResoluteTagUtils.components;

import javax.baja.collection.Column;
import javax.baja.nre.annotations.NiagaraEnum;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.nre.annotations.Range;
import javax.baja.sys.*;
import java.io.IOException;

@NiagaraType

@NiagaraEnum(
        range = {
                @Range("pointName"),
                @Range("metricId"),
                @Range("tags")
        },
        defaultValue = "pointName"
)

public final class BPointColumn extends BFrozenEnum implements Column {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.components.BPointColumn(1011913392)1.0$ @*/
/* Generated Mon Oct 14 14:40:39 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */
  
  /** Ordinal value for pointName. */
  public static final int POINT_NAME = 0;
  /** Ordinal value for metricId. */
  public static final int METRIC_ID = 1;
  /** Ordinal value for tags. */
  public static final int TAGS = 2;
  
  /** BPointColumn constant for pointName. */
  public static final BPointColumn pointName = new BPointColumn(POINT_NAME);
  /** BPointColumn constant for metricId. */
  public static final BPointColumn metricId = new BPointColumn(METRIC_ID);
  /** BPointColumn constant for tags. */
  public static final BPointColumn tags = new BPointColumn(TAGS);
  
  /** Factory method with ordinal. */
  public static BPointColumn make(int ordinal)
  {
    return (BPointColumn)pointName.getRange().get(ordinal, false);
  }
  
  /** Factory method with tag. */
  public static BPointColumn make(String tag)
  {
    return (BPointColumn)pointName.getRange().get(tag);
  }
  
  /** Private constructor. */
  private BPointColumn(int ordinal)
  {
    super(ordinal);
  }
  
  public static final BPointColumn DEFAULT = pointName;

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BPointColumn.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    @Override
    public String getName() {
        return this.getTag();
    }

    @Override
    public String getDisplayName(Context context) {
        switch(this.getTag()){
            case "pointName":
                return "Name";
            case "metricId":
                return "Metric ID";
            case "tags":
                return "Tags";
            default:
                return "Null";
        }
    }

    @Override
    public int getFlags() {
        return 0;
    }

    @Override
    public BFacets getFacets() {
        return null;
    }
}
