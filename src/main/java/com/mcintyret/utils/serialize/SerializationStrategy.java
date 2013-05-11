package com.mcintyret.utils.serialize;

/**
 * User: mcintyret2
 * Date: 10/05/2013
 */
public enum SerializationStrategy {

    JSON(".json"), JAVA(".ser"), HESSIAN(".hessian");

    private String suffix;

    private SerializationStrategy(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
