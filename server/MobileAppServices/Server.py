import threading
from importlib import import_module
import handlers.Handler
import tornado.websocket
import json
import Config

import sys


def import_string(dotted_path):
    """
    Import a dotted module path and return the attribute/class designated by the
    last name in the path. Raise ImportError if the import failed.
    """
    try:
        module_path, class_name = dotted_path.rsplit('.', 1)
    except ValueError:
        msg = "%s doesn't look like a module path" % dotted_path
        raise msg
    module = import_module(module_path)

    try:
        return getattr(module, class_name)
    except AttributeError:
        msg = 'Module "%s" does not define a "%s" attribute/class' % (
            module_path, class_name)
        raise msg


class WebsocketHandler(tornado.websocket.WebSocketHandler):
    clients = set()

    def data_received(self, chunk):
        pass

    def __init__(self, application, request, **kwargs):
        super(WebsocketHandler, self).__init__(application, request, **kwargs)
        self.handlers = {}
        for (c, h) in Config.cmd_handlers:
            self.handlers[c] = import_string(h)

    def open(self):
        print 'Get connection request'
        self.write_message(json.dumps({
            'cmd': 'connect',
            'result': 'succeed',
            'code': 101
        }))
        WebsocketHandler.clients.add(self)

    def on_close(self):
        WebsocketHandler.clients.remove(self)

    def on_message(self, dat):
        message = json.loads(dat)
        print 'Handle message: %s' % message

        cmd = message['cmd']
        handled = False
        for (c, h) in self.handlers.items():
            # find a handler to handle the command
            if c == cmd:
                try:
                    (code, msg) = h(self, message)
                except Exception, e:
                    (code, msg) = -1, e.__str__()

                result = {
                    'cmd': cmd,
                    'result': code,
                    'message': msg,
                }
                print 'Send message: %s' % result
                self.write_message(result)
                handled = True
                break
        # tell client
        if not handled:
            result = {
                'cmd': cmd,
                'result': -1,
                'message': 'unknown command',
            }
            self.write_message(json.dumps(result))

    @classmethod
    def broadcast(cls, msg):
        for client in WebsocketHandler.clients:
            client.write_message(json.dumps(msg))


timer = None


def timer_task():
    message = {
        'cmd': 'signal_parameter_updated',
        'result': 0,
        'message': {
            'device_code' : 'HNC10AN001',
            'value': ''
        },
    }
    WebsocketHandler.broadcast(message)

    global timer
    timer = threading.Timer(2.0, timer_task, [])
    timer.start()


class Application(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r'/api', WebsocketHandler),
            (r'/database', WebsocketHandler),
        ]
        tornado.web.Application.__init__(self, handlers, **Config.settings)


if __name__ == '__main__':
    reload(sys)
    sys.setdefaultencoding('utf8')

    ws_app = Application()
    server = tornado.httpserver.HTTPServer(ws_app)
    server.listen(Config.SERVER_PORT)
    print('Mobile server is running at %s:%d' % (handlers.Handler.get_host_ip(), Config.SERVER_PORT))
    print('Quit the server with CONTROL-C')
    tornado.ioloop.IOLoop.instance().start()
