from .models import Device as StormDevice
from .models import Workshop as StormWorkshop
import xlrd
import time


def create_entry_error(row, exception):
    return 'Database error "%s"'%(exception)


def load_null_blank_cell(sheet, row, col):
    cell = sheet.cell(row, col)
    if cell is None or cell.ctype ==xlrd.XL_CELL_EMPTY or cell.ctype == xlrd.XL_CELL_BLANK:
        raise ValueError('Blank cell col:%d'%(col))
    return cell.value


def import_device(row):
    try:
        code = row[1]
        model = row[7]
        name = row[2]
        system = row[16]
        distribution_cabinet = row[22]
        local_control_panel = row[24]
        dcs_cabinet = row[26]
        legend = row[4] + row[3]
        workshop_name = row[15]
        workshop = StormWorkshop.objects.get(name=row[15])
        if workshop is None:
            print("Can't find workshop with name%s"%workshop_name)
            return

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

    except Exception as e:
        print e


def import_workshop(sheet, row_index):
    workshop_index = load_null_blank_cell(sheet, row_index, 1)
    code = load_null_blank_cell(sheet, row_index, 2)
    name = load_null_blank_cell(sheet, row_index, 3)
    workshop = StormWorkshop.objects.create(workshop_index=workshop_index,
                                            code=code,
                                            name=name)
    workshop.save()



def import_device_data(file_path):
    with xlrd.open_workbook(file_path) as file_data:
        table = file_data.sheet_by_index(0)
        rows = table.nrows
        for row_index in range(1, rows):
            row = table.row_values(row_index)
            import_device(row)


def import_workshop_data(file_path):
    begin = time.time()
    import_result = []
    count = 0
    with xlrd.open_workbook(file_path) as file_data:
        sheet = file_data.sheet_by_index(0)
        rows = sheet.nrows
        for row_index in range(1, rows):
            try:
                import_workshop(sheet, row_index)
                count += 1
            except Exception as e:
                msg = 'Import error, "%s" at row:%d' % (e, row_index)
                import_result.append(msg)

    for result in import_result:
        print result
    print ('Done, imported %d entries from %d rows, used time: %d seconds' % (count, rows, time.time()-begin))


if __name__ == '__main__':
    import_workshop_data('/home/btian/workshop.xlsx')
    import_device_data('/home/btian/device.xlsx')