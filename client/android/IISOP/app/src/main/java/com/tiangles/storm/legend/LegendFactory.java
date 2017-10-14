package com.tiangles.storm.legend;

import android.content.Context;

import com.tiangles.storm.StormApp;
import com.tiangles.storm.database.dao.StormDevice;
import com.tiangles.storm.legend.model.Circle;
import com.tiangles.storm.legend.model.Line;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LegendFactory {
    private static LegendFactory instance;
    private Map<String, DeviceLinkLegend> allLegends = new HashMap<>();
    private LegendFactory() {
        loadLegend(StormApp.getContext());
    }

    public static LegendFactory getInstance(){
        if(instance == null) {
            instance = new LegendFactory();
        }
        return instance;
    }

    public DeviceLink createLegend(StormDevice device) {
        DeviceLink link = null;
        DeviceLinkLegend legend = allLegends.get(device.getLegend().trim());
        if (legend != null) {
            link = new DeviceLink(legend);
            link.setCode(device.getCode());
            link.setName(device.getName());
            for(StormDevice leftDevice: StormApp.getDBManager().getLeftDevice(device)){
                DeviceLink leftLink = createLegend(leftDevice);
                if(leftLink != null){
                    link.addLeftDevice(leftLink);
                }
            }
            for(StormDevice rightDevice: StormApp.getDBManager().getRightDevice(device)){
                DeviceLink rightLink = createLegend(rightDevice);
                if(rightLink != null){
                    link.addRightDevice(rightLink);
                }
            }
        }
        return link;
    }

    private void loadLegend(Context context){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream is = context.getAssets().open("device_legend.xml");
            Document doc = builder.parse(is);
            Element rootElement = doc.getDocumentElement();
            NodeList items = rootElement.getChildNodes();
            for(int i=0; i<items.getLength(); ++i) {
                Node item = items.item(i);
                if(item.getNodeType()==Node.ELEMENT_NODE) {
                    createLegnedFromNode(item);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private void createLegnedFromNode(Node item) {
        String type=item.getAttributes().getNamedItem("name").getNodeValue();
        DeviceLinkLegend legend = new DeviceLinkLegend();
        for(Node node=item.getFirstChild();node!=null;node=node.getNextSibling()) {
            if(node.getNodeType()==Node.ELEMENT_NODE) {
                if(node.getNodeName().equals("meta")) {
                    loadMeta(legend, node);
                } else if(node.getNodeName().equals("draw")) {
                    loadDraw(legend, node);
                }
            }
        }
        allLegends.put(type, legend);
    }

    private void loadMeta(DeviceLinkLegend legend, Node item){
        for(Node node=item.getFirstChild();node!=null;node=node.getNextSibling()) {
            if(node.getNodeType()==Node.ELEMENT_NODE && node != null) {
                if(node.getNodeName().equals("offset")) {
                    String x=node.getAttributes().getNamedItem("x").getNodeValue();
                    String y=node.getAttributes().getNamedItem("y").getNodeValue();
                } else if(node.getNodeName().equals("size")) {
                    String width=node.getAttributes().getNamedItem("width").getNodeValue();
                    String height=node.getAttributes().getNamedItem("height").getNodeValue();
                } else if(node.getNodeName().equals("base_length")) {
                    String baseLength=node.getFirstChild().getNodeValue();
                    legend.baseLength = Float.valueOf(baseLength);
                } else if(node.getNodeName().equals("name_offset")) {
                    String nameOffset = node.getFirstChild().getNodeValue();
                    legend.nameOffset = Integer.valueOf(nameOffset);
                } else if(node.getNodeName().equals("code_offset")) {
                    String codeOffset = node.getFirstChild().getNodeValue();
                    legend.codeOffset = Integer.valueOf(codeOffset);
                }
            }
        }
    }

    private void loadDraw(DeviceLinkLegend legend, Node item){
        for(Node node=item.getFirstChild();node!=null;node=node.getNextSibling()) {
            if (node.getNodeType() == Node.ELEMENT_NODE && node != null) {
                if(node.getNodeName().equals("line")) {
                    String x1=node.getAttributes().getNamedItem("x1").getNodeValue();
                    String y1=node.getAttributes().getNamedItem("y1").getNodeValue();
                    String x2=node.getAttributes().getNamedItem("x2").getNodeValue();
                    String y2=node.getAttributes().getNamedItem("y2").getNodeValue();
                    legend.lines.add(new Line(Integer.parseInt(x1),
                            Integer.parseInt(y1),
                            Integer.parseInt(x2),
                            Integer.parseInt(y2)));
                } else if(node.getNodeName().equals("circle")) {
                    String cx=node.getAttributes().getNamedItem("cx").getNodeValue();
                    String cy=node.getAttributes().getNamedItem("cy").getNodeValue();
                    String r=node.getAttributes().getNamedItem("r").getNodeValue();
                    legend.circles.add(new Circle(Integer.parseInt(cx),
                            Integer.parseInt(cy),
                            Integer.parseInt(r)));
                } else if(node.getNodeName().equals("text")) {

                }
            }
        }
    }
}
