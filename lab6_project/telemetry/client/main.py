import grpc
import json
from time import sleep, time
from random import randint, choice, uniform
from termcolor import colored
from datetime import datetime

import gen.interface_pb2 as pb2
import gen.interface_pb2_grpc as pb2_grpc

# export GRPC_VERBOSITY=debug
# export GRPC_TRACE=all


class Client:
    _OPTIONS = [
        ("grpc.keepalive_time_ms", 10000),
        ("grpc.keepalive_timeout_ms", 5000),
        ("grpc.keepalive_permit_without_calls", 1),
        ("grpc.http2_max_pings_without_data", 0),
        ("grpc.http2_min_sent_ping_interval_without_data_ms", 10000),
    ]

    def __init__(self):
        self.config_path = "config.json"
        self.config = self._read_config()
        self.stub = None
        self.buffer = []
        self.buffer_timestamp = time()
        self.naive = self.config["client_strategy"]["strategy"] == "naive"
        self.buffer_time = (
            self.config["client_strategy"]["buffer_time"] if not self.naive else None
        )

    def _read_config(self):
        with open(self.config_path, "r") as config_file:
            return json.load(config_file)

    def _get_random_reading(self) -> pb2.Reading:
        meter_config = choice(self.config["meters"])

        def make_reading(**kwargs):
            return pb2.Reading(
                meter_id=meter_config["id"],
                sender_id=self.config["sender_id"],
                timestamp=datetime.now().strftime("%d/%m/%Y %H:%M:%S"),
                **kwargs
            )

        if meter_config["type"] == "water":
            meter = pb2.Reading.WaterMeter(reading=randint(1000, 10000))
            return make_reading(water=meter)

        if meter_config["type"] == "temperature":
            meter = pb2.Reading.TemperatureMeter(reading=uniform(0.0, 100.0))
            return make_reading(temperature=meter)

        meter = pb2.Reading.ElectricityMeter(
            reading=randint(1000, 10000), type=choice(["day", "night"])
        )
        return make_reading(electricity=meter)

    @staticmethod
    def log(msg, status="info"):
        def get_color():
            if status == "info":
                return "grey"
            if status == "ok":
                return "green"
            return "red"

        print(colored(msg, get_color()))

    def send(self, reading):
        if self.naive:
            self.stub.sendReading(reading)
            Client.log("Sending single reading", "ok")
        else:
            self.buffer.append(reading)

            def pack_readings(buffer):
                return pb2.Readings(readings=buffer)

            timestamp = time()
            if timestamp - self.buffer_timestamp > self.buffer_time:
                self.stub.sendReadings(pack_readings(self.buffer))
                Client.log("Sending multiple readings", "ok")
                self.buffer = []
                self.buffer_timestamp = timestamp

    def run(self):
        while True:
            reading = self._get_random_reading()
            with grpc.insecure_channel(
                self.config["server"], Client._OPTIONS
            ) as channel:
                self.stub = pb2_grpc.TelemetryStub(channel)
                try:
                    self.send(reading)
                    # Client.log("Connection correct", "ok")
                except grpc.RpcError as e:
                    status_code = e.code()
                    if e.details() == "Stream removed":
                        Client.log("Connection lost", "error")
                    elif status_code == grpc.StatusCode.UNAVAILABLE:
                        Client.log("Connection impossible", "error")
                        sleep(1)
                    else:
                        raise e


if __name__ == "__main__":
    client = Client()
    client.run()
