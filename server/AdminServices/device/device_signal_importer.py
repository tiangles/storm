# -*- coding: UTF-8 -*-

import xlrd
from models import DeviceSignal


def import_signal(file_path):
    workbook = xlrd.open_workbook(file_path)
    table = workbook.sheets()[1]
    rows = table.nrows
    for i in range(1, rows):
        row_values = table.row_values(i)
        signal = DeviceSignal(figure_number=row_values[1],
                              code=row_values[2],
                              name=row_values[3],
                              io_type=row_values[4],
                              signal_type=row_values[5],
                              power_supply=row_values[6],
                              isolate=True if row_values[7] is not None else False,
                              connect_to=row_values[8],
                              unit=row_values[9],
                              min_value=row_values[10],
                              max_value=row_values[11],
                              lll=True if row_values[12] is not None else False,
                              ll=True if row_values[13] is not None else False,
                              l=True if row_values[14] is not None else False,
                              h=True if row_values[15] is not None else False,
                              hh=True if row_values[16] is not None else False,
                              hhh=True if row_values[17] is not None else False,
                              tendency=True if row_values[18] is not None else False,)
        signal.save()

