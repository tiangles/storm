from .models import Device as StormDevice
from .models import Workshop as StormWorkshop
from .models import DeviceLinkInfo
from .models import DeviceSignal
import xlrd
import time


def column(name):
    return ord(name) - ord('A')


def load_null_blank_cell(sheet, row, col):
    cell = sheet.cell(row, col)
    if cell is None or cell.ctype ==xlrd.XL_CELL_EMPTY or cell.ctype == xlrd.XL_CELL_BLANK:
        raise ValueError('Blank cell, col:%s'%(chr(col+ord('A'))))
    return cell.value


def load_cell(sheet, row, col):
    return sheet.cell_value(row, col)


def import_device(sheet, row_index):
    code = load_null_blank_cell(sheet, row_index, column('B'))
    model = load_cell(sheet, row_index, column('H'))
    name = load_null_blank_cell(sheet, row_index, column('C'))
    system = load_cell(sheet, row_index, column('K'))
    distribution_cabinet = ''
    local_control_panel = ''
    dcs_cabinet = ''
    legend = load_cell(sheet, row_index, column('E')) + load_cell(sheet, row_index, column('D'))
    workshop_name = load_cell(sheet, row_index, column('J'))
    if len(workshop_name)>0:
        workshop = StormWorkshop.objects.get(name=workshop_name)
    else:
        workshop = None

    StormDevice.objects.get_or_create(code=code,
                         model=model,
                         name=name,
                         system=system,
                         distribution_cabinet=distribution_cabinet,
                         local_control_panel=local_control_panel,
                         dcs_cabinet=dcs_cabinet,
                         legend=legend,
                         workshop=workshop)


def import_workshop(sheet, row_index):
    workshop_index = load_null_blank_cell(sheet, row_index, column('A'))
    code = load_null_blank_cell(sheet, row_index, column('B'))
    name = load_null_blank_cell(sheet, row_index, column('C'))
    StormWorkshop.objects.get_or_create(workshop_index=workshop_index,
                                            code=code,
                                            name=name)

def import_signal(sheet, row_index):
    code = load_cell(sheet, row_index, column('C'))
    figure_number = load_cell(sheet, row_index, column('B'))
    for_device_code = load_cell(sheet, row_index, column('D'))
    for_device = StormDevice.objects.get(code=for_device_code)
    name = load_cell(sheet, row_index, column('E'))
    io_type = load_cell(sheet, row_index, column('F'))
    signal_type = load_cell(sheet, row_index, column('G'))
    remark = load_cell(sheet, row_index, column('H'))
    power_supply = load_cell(sheet, row_index, column('I'))
    connect_to_system = load_cell(sheet, row_index, column('J'))
    status_when_io_is_1 = load_cell(sheet, row_index, column('K'))
    status_when_io_is_0 = load_cell(sheet, row_index, column('L'))
    interface_type = load_cell(sheet, row_index, column('M'))
    control_signal_type = load_cell(sheet, row_index, column('N'))
    incident_record = load_cell(sheet, row_index, column('O'))
    DeviceSignal.objects.update_or_create(code=code,
                                          figure_number=figure_number,
                                          for_device=for_device,
                                          name=name,
                                          io_type=io_type,
                                          signal_type=signal_type,
                                          remark=remark,
                                          power_supply=power_supply,
                                          connect_to_system=connect_to_system,
                                          status_when_io_is_1=status_when_io_is_1,
                                          status_when_io_is_0=status_when_io_is_0,
                                          interface_type=interface_type,
                                          control_signal_type=control_signal_type,
                                          incident_record=incident_record)


def import_device_link_info(sheet, row_index):
    left_device_code = load_null_blank_cell(sheet, row_index, column('B'))
    left_device = StormDevice.objects.get(code=left_device_code)
    right_device_codes = load_null_blank_cell(sheet, row_index, column('M'))
    for right_deice_code in right_device_codes.split():
        info = DeviceLinkInfo.objects.filter(left_device__code=left_device_code, right_device__code=right_deice_code)
        if info is not None and len(info)>0:
            raise ValueError('duplicated record with left_device_code:%s right_device_code:%s'% (left_device_code, right_deice_code))

        try:
            right_device = StormDevice.objects.get(code=right_deice_code)
        except Exception:
            raise ValueError('can not find specified device with code:%s'%right_deice_code)
        DeviceLinkInfo.objects.get_or_create(left_device=left_device,
                                             right_device=right_device)


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
    do_import_data(file_path, 2, import_device)


def import_device_link_info_data(file_path):
    do_import_data(file_path, 2, import_device_link_info)


def import_workshop_data(file_path):
    do_import_data(file_path, 1, import_workshop)


def import_signal_data(file_path):
    do_import_data(file_path, 1, import_signal)

