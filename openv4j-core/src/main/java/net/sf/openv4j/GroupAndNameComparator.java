/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.openv4j;

import java.util.Comparator;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class GroupAndNameComparator implements Comparator<DataPoint> {
    /**
     * Creates a new GroupAndNameComparator object.
     */
    public GroupAndNameComparator() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param o1 DOCUMENT ME!
     * @param o2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public int compare(DataPoint o1, DataPoint o2) {
        int result = o1.getGroup().compareTo(o2.getGroup());

        if (result != 0) {
            return result;
        }

        result = o1.getValueKind().compareTo(o2.getValueKind());

        if (result != 0) {
            return result;
        }

        result = o1.getValue().compareTo(o2.getValue());

        return result;
    }
}
