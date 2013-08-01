package com.mcintyret.utils;

import com.google.common.collect.ImmutableMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

/**
 * User: mcintyret2
 * Date: 15/07/2013
 */
public class Scripting {

    public static void main(String[] args) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();

        Map<String, Integer> map = ImmutableMap.of("foo", 1, "bar", 2);

        ScriptEngine engine = manager.getEngineByName("JavaScript");
        engine.put("map", map);

        engine.eval("print(map.entrySet())");
    }
}
