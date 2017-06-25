import qrcode
import os

if __name__ == '__main__':
    BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

    s = 'https://www.baidu.com/'
    img = qrcode.make(s)
    img.save(BASE_DIR+'/qr.png')
