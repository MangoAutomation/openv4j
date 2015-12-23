/*
 * Copyright 2009, openv4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 * $Id: $
 *
 * @author arnep
 */
package net.sf.openv4j;

import java.io.Serializable;

/**
 * 
 */
public enum PropertyType implements Serializable {STATE("state"),
    TEMP_ACTUAL("temp (a)[°C]"),
    TEMP_NOMINAL("temp (n)[°C]"),
    TEMP_LOW_PASS("temp (lp)[°C]"),
    TEMP_DAMPED("temp (d)[°C]"),
    TEMP_MAX("temp (max)[°C]"),
    CYCLES("cycles"),
    POWER_IN_PERCENT("power [%]"),
    CONSUPTION("consuption [l|m³]"),
    OPERATING_TIME_S("time [s]"),
    OPERATING_TIME_H("time [h]"),
    ENERGY_KWH("energy [kWh]"),
    CYCLES_HEATING("heating cycles"),
    CYCLES_RECIRC("recirc cycles"),
    TIME_STAMP("time"),
    POSITION_IN_PERCENT("position [%]"),
    CONFIG("config "),
    ERROR_STATE("error"),
    ERROR("error");

    final String label;

    private PropertyType(String label) {
        this.label = label;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLabel() {
        return label;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public String toString() {
        return label;
    }
}
