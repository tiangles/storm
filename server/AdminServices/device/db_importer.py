# -*- coding: utf-8 -*-
from .models import Workshop as StormWorkshop
from .models import Device as StormDevice, PowerDevice, DeviceLinkInfo
from .models import DCSCabinet, DCSConnection, DeviceDioSignal, DeviceAioSignal
from .models import LocalControlCabinet, LocalControlCabinetConnection, LocalControlCabinetTerminal
from staff.models import DatabaseMeta
import xlrd
import time
import os


def column(name):
    return ord(name) - ord('A')


def load_null_blank_cell(sheet, row, col):
    cell = sheet.cell(row, col)
    if cell is None or cell.ctype == xlrd.XL_CELL_EMPTY or cell.ctype == xlrd.XL_CELL_BLANK:
        raise ValueError('Blank cell, col:%s' % (chr(col+ord('A')), ))
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
    if cell is None or cell.ctype == xlrd.XL_CELL_EMPTY or cell.ctype == xlrd.XL_CELL_BLANK:
        raise ValueError('Blank cell, col:%s' % (chr(col + ord('A'))))
    return int(cell.value)


def load_boolean_cell(sheet, row, col):
    cell = sheet.cell(row, col)
    if cell is None or cell.ctype == xlrd.XL_CELL_EMPTY or cell.ctype == xlrd.XL_CELL_BLANK:
        return False
    return True


def import_device(sheet, row_index):
    code = load_null_blank_cell(sheet, row_index, column('B'))
    name = load_cell(sheet, row_index, column('C'))
    device_type = load_cell(sheet, row_index, column('D'))
    driver_type = load_cell(sheet, row_index, column('E'))
    power_circuit_voltage = load_cell(sheet, row_index, column('F'))
    control_circuit_voltage = load_cell(sheet, row_index, column('G'))

    model = load_cell(sheet, row_index, column('H'))
    maintenance_record = load_cell(sheet, row_index, column('I'))

    workshop_name = load_cell(sheet, row_index, column('J'))
    if len(workshop_name) > 0:
        workshop = StormWorkshop.objects.get(name=workshop_name)
    else:
        workshop = None

    system = load_cell(sheet, row_index, column('K'))

    power_device_code = load_cell(sheet, row_index, column('O'))
    power_device_name = load_cell(sheet, row_index, column('P'))
    if power_device_code is not None and len(power_device_code) > 0:
        power_device, created = PowerDevice.objects.update_or_create(code=power_device_code,
                                                                     name=power_device_name)
    else:
        power_device = None

    StormDevice.objects.update_or_create(code=code,
                                         name=name,
                                         type=device_type,
                                         driver_type=driver_type,
                                         power_circuit_voltage=power_circuit_voltage,
                                         control_circuit_voltage=control_circuit_voltage,
                                         model=model,
                                         maintenance_record=maintenance_record,
                                         workshop=workshop,
                                         system=system,
                                         power_device=power_device)


def import_cabinet(sheet, row_index):
    code = load_null_blank_cell(sheet, row_index, column('B'))
    usage = load_cell(sheet, row_index, column('C'))
    dcs_controller = load_cell(sheet, row_index, column('D'))
    specification = load_cell(sheet, row_index, column('F'))
    maintenance_record = load_cell(sheet, row_index, column('G'))
    workshop_code = load_cell(sheet, row_index, column('H'))
    if len(workshop_code) > 0:
        workshop = StormWorkshop.objects.get(code=workshop_code)
    else:
        workshop = None
    remark = load_cell(sheet, row_index, column('G'))
    DCSCabinet.objects.update_or_create(code=code,
                                        usage=usage,
                                        dcs_controller=dcs_controller,
                                        specification=specification,
                                        maintenance_record=maintenance_record,
                                        workshop=workshop,
                                        remark=remark)


