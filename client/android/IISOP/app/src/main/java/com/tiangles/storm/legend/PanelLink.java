package com.tiangles.storm.legend;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class PanelLink {
    class SignalPanel{
        class Channel {
            class Terminal{
                Terminal(String name){
                    this.name = name;
                }
                String name;
                int linkPointY;
                int linkPointLeftX;
                int linkPointRightX;
            }
            String name = "1";
            Terminal[] terminals = {new Terminal("1"),
                    new Terminal("2"),
                    new Terminal("3")};
            public Channel(String name){
                this.name = name;
            }
        }
        String name;
        List<Channel> channels;
        SignalPanel(String name){
            this.name = name;
            channels = new ArrayList<>(2);
            channels.add(new Channel("通道1"));
            channels.add(new Channel("通道2"));
        }
    }
    class SignalCable {
        class CableWire{
            class Plug{
                SignalPanel.Channel.Terminal terminal;
                public Plug(SignalPanel.Channel.Terminal terminal){
                    this.terminal = terminal;
                }
            }
            String name;
            Plug leftPlug;
            Plug rightPlug;

            public CableWire(SignalPanel leftPanel, int leftChannelIndex, int leftTerminalIndex,
                             SignalPanel rightPanel, int rightChannelIndex, int rightTerminalIndex,
                             String name){
                this.name = name;
                leftPlug = new Plug(leftPanel.channels.get(leftChannelIndex).terminals[leftTerminalIndex]);
                rightPlug = new Plug(rightPanel.channels.get(rightChannelIndex).terminals[rightTerminalIndex]);
            }
        }

        private String name;
        private List<CableWire> wires;

        SignalCable(SignalPanel leftPanel, SignalPanel rightPanel, String name){
            this.name = name;
            wires = new ArrayList<>(2);
            wires.add(new CableWire(leftPanel, 0, 0, rightPanel, 0, 0, "HNC10AA101GT+"));
            wires.add(new CableWire(leftPanel, 0, 1, rightPanel, 0, 2, "HNC10AA101GT-"));
        }
    }

    private SignalPanel leftPanel;
    private SignalPanel rightPanel;
    private SignalCable cable;

    protected Rect rect;

    public PanelLink(){
        leftPanel = new SignalPanel("CBB05柜 F面 0号板 8通道");
        rightPanel = new SignalPanel("接线盒GL069J");
        cable = new SignalCable(leftPanel, rightPanel, "CBB054006\nZRJ-4X2X1.0");
    }

    public void setRect(Rect rect){
        this.rect = rect;
    }
    public void draw(Canvas canvas, Paint paint){
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(0, 0, 255));
        paint.setStrokeWidth(2);
        drawPanel(leftPanel,  new Rect(rect.left,                 rect.top, rect.left+rect.width()/4,  rect.bottom), canvas, paint);
        drawPanel(rightPanel, new Rect(rect.right-rect.width()/4, rect.top, rect.right,                rect.bottom), canvas, paint);
        drawCable(cable,      new Rect(rect.left+rect.width()/4,  rect.top, rect.right-rect.width()/4, rect.bottom), canvas, paint);
    }

    private static int PADDING = 5;
    private static int CELL_HEIGHT = 50;
    private static int FONT_SIZE = 25;
    private void drawPanel(SignalPanel panel, Rect rect, Canvas canvas, Paint paint){
        int left  = rect.left + PADDING;
        int top = rect.top + PADDING;
        int right = rect.right-PADDING;

        int heightOffset = 0;
        float[] pts = {
                left, top+CELL_HEIGHT*heightOffset, right, top+CELL_HEIGHT*(heightOffset++),
                left, top+CELL_HEIGHT*heightOffset, right, top+CELL_HEIGHT*(heightOffset++),
                left, top+CELL_HEIGHT*heightOffset, right, top+CELL_HEIGHT*(heightOffset),
        };
        canvas.drawLines(pts, paint);
        drawText(panel.name, left, top, right-left, CELL_HEIGHT, canvas, paint);
        drawText("端子号", left, top+CELL_HEIGHT, right-left, CELL_HEIGHT, canvas, paint);
        for(SignalPanel.Channel channel: panel.channels){
            drawChannel(channel, left, top+CELL_HEIGHT*heightOffset, right, canvas, paint);
            heightOffset += channel.terminals.length;
        }

        float[] pts2 = {
                left, top+CELL_HEIGHT*heightOffset, right, top+CELL_HEIGHT*(heightOffset),
                left, top, left, top+CELL_HEIGHT*heightOffset,
                right, top, right, top+CELL_HEIGHT*heightOffset,
        };
        canvas.drawLines(pts2, paint);
    }

    private void drawChannel(SignalPanel.Channel channel, int left, int top, int right, Canvas canvas, Paint paint){
        int heightOffset = 0;
        for(SignalPanel.Channel.Terminal terminal: channel.terminals){
            drawText(terminal.name, (left+right)/2, top+CELL_HEIGHT*heightOffset, (right-left)/2, CELL_HEIGHT, canvas, paint);
            canvas.drawLine((left+right)/2, top+CELL_HEIGHT*heightOffset, right, top+CELL_HEIGHT*(heightOffset++), paint);
            terminal.linkPointY = top+CELL_HEIGHT*heightOffset - CELL_HEIGHT/2;
            terminal.linkPointLeftX = left;
            terminal.linkPointRightX = right;
        }
        canvas.drawLine(left, top+CELL_HEIGHT*heightOffset, right, top+CELL_HEIGHT*heightOffset, paint);
        canvas.drawLine((left+right)/2, top, (left+right)/2, top+CELL_HEIGHT*heightOffset, paint);
        drawText(channel.name, left, top, (right-left)/2, CELL_HEIGHT*channel.terminals.length, canvas, paint);
    }

    private static int LINK_DIRECTION_LEFT = 0;
    private static int LINK_DIRECTION_RIGHT = 1;
    private void drawCable(SignalCable cable, Rect rect, Canvas canvas, Paint paint){
        int minLinkPointLeftY = 10000;
        int maxLinkPointLeftY = -1;
        int linkPointLeftX = 0;
        int minLinkPointRightY = 10000;
        int maxLinkPointRightY = -1;
        int linkPointRightX = 0;
        for(SignalCable.CableWire wire: cable.wires){
            drawPlug(wire.leftPlug, rect, LINK_DIRECTION_LEFT, canvas, paint);
            drawPlug(wire.rightPlug, rect, LINK_DIRECTION_RIGHT, canvas, paint);

            minLinkPointLeftY = Math.min(minLinkPointLeftY, wire.leftPlug.terminal.linkPointY);
            maxLinkPointLeftY = Math.max(maxLinkPointLeftY, wire.leftPlug.terminal.linkPointY);
            linkPointLeftX = wire.leftPlug.terminal.linkPointRightX+rect.width()/3;

            minLinkPointRightY = Math.min(minLinkPointRightY, wire.rightPlug.terminal.linkPointY);
            maxLinkPointRightY = Math.max(maxLinkPointRightY, wire.rightPlug.terminal.linkPointY);
            linkPointRightX = wire.rightPlug.terminal.linkPointLeftX-rect.width()/3;
        }
        canvas.drawLine(linkPointLeftX, minLinkPointLeftY, linkPointLeftX, maxLinkPointLeftY, paint);
        canvas.drawLine(linkPointRightX, minLinkPointRightY, linkPointRightX, maxLinkPointRightY, paint);
        canvas.drawLine(linkPointLeftX, (minLinkPointRightY + maxLinkPointRightY)/2, linkPointRightX, (minLinkPointRightY + maxLinkPointRightY)/2, paint);
    }

    private void drawPlug(SignalCable.CableWire.Plug plug, Rect rect, int direction, Canvas canvas, Paint paint){
        SignalPanel.Channel.Terminal terminal = plug.terminal;
        if(direction == LINK_DIRECTION_LEFT) {
            canvas.drawLine(terminal.linkPointRightX, terminal.linkPointY, terminal.linkPointRightX+rect.width()/3, terminal.linkPointY, paint);
        } else {
            canvas.drawLine(terminal.linkPointLeftX, terminal.linkPointY, terminal.linkPointLeftX-rect.width()/3, terminal.linkPointY, paint);
        }
    }

    private void drawText(String str, int left, int top, int width, int height, Canvas canvas, Paint paint){
        paint.setTextSize(FONT_SIZE);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (fontMetrics.bottom - fontMetrics.top) / 2 ;

        Rect bounds = new Rect();
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str,
                left + (width-bounds.width() + bounds.left)/2,
                top+(height-bounds.height())/2 + baseline,
                paint);
    }
}
