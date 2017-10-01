from .models import Device as StormDevice
from .models import Workshop as StormWorkshop
from .models import DeviceLinkInfo
import xlrd
import time


def load_null_blank_cell(sheet, row, col):
    cell = sheet.cell(row, col)
    if cell is None or cell.ctype ==xlrd.XL_CELL_EMPTY or cell.ctype == xlrd.XL_CELL_BLANK:
        raise ValueError('Blank cell, col:%d'%(col))
    return cell.value


def load_cell(sheet, row, col):
    return sheet.cell_value(row, col)


def import_device(sheet, row_index):
    code = load_null_blank_cell(sheet, row_index, 1)
    model = load_cell(sheet, row_index, 7)
    name = load_null_blank_cell(sheet, row_index, 2)
    system = load_cell(sheet, row_index, 16)
    distribution_cabinet = load_cell(sheet, row_index, 22)
    local_control_panel = load_cell(sheet, row_index, 24)
    dcs_cabinet = load_cell(sheet, row_index, 26)
    legend = load_cell(sheet, row_index, 4) + load_cell(sheet, row_index, 3)
    workshop_name = load_cell(sheet, row_index, 15)
    if len(workshop_name)>0:
        workshop = StormWorkshop.objects.get(name=workshop_name)
    else:
        workshop = None

    device = StormDevice.objects.create(code=code,
                         model=model,
                         name=name,
                         system=system,
                         distribution_cabinet=distribution_cabinet,
                         local_control_panel=local_control_panel,
                         dcs_cabinet=dcs_cabinet,
                         legend=legend,
                         workshop=workshop)
    device.save()


def import_workshop(sheet, row_index):
    workshop_index = load_null_blank_cell(sheet, row_index, 1)
    code = load_null_blank_cell(sheet, row_index, 2)
    name = load_null_blank_cell(sheet, row_index, 3)
    workshop = StormWorkshop.objects.create(workshop_index=workshop_index,
                                            code=code,
                                            name=name)
    workshop.save()


def import_device_link_info(sheet, row_index):
    left_device_code = load_null_blank_cell(sheet, row_index, 1)
    left_device = StormDevice.objects.get(code=left_device_code)
    right_device_codes = load_null_blank_cell(sheet, row_index, 20)
    for right_deice_code in right_device_codes.split():
        info = DeviceLinkInfo.objects.filter(left_device__code=left_device_code, right_device__code=right_deice_code)
        if info is not None and len(info)>0:
            raise ValueError('duplicated record with left_device_code:%s right_device_code:%s'% (left_device_code, right_deice_code))

        try:
            right_device = StormDevice.objects.get(code=right_deice_code)
        except Exception:
            raise ValueError('can not find specified device with code:%s'%right_deice_code)
        info = DeviceLinkInfo.objects.create(left_device=left_device,
                                             right_device=right_device)
        info.save()


def do_import_data(file_path, row_offset, load_func):
    begin_time = time.time()
    imported_count = 0

    with xlrd.open_workbook(file_path) as file_data:
        sheet = file_data.sheet_by_index(0)
        nrows = sheet.nrows
        for row_index in range(row_offset, nrows):
            try:
                load_func(sheet, row_index)
                imported_count += 1
            except Exception as e:
                print('Import error, row: %d, msg: "%s"' % (row_index, e))
    print ('Done, imported %d entries from %d rows, used time: %d seconds' %
           (imported_count, nrows-row_offset, time.time()-begin_time))


def import_device_data(file_path):
    # do_import_data(file_path, 2, import_device)
    do_import_data(file_path, 2, import_device_link_info)


def import_workshop_data(file_path):
    do_import_data(file_path, 1, import_workshop)


if __name__ == '__main__':
    import_workshop_data('/home/btian/workshop.xlsx', )
    import_device_data('/home/btian/device.xlsx')