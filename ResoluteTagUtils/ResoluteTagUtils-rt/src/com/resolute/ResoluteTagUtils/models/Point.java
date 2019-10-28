package com.resolute.ResoluteTagUtils.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Point {

    private String name, metricId;
    private String[] tags;

    public String getMetricId() {
        return metricId;
    }
    public String getName() {
        return name;
    }

    public String[] getTags() {
        return tags;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public Point as(Point pointClass){
        return pointClass;
    }

/*    @Override             PRODUCES STACK-OVERFLOW / OUT-OF-MEMORY ERROR
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        return prime * result + (this.hashCode());
    }*/

    /***
     * override super-class's equals method
     * @return
     */
/*    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        else
            return false;
    }*/

    /***
     * override super-class's toString method
     * @return
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ".concat(getName()));
        sb.append("Metric Id: ".concat(getMetricId()));
        sb.append(Stream.of(getTags())
                .filter(Objects::nonNull)
                .collect(Collectors
                        .joining(",")));
        return sb.toString();
    }

/***
 * returns a json string of the point object
 * @return
 */
    public String toJson(){
        try{
            return AccessController.doPrivileged((PrivilegedAction<String>)() -> {
                Gson gson = new GsonBuilder().setPrettyPrinting()
                                             .serializeNulls()
                                             .create();
                return gson.toJson(this.getClass());
            });
        }catch(Exception e){
            e.printStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append("Name: ".concat(getName()));
            sb.append("Metric Id: ".concat(getMetricId()));
            sb.append(Stream.of(getTags())
                            .filter(Objects::nonNull)
                            .collect(Collectors
                            .joining(",")));
            return sb.toString();
        }
    }
}
