from http.server import BaseHTTPRequestHandler, HTTPServer
import json

class Handler(BaseHTTPRequestHandler):
    def do_POST(self):
        length = int(self.headers.get('Content-Length', '0'))
        payload = self.rfile.read(length).decode('utf-8')
        print(json.dumps({'event': 'notification', 'payload': payload}, ensure_ascii=False), flush=True)
        self.send_response(202)
        self.send_header('Content-Type', 'application/json')
        self.end_headers()
        self.wfile.write(b'{"accepted":true}')
    def do_GET(self):
        self.send_response(200)
        self.send_header('Content-Type', 'application/json')
        self.end_headers()
        self.wfile.write(b'{"service":"notification-center","status":"ready"}')

HTTPServer(('0.0.0.0', 8090), Handler).serve_forever()
