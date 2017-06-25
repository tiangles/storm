SERVER_ADDRESS = ''
SERVER_PORT = 8080

settings = {}

db_config = {
    'backup': 'sqlite',
    'db_name': 'storm.db'
}

cmd_handlers = [
    (r'login', 'handlers.Handler.handle_login')
]

