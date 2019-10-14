package com.resolute.ResoluteTagUtils.components;

import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NiagaraType
@NiagaraProperty(
        name = "pointName",
        type = "baja:String",
        defaultValue = "BString.make(\"\")"
)
@NiagaraProperty(
        name = "metricId",
        type = "baja:String",
        defaultValue = "BString.make(\"\")"
)
@NiagaraProperty(
        name = "tags",
        type = "baja:String",
        defaultValue = "BString.make(\"\")"
)

public class BPoint extends BComponent {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.resolute.ResoluteTagUtils.components.BPoint(2762374506)1.0$ @*/
/* Generated Mon Oct 14 11:08:55 EDT 2019 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Property "pointName"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code pointName} property.
   * @see #getPointName
   * @see #setPointName
   */
  public static final Property pointName = newProperty(0, BString.make(""), null);
  
  /**
   * Get the {@code pointName} property.
   * @see #pointName
   */
  public String getPointName() { return getString(pointName); }
  
  /**
   * Set the {@code pointName} property.
   * @see #pointName
   */
  public void setPointName(String v) { setString(pointName, v, null); }

////////////////////////////////////////////////////////////////
// Property "metricId"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code metricId} property.
   * @see #getMetricId
   * @see #setMetricId
   */
  public static final Property metricId = newProperty(0, BString.make(""), null);
  
  /**
   * Get the {@code metricId} property.
   * @see #metricId
   */
  public String getMetricId() { return getString(metricId); }
  
  /**
   * Set the {@code metricId} property.
   * @see #metricId
   */
  public void setMetricId(String v) { setString(metricId, v, null); }

////////////////////////////////////////////////////////////////
// Property "tags"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code tags} property.
   * @see #getTags
   * @see #setTags
   */
  public static final Property tags = newProperty(0, BString.make(""), null);
  
  /**
   * Get the {@code tags} property.
   * @see #tags
   */
  public String getTags() { return getString(tags); }
  
  /**
   * Set the {@code tags} property.
   * @see #tags
   */
  public void setTags(String v) { setString(tags, v, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BPoint.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

    private BPoint(String name, String metric, String[] tags){
        setPointName(name);
        setMetricId(metric);
        List<String> list = Arrays.asList(tags);
        setTags(
                list.stream().collect(
                        Collectors.joining(",")));
    }

    public static BPoint make(String name, String metric, String[] tags){
        return new BPoint(name, metric, tags);
    }
}
