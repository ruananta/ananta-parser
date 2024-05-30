package org.ruananta.parser.entity;

import java.util.TreeSet;

public class ValueMover {
    public static <T extends Identifiable> boolean canMoveUp(T t, TreeSet<T> treeSet) {
        return treeSet.lower(t) != null;
    }

    public static <T extends Identifiable> boolean canMoveDown(T t, TreeSet<T> treeSet) {
        return treeSet.higher(t) != null;
    }

    public static <T extends Identifiable> void moveUp(T t, TreeSet<T> treeSet) {
        if (t == null || treeSet == null) {
            throw new NullPointerException("Can't move up");
        }
        T up = treeSet.lower(t);
        if (up == null) {
            throw new RuntimeException("Can't move up");
        }
        Long temp = up.getId();
        if (temp == null) {
            throw new NullPointerException("Identifiable id is null");
        }
        up.setId(t.getId());
        t.setId(temp);
    }

    public static <T extends Identifiable> void moveDown(T t, TreeSet<T> treeSet) {
        if (t == null || treeSet == null) {
            throw new NullPointerException("Can't move down");
        }
        T down = treeSet.higher(t);
        if (down == null) {
            throw new RuntimeException("Can't move down");
        }
        Long temp = down.getId();
        if (temp == null) {
            throw new NullPointerException("Identifiable id is null");
        }
        down.setId(t.getId());
        t.setId(temp);
    }

}
