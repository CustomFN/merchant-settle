package com.z.merchantsettle.utils;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;

public class DiffUtil {

    public static <T> String diff(T o1, T o2) {
        DiffNode diff = ObjectDifferBuilder.buildDefault().compare(o1, o2);

        StringBuilder sb = new StringBuilder();
        diff.visit(new DiffNode.Visitor() {
            public void node(DiffNode node, Visit visit) {
                if (!"/".equals(node.getPath().toString())) {
                    Object baseValue = node.canonicalGet(o1);
                    baseValue = baseValue == null ? "" : baseValue;
                    Object workingValue = node.canonicalGet(o2);
                    workingValue = workingValue == null ? "" : workingValue;
                    String message = node.getPath() + " \t:" +
                            baseValue + " => " + workingValue;
                    sb.append(message).append("\r\n");
                }
            }
        });

        return sb.toString();
    }
}
