import tornado.websocket
import json
import Config


class SocketHandler(tornado.websocket.WebSocketHandler):
    clients = set()

    # def check_origin(self, origin):
    #     return True
    #
    def open(self):
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
        for (c, h) in Config.cmd_handlers:
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