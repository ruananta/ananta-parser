package org.ruananta.parser.entity;

import java.util.Comparator;

public class IdentifiableComparator implements Comparator<Identifiable> {
    @Override
    public int compare(Identifiable i1, Identifiable i2) {
        if (i1 == null && i2 == null) return 0;
        if (i1 == null) return 1;
        if (i2 == null) return -1;
        return i1.getId().compareTo(i2.getId());
    }
}
