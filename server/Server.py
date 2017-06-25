from importlib import import_module

import tornado.websocket
import json
import Config


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


class SocketHandler(tornado.websocket.WebSocketHandler):
    clients = set()

    def __init__(self, application, request, **kwargs):
        super(SocketHandler, self).__init__(application, request, **kwargs)
        self.handlers = {}
        for (c, h) in Config.cmd_handlers:
            self.handlers[c] = import_string(h)

    def open(self):
        print "Get connection request"
        self.write_message(json.dumps({
            'cmd': 'connect',
            'result': 'succeed',
            'code': 101
        }))
        SocketHandler.clients.add(self)

    def on_close(self):
        SocketHandler.clients.remove(self)

    def on_message(self, dat):
        message = json.loads(dat)
        print "Handle message: %s" % message

        cmd = message['cmd']
        for (c, h) in self.handlers.items():
            if c == cmd:
                str = h(self, message)
                print "Send message: %s" % str
                self.write_message(str)
                break


class Application(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r'/', SocketHandler),
        ]
        tornado.web.Application.__init__(self, handlers, **Config.settings)


if __name__ == '__main__':
    ws_app = Application()
    server = tornado.httpserver.HTTPServer(ws_app)
    server.listen(Config.SERVER_PORT)
    tornado.ioloop.IOLoop.instance().start()
