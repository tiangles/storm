package com.tiangles.storm.legend;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;

public class LegendFactory {
    public LegendFactory() {

    }

    public static LegendBase createLegend(StormDevice device) {
        LegendBase legend = makeLegend(device);
        if (legend != null) {
            legend.addLeftDevice(makeLegend(StormApp.getStormDB().getDevice(device.getForwardDevice())));
            legend.addRightDevice(makeLegend(StormApp.getStormDB().getDevice(device.getBackwardDevice())));
        }
        return legend;
    }

    private static LegendBase makeLegend(StormDevice device) {
        LegendBase legend = null;
        if (device == null) {
            return null;
        }
        String legendName = device.getLegend().trim();
        if (legendName.equals("电动阀门")) {
            legend = new ElectricValve();
        } else if (legendName.equals("电动风机")) {
            legend = new ElectricValve();
        } else if (legendName.equals("电动调节阀门")) {
            legend = new ElectricValve();
        }

        if(legend != null) {
            legend.setCode(device.getCode());
            legend.setName(device.getName());
        }
        return legend;
    }
}
