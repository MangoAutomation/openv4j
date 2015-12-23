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
public enum Property implements Serializable {UNKNOWN("Unknown"),
    DEVICE_TYPE_ID("Device type id"),
    DEVICE("Device"),
    BURNER_STAGE_1("Burner stage 1"),
    BURNER_STAGE_2("Burner stage 2"),
    BURNER_STAGES("Burner stages"),
    BURNER("burner"),
    OUTSIDE("Outside"),
    BOILER_WATER("Boiler water"),
    BOILER_WATER_HYSTERERSIS_LOW_FIX("Boiler water hysteresis fix lower point"),
    BOILER_WATER_HYSTERERSIS_UPPER_FIX("Boiler water hysteresis fix upper (1st) point"),
    BOILER_WATER_HYSTERERSIS_UPPER_VARIABLE("Boiler water hysteresis variable (2nd) point"),
    DHW_CYLINDER("DHW cylinder"),
    DHW_CYLINDER_2("DHW cylinder 2"),
    DHW_PUMP("DHW pump"),
    DHW_RECIRC_PUMP("DWH recirc pump"),
    EXHAUST("Exhaust"),
    INTERNAL_PUMP("Internal pump"),
    THROTTLE_VALVE("Throttle valve"),
    HEATING_RETURN("Heating return"),
    HEATING_SUPPLY("Heating supply"),
    COLLECTOR("Collector"),
    NACHLADEUNTERDRUECKUNG("???de??? Nachladeunterdrückung"),
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNSDAY("Wedsday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday"),
    MIXER("Mixer"),
    SAVE_MODE("save mode"),
    PARTY_MODE("party mode"),
    SLOPE("slope"),
    SHIFT("shift"),
    //TODO to String??
    OPERATING_TYPE("operating type"),ROOM_NORMAL("room normal"),
    ROOM_REDUCED("room reduced"),
    ROOM_PARTY("room party"),
    ROOM_CURRENT("current room"),
    FROST("frost"),
    PUMP("pump"),
    HEATUP_TIME("heatup time"),
    DHW_HEATUP("DHW heatup"),
    HOLIDAY_PROGRAMME("Holiday"),
    C2_00("C2 00"),
    C2_02("C2 02"),
    C2_03("C2 03"),
    C2_04("C2 04"),
    C2_05("C2 05"),
    C2_06("C2 06"),
    C2_13("C2 13"),
    C2_1F("C2 1F"),
    C2_21("C2 21"),
    C2_23("C2 23"),
    C2_24("C2 24"),
    C2_26("C2 26"),
    C2_28("C2 28"),
    C2_29("C2 29"),
    C2_32("C2 32"),
    C2_33("C2 33"),
    //BURNER 2 stages
    C2_10("C2 10"),C2_11("C2 11"),
    C2_12("C2 12"),
    //BURNER Modulated
    C2_15("C2 15"),C2_16("C2 16"),
    C2_17("C2 17"),
    C2_18("C2 18"),
    C2_1A("C2 1A"),
    //DHW
    C2_55("C2 55"),C2_56("C2 56"),
    C2_58("C2 58"),
    C2_59("C2 59"),
    C2_60("C2 60"),
    C2_61("C2 61"),
    C2_62("C2 62"),
    C2_64("C2 64"),
    C2_66("C2 66"),
    C2_70("C2 70"),
    C2_71("C2 71"),
    C2_72("C2 72"),
    C2_73("C2 73"),
    C2_74("C2 74"),
    C2_75("C2 75"),
    //COMMON
    C2_7F("C2 7F"),C2_80("C2 80"),
    C2_81("C2 81"),
    C2_82("C2 82"),
    C2_83("C2 83"),
    C2_84("C2 84"),
    C2_85("C2 85"),
    C2_86("C2 86"),
    C2_87("C2 87"),
    C2_89("C2 89"),
    C2_8A("C2 8A"),
    C2_8E("C2 8E"),
    C2_90("C2 90"),
    C2_93("C2 93"),
    C2_94("C2 94"),
    C2_95("C2 95"),
    //MIXER
    C2_A0("C2 A0"),C2_A2("C2 A2"),
    C2_A3("C2 A3"),
    C2_A4("C2 A4"),
    C2_A5("C2 A5"),
    C2_A6("C2 A6"),
    C2_A7("C2 A7"),
    C2_A9("C2 A9"),
    C2_B0("C2 B0"),
    C2_B1("C2 B1"),
    C2_B2("C2 B2"),
    C2_B3("C2 B3"),
    C2_B5("C2 B5"),
    C2_B7("C2 B7"),
    C2_B8("C2 B8"),
    C2_B9("C2 B9"),
    C2_C0("C2 C0"),
    C2_C1("C2 C1"),
    C2_C2("C2 C2"),
    C2_C4("C2 C4"),
    C2_C5("C2 C5"),
    C2_C6("C2 C6"),
    C2_C7("C2 C7"),
    C2_C8("C2 C8"),
    C2_C9("C2 C9"),
    C2_E1("C2 E1"),
    C2_E2("C2 E2"),
    C2_E5("C2 E5"),
    C2_E6("C2 E6"),
    C2_E7("C2 E7"),
    C2_E8("C2 E8"),
    C2_E9("C2 E9"),
    C2_F0("C2 F0"),
    C2_F1("C2 F1"),
    C2_F2("C2 F2"),
    ERROR_1("Error 1"),
    ERROR_2("Error 2"),
    ERROR_3("Error 3"),
    ERROR_4("Error 4"),
    ERROR_5("Error 5"),
    ERROR_6("Error 6"),
    ERROR_7("Error 7"),
    ERROR_8("Error 8"),
    ERROR_9("Error 9"),
    ERROR_10("Error 10"),
    STATUS_ERROR("Stat error");

    final String label;

    private Property(String label) {
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
