#include <QZXing.h>
#include <QDebug>
#include "qqrencode/qqrencode.h"

int main(int /*argc*/, char */*argv[]*/)
{
    QString outputPath("/home/btian/qr.png");
//    zxing.setDecoder( QZXing::DecoderFormat_QR_CODE);
//    auto img = zxing.encodeData("https://www.baidu.com/");
//    img.save(outputPath);

    QQREncode encode;
    encode.encode("https://www.baidu.com/", true);
    auto img = encode.toQImage();
    img.save(outputPath);

    QImage readImage(outputPath);
    QZXing zxing;
    auto str = zxing.decodeImage(readImage);
    qWarning()<<str;

    return 0;
}

