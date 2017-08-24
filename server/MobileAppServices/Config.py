import os
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

SERVER_ADDRESS = ''
SERVER_PORT = 8080

settings = {}

db_config = {
    'backup': 'sqlite',
    'db_name': os.path.join(BASE_DIR, 'database', 'storm.sqlite3')
}

cmd_handlers = [
    (r'login',        'handlers.Handler.handle_login'),
    (r'upload_event', 'handlers.Handler.handle_upload_event'),
    (r'update_device', 'handlers.Handler.handle_update_device'),
    (r'sync_device', 'handlers.Handler.handle_sync_device'),
    (r'sync_workshop_list', 'handlers.Handler.handle_sync_workshop_list'),
    (r'sync_workshop', 'handlers.Handler.handle_sync_workshop'),
    (r'get_signal_parameter_record', 'handlers.Handler.handle_get_signal_parameter_record'),

]

