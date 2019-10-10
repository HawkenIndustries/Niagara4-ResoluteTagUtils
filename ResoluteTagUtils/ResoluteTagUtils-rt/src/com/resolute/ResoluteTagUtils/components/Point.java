package com.resolute.ResoluteTagUtils.components;

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
}
