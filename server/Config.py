import handlers.Handler

SERVER_ADDRESS = ''
SERVER_PORT = 8080

settings = {}

cmd_handlers = [
    (r'login', handlers.Handler.handle_login)
]
