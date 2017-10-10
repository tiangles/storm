package com.tiangles.storm.panel;

/**
 * Created by btian on 10/5/17.
 */

public class Panel {
    private String code;
    private String name;
    private String controller;
    private String fSurface;
    private String bSurface;

    public Panel(){
        setCode("10CGB01");
        setName("#1机组DPU31控制柜");
        setController("DPU31");
        setfSurface("F面:0、1、2、3、4、5、6、7、8、9");
        setbSurface("B面:0、1、2、3、4、5、6、7、8、9、10、11");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getfSurface() {
        return fSurface;
    }

    public void setfSurface(String fSurface) {
        this.fSurface = fSurface;
    }

    public String getbSurface() {
        return bSurface;
    }

    public void setbSurface(String bSurface) {
        this.bSurface = bSurface;
    }
}
