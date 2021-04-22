import sys
import subprocess
from kazoo.client import KazooClient, KazooState


class AppRunner:
    HOSTS = ['127.0.0.1:2181', '127.0.0.1:2182', '127.0.0.1:2183']
    NODE = '/z/'

    def __init__(self, target_path):
        self.target_path = target_path
        self.app_process = None
        self.nodes = set()

    @staticmethod
    def format_node(node, child=None):
        if node[-1] != '/':
            node += '/'
        if child is not None:
            return AppRunner.format_node(node+child)
        return node

    def start_app(self):
        if self.app_process is None or self.app_process.poll() is not None:
            print("Run app")
            self.app_process = subprocess.Popen(self.target_path)

    def stop_app(self):
        if self.app_process is None:
            return
        print("Stop app")
        self.app_process.terminate()

    @staticmethod
    def get_tree(zk, node=None):
        if not node:
            node = AppRunner.NODE
        res = [node]
        for child in zk.get_children(node):
            res.extend(AppRunner.get_tree(zk, AppRunner.format_node(node, child)))
        return res

    @staticmethod
    def print_tree(zk):
        print('\n'.join(AppRunner.get_tree(zk)))

    def run(self):
        zk = KazooClient(hosts=AppRunner.HOSTS)
        zk.start()

        def create_watcher(node):
            def watch_children(children):
                print(f'Number of children: {len(self.nodes)}')
                for child in children:
                    child_node = AppRunner.format_node(node, child)
                    if child_node in self.nodes:
                        continue
                    self.nodes.add(child_node)
                    create_watcher(child_node)

            @zk.DataWatch(node)
            def watch_node(data, stat):
                node_path = AppRunner.format_node(node)
                if stat is None:
                    print(f'Node {node} deleted')
                    if node_path in self.nodes:
                        self.nodes.remove(node_path)
                    if AppRunner.NODE == node_path:
                        self.stop_app()
                else:
                    print(f'Node {node} created')
                    self.nodes.add(node_path)
                    zk.ChildrenWatch(node, watch_children)
                    if AppRunner.NODE == node_path:
                        self.start_app()

        create_watcher(AppRunner.NODE)



        while True:
            cmd = input()
            if cmd == 'tree':
                AppRunner.print_tree(zk)


if __name__ == '__main__':
    # target_path = '/usr/bin/kcalc'
    target_path = sys.argv[1:][0]
    runner = AppRunner(target_path).run()
