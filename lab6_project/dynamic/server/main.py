import Demo
import sys, Ice
from overrides import overrides


class Dynamic(Demo.Calc):
    def __init__(self):
        super(Dynamic, self).__init__()

    def add(self, a, b, current=None):
        print("CALL ADD")
        print(a)
        print(b)
        return a+b

    def subtract(self, a, b, current=None):
        print("CALL SUBTRACT")
        print(a)
        print(b)
        return a-b

    def op(self, a1, b1, current=None):
        print("CALL OP")
        print(b1)
        print(a1)
        return f'{a1.d} | {b1}'

# export ICE_CONFIG=/home/maciektr/Programowanie/AGH_Laby/distributed_lab/lab6_project/dynamic/server/config.server
if __name__ == '__main__':
    with Ice.initialize(sys.argv) as icm:
        adapter = icm.createObjectAdapter('Adapter1')
        adapter.add(Dynamic(), icm.stringToIdentity('DynamicObject'))
        adapter.activate()
        icm.waitForShutdown()
