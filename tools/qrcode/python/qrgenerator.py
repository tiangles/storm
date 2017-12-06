import qrcode
import os


def generate_text_im(text):
    f = pygame.font.Font('/usr/share/fonts/truetype/wqy/wqy-microhei.ttc', 16)
    rtext = f.render(text, True, (0, 0, 0), (255, 255, 255))
    sio = StringIO.StringIO()
    pygame.image.save(rtext, sio)
    sio.seek(0)

    img = Image.open(sio)
    return img


def generate_qr_img(text):
    qr = qrcode.QRCode(
        version=1,
        error_correction=qrcode.constants.ERROR_CORRECT_H,
        box_size=10,
        border=4,
    )
    qr.add_data(text)
    qr.make(fit=True)
    img = qr.make_image()
    return img


def merge_img(img1, img2, img3):
    (w1, h1) = (img1.size[0], img1.size[1])
    (w2, h2) = (img2.size[0], img2.size[1])
    (w3, h3) = (img3.size[0], img3.size[1])
    (w,h) = (max(w1, w2, w3), h1+h2+h3+10)

    img = Image.new('RGB', (w, h), 'white')

    box = ((w-w1)/2, 0, (w+w1)/2, h1)
    img.paste(img1, box)

    box = ((w-w2)/2, h1, (w+w2)/2, h1+h2)
    img.paste(img2, box)

    box = ((w-w3)/2, h1+h2, (w+w3)/2, h1+h2+h3)
    img.paste(img3, box)

    return img



def column(name):
    return ord(name) - ord('A')


def load_null_blank_cell(sheet, row, col):
    cell = sheet.cell(row, col)
    if cell is None or cell.ctype ==xlrd.XL_CELL_EMPTY or cell.ctype == xlrd.XL_CELL_BLANK:
        raise ValueError('Blank cell, col:%s'%(chr(col+ord('A'))))
    return cell.value


def load_cell(sheet, row, col):
    return sheet.cell_value(row, col)


def load_int_cell(sheet, row, col):
    cell = sheet.cell(row, col)
    if cell is not None and cell.ctype == xlrd.XL_CELL_NUMBER:
        return int(cell.value)
    return None


def load_null_blank_int_cell(sheet, row, col):
    cell = sheet.cell(row, col)
    if cell is None or cell.ctype ==xlrd.XL_CELL_EMPTY or cell.ctype == xlrd.XL_CELL_BLANK:
        raise ValueError('Blank cell, col:%s' % (chr(col + ord('A'))))
    return int(cell.value)


def load_boolean_cell(sheet, row, col):
    cell = sheet.cell(row, col)
    if cell is None or cell.ctype ==xlrd.XL_CELL_EMPTY or cell.ctype == xlrd.XL_CELL_BLANK:
        return False
    return True


def generate_qrcode_for_file(file_path, sheet_index, row_offset, load_func):
    begin_time = time.time()
    imported_count = 0

    with xlrd.open_workbook(file_path) as file_data:
        sheet = file_data.sheet_by_index(sheet_index)
        nrows = sheet.nrows
        for row_index in range(row_offset, nrows):
            try:
                code = load_null_blank_cell(sheet, row_index, column('B'))
                name = load_cell(sheet, row_index, column('C'))

                code_img = generate_text_im(code)
                name_img = generate_text_im(name)
                qr_img = generate_qr_img(code)

                img = merge_img(qr_img, name_img, code_img)
                img.save('/home/btian/workspaces/storm/qrcodes/%s-%s.png', (code, name))

            except Exception as e:
                print e


if __name__ == '__main__':
    generate_qrcode_for_file('/media/btian/workspace/Storm_Doc/V3.0/01-devices-1.xlsx', 0, 2)