def import_workshop(sheet, row_index):
    workshop_index = load_null_blank_cell(sheet, row_index, column('A'))
    code = load_null_blank_cell(sheet, row_index, column('B'))
    name = load_null_blank_cell(sheet, row_index, column('C'))
    StormWorkshop.objects.update_or_create(workshop_index=workshop_index,
                                           code=code,
                                           name=name)


def import_dio_signal(sheet, row_index):
    code = load_null_blank_cell(sheet, row_index, column('C'))
    figure_number = load_cell(sheet, row_index, column('B'))
    for_device_code = load_null_blank_cell(sheet, row_index, column('D'))
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
    DeviceDioSignal.objects.update_or_create(code=code,
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


def import_aio_signal(sheet, row_index):
    code = load_null_blank_cell(sheet, row_index, column('C'))
    figure_number = load_cell(sheet, row_index, column('B'))
    for_device_code = load_null_blank_cell(sheet, row_index, column('D'))
    for_device = StormDevice.objects.get(code=for_device_code)
    name = load_cell(sheet, row_index, column('E'))
    io_type = load_cell(sheet, row_index, column('F'))
    signal_type = load_cell(sheet, row_index, column('G'))
    isolation = load_cell(sheet, row_index, column('H'))
    unit = load_cell(sheet, row_index, column('I'))
    min_range = load_cell(sheet, row_index, column('J'))
    max_range = load_cell(sheet, row_index, column('K'))
    remark = load_cell(sheet, row_index, column('L'))
    power_supply = load_cell(sheet, row_index, column('M'))
    connect_to_system = load_cell(sheet, row_index, column('N'))
    lll = load_boolean_cell(sheet, row_index, column('O'))
    ll = load_boolean_cell(sheet, row_index, column('P'))
    l = load_boolean_cell(sheet, row_index, column('Q'))
    h = load_boolean_cell(sheet, row_index, column('R'))
    hh = load_boolean_cell(sheet, row_index, column('S'))
    hhh = load_boolean_cell(sheet, row_index, column('T'))
    tendency = load_boolean_cell(sheet, row_index, column('U'))
    DeviceAioSignal.objects.update_or_create(code=code,
                                             figure_number=figure_number,
                                             for_device=for_device,
                                             name=name,
                                             io_type=io_type,
                                             signal_type=signal_type,
                                             isolation=isolation,
                                             unit=unit,
                                             min_range=min_range,
                                             max_range=max_range,
                                             remark=remark,
                                             power_supply=power_supply,
                                             connect_to_system=connect_to_system,
                                             lll=lll,
                                             ll=ll,
                                             l=l,
                                             h=h,
                                             hh=hh,
                                             hhh=hhh,
                                             tendency=tendency)


def import_dcs_connection(sheet, row_index):
    code = load_null_blank_cell(sheet, row_index, column('C'))
    belong_to_system = load_cell(sheet, row_index, column('B'))
    description = load_cell(sheet, row_index, column('D'))
    dcs_cabinet_number = load_cell(sheet, row_index, column('E'))
    id_type = load_cell(sheet, row_index, column('F'))
    signal_type = load_cell(sheet, row_index, column('G'))
    face_name = load_cell(sheet, row_index, column('H'))
    clamp = load_int_cell(sheet, row_index, column('I'))
    channel = load_int_cell(sheet, row_index, column('J'))
    terminal_a = load_int_cell(sheet, row_index, column('K'))
    terminal_b = load_int_cell(sheet, row_index, column('L'))
    terminal_c = load_int_cell(sheet, row_index, column('M'))
    cable_number_1 = load_cell(sheet, row_index, column('N'))
    cable_number_2 = load_cell(sheet, row_index, column('O'))
    cable_number_3 = load_cell(sheet, row_index, column('P'))
    cable_code = load_cell(sheet, row_index, column('Q'))
    cable_model = load_cell(sheet, row_index, column('R'))
    cabel_backup_core = load_cell(sheet, row_index, column('S'))
    cable_direction = load_cell(sheet, row_index, column('T'))
    remarks = load_cell(sheet, row_index, column('U'))
    DCSConnection.objects.update_or_create(
        code=code,
        belong_to_system=belong_to_system,
        description=description,
        dcs_cabinet_number=dcs_cabinet_number,
        id_type=id_type,
        signal_type=signal_type,
        face_name=face_name,
        clamp=clamp,
        channel=channel,
        terminal_a=terminal_a,
        terminal_b=terminal_b,
        terminal_c=terminal_c,
        cable_number_1=cable_number_1,
        cable_number_2=cable_number_2,
        cable_number_3=cable_number_3,
        cable_code=cable_code,
        cable_model=cable_model,
        cabel_backup_core=cabel_backup_core,
        cable_direction=cable_direction,
        remarks=remarks,)


def import_local_control_cabinet(sheet, row_index):
    code = load_null_blank_cell(sheet, row_index, column('B'))
    name = load_cell(sheet, row_index, column('C'))
    specification = load_cell(sheet, row_index, column('D'))
    maintenance_record = load_cell(sheet, row_index, column('E'))
    workshop_code = load_cell(sheet, row_index, column('F'))
    terminal_count = load_int_cell(sheet, row_index, column('H'))
    remark = load_cell(sheet, row_index, column('I'))
    deployed_to = load_cell(sheet, row_index, column('J'))
    if len(workshop_code) > 0:
        workshop = StormWorkshop.objects.get(code=workshop_code)
    else:
        workshop = None

    LocalControlCabinet.objects.update_or_create(
        code=code,
        name=name,
        specification=specification,
        deployed_to=deployed_to,
        terminal_count=terminal_count,
        remark=remark,
        maintenance_record=maintenance_record,
        workshop=workshop)


def import_local_control_connection(sheet, row_index):
    code = load_cell(sheet, row_index, column('B'))
    if code is None or len(code) == 0:
        return
    figure_number = load_cell(sheet, row_index, column('A'))
    name = load_cell(sheet, row_index, column('C'))
    instrument_type = load_cell(sheet, row_index, column('D'))
    cable_wire_model = load_cell(sheet, row_index, column('E'))
    cable_pipe_model = load_cell(sheet, row_index, column('F'))
    cable_index = load_cell(sheet, row_index, column('L'))
    cable_model = load_cell(sheet, row_index, column('M'))
    cable_backup_core = load_cell(sheet, row_index, column('N'))
    cable_direction = load_cell(sheet, row_index, column('O'))
    remark = load_cell(sheet, row_index, column('P'))
    cabinet_code = load_cell(sheet, row_index, column('I'))
    if len(cabinet_code) > 0:
        cabinet = LocalControlCabinet.objects.get(code=cabinet_code)
    else:
        cabinet = None

    obj, created = LocalControlCabinetConnection.objects.update_or_create(
        code=code,
        figure_number=figure_number,
        name=name,
        instrument_type=instrument_type,
        cable_wire_model=cable_wire_model,
        cable_pipe_model=cable_pipe_model,
        cable_index=cable_index,
        cable_model=cable_model,
        cable_backup_core=cable_backup_core,
        cable_direction=cable_direction,
        remark=remark,
        cabinet=cabinet,)

    return obj


current_local_control_connection = None


def import_local_control_cabinet_terminal(sheet, row_index):
    global current_local_control_connection
    for_connection_code = load_cell(sheet, row_index, column('B'))
    if len(for_connection_code) > 0:
        # current_local_control_connection = LocalControlCabinetConnection.objects.get(code=for_connection_code)
        current_local_control_connection = import_local_control_connection(sheet, row_index)

    cabinet_code = load_cell(sheet, row_index, column('I'))
    if len(cabinet_code) > 0:
        cabinet = LocalControlCabinet.objects.get(code=cabinet_code)
    else:
        cabinet = None
    cabinet_terminal = load_int_cell(sheet, row_index, column('J'))
    if cabinet_terminal is None:
        cabinet_terminal = load_null_blank_cell(sheet, row_index, column('J'))
    cabinet_cable_number = load_cell(sheet, row_index, column('K'))
    instrument_terminal = load_cell(sheet, row_index, column('G'))
    instrument_cable_number = load_cell(sheet, row_index, column('H'))

    LocalControlCabinetTerminal.objects.update_or_create(cabinet=cabinet,
                                                         cabinet_terminal=str(cabinet_terminal),
                                                         cabinet_cable_number=cabinet_cable_number,
                                                         instrument_terminal=instrument_terminal,
                                                         instrument_cable_number=instrument_cable_number,
                                                         for_connection=current_local_control_connection)


def import_device_link_info(sheet, row_index):
    left_device_code = load_cell(sheet, row_index, column('B'))
    right_device_codes = load_cell(sheet, row_index, column('M'))
    if left_device_code is None \
            or right_device_codes is None \
            or len(left_device_code) == 0 \
            or len(right_device_codes) == 0:
        return

    left_device = StormDevice.objects.get(code=left_device_code)

    for right_deice_code in right_device_codes.split():
        info = DeviceLinkInfo.objects.filter(left_device__code=left_device_code, right_device__code=right_deice_code)
        if info is not None and len(info) > 0:
            raise ValueError('duplicated record with left_device_code:%s right_device_code:%s'
                             % (left_device_code, right_deice_code))

        try:
            right_device = StormDevice.objects.get(code=right_deice_code)
        except Exception:
            raise ValueError('can not find specified device with code:%s' % (right_deice_code,))
        DeviceLinkInfo.objects.update_or_create(left_device=left_device,
                                                right_device=right_device)


def do_import_data(file_path, sheet_name, row_offset, load_func, description):
    begin_time = time.time()
    imported_count = 0

    errors = []
    with xlrd.open_workbook(file_path) as file_data:
        try:
            sheet = file_data.sheet_by_name(sheet_name)
        except xlrd.XLRDError:
            return {
                'res': 'NoSuchSheet',
                'description': description,
                'sheet_name': sheet_name,
                'errors': errors
            }

        if sheet is not None:
            nrows = sheet.nrows
            for row_index in range(row_offset, nrows):
                try:
                    load_func(sheet, row_index)
                    imported_count += 1
                except Exception as e:
                    errors.append({
                        'row': row_index+1,
                        'msg': str(e)
                    })
        else:
            nrows = 0

    return {
        'res': 'OK',
        'description': description,
        'sheet_name': sheet_name,
        'imported_count': imported_count,
        'rows': nrows-row_offset,
        'used_time': time.time()-begin_time,
        'errors': errors
    }


def update_db_meta():
    db_metas = DatabaseMeta.objects.all()
    if len(db_metas) == 0:
        meta = DatabaseMeta()
    else:
        meta = db_metas[0]

    meta.version = int(round(time.time() * 1000))
    meta.save()


sheet_info = [(u'车间列表', u'导入车间信息', 1, import_workshop),
              (u'设备库', u'导入设备库信息', 2, import_device),
              (u'设备库', u'导入设备系统信息', 2, import_device_link_info),
              (u'盘台', u'导入DCS控制柜信息', 1, import_cabinet),
              (u'接线库', u'导入DCS接线库信息', 2, import_dcs_connection),
              (u'AIO', u'导入AIO信息', 1, import_aio_signal),
              (u'DIO', u'导入DIO信息', 1, import_dio_signal),
              (u'就地柜盒一览表', u'导入就地柜盒信息', 1, import_local_control_cabinet),
              (u'就地柜盒接线', u'导入就地柜盒接线信息', 2, import_local_control_cabinet_terminal)]


def import_data(file_name, file_data):
    folder = 'media/uploads/'
    file_path = folder + file_name
    if not os.path.exists(folder):
        os.makedirs(folder)
    with open(file_path, 'wb+')as destination:
        for chunk in file_data.chunks():
            destination.write(chunk)

    res = []
    for (sheet_name, description, row_offset, load_func) in sheet_info:
        result = do_import_data(file_path, sheet_name, row_offset, load_func, description)
        res.append(result)

    update_db_meta()

    os.unlink(file_path)
    return res
