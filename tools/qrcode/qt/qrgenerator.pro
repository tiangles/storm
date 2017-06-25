TEMPLATE = app
CONFIG += c++11 console
QT += svg

LIBS += -L/home/btian/workspace/storm/tools/qrcode/qrencode -lqrencode
INCLUDEPATH += qqrencode qrencode

SOURCES += src/main.cpp \
    qqrencode/qqrencode.cpp

DEFINES += QT_DEPRECATED_WARNINGS

include(qzxing/QZXing.pri)

HEADERS += \
    qqrencode/libqtqrencode_global.h \
    qqrencode/qqrencode.h \
    qqrencode/qqrencode_p.h
