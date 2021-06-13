# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: streaming.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='streaming.proto',
  package='streaming',
  syntax='proto3',
  serialized_options=b'\n\013sr.grpc.genB\016StreamingProtoP\001',
  create_key=_descriptor._internal_create_key,
  serialized_pb=b'\n\x0fstreaming.proto\x12\tstreaming\"\x13\n\x04Task\x12\x0b\n\x03max\x18\x01 \x01(\x05\"\x17\n\x06Number\x12\r\n\x05value\x18\x01 \x01(\x05\".\n\x06Report\x12\r\n\x05\x63ount\x18\x01 \x01(\x05\x12\x15\n\rprocessorTime\x18\x02 \x01(\x03\x32\x8d\x01\n\x0cStreamTester\x12>\n\x14GeneratePrimeNumbers\x12\x0f.streaming.Task\x1a\x11.streaming.Number\"\x00\x30\x01\x12=\n\x11\x43ountPrimeNumbers\x12\x11.streaming.Number\x1a\x11.streaming.Report\"\x00(\x01\x42\x1f\n\x0bsr.grpc.genB\x0eStreamingProtoP\x01\x62\x06proto3'
)




_TASK = _descriptor.Descriptor(
  name='Task',
  full_name='streaming.Task',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='max', full_name='streaming.Task.max', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=30,
  serialized_end=49,
)


_NUMBER = _descriptor.Descriptor(
  name='Number',
  full_name='streaming.Number',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='value', full_name='streaming.Number.value', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=51,
  serialized_end=74,
)


_REPORT = _descriptor.Descriptor(
  name='Report',
  full_name='streaming.Report',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='count', full_name='streaming.Report.count', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='processorTime', full_name='streaming.Report.processorTime', index=1,
      number=2, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=76,
  serialized_end=122,
)

DESCRIPTOR.message_types_by_name['Task'] = _TASK
DESCRIPTOR.message_types_by_name['Number'] = _NUMBER
DESCRIPTOR.message_types_by_name['Report'] = _REPORT
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

Task = _reflection.GeneratedProtocolMessageType('Task', (_message.Message,), {
  'DESCRIPTOR' : _TASK,
  '__module__' : 'streaming_pb2'
  # @@protoc_insertion_point(class_scope:streaming.Task)
  })
_sym_db.RegisterMessage(Task)

Number = _reflection.GeneratedProtocolMessageType('Number', (_message.Message,), {
  'DESCRIPTOR' : _NUMBER,
  '__module__' : 'streaming_pb2'
  # @@protoc_insertion_point(class_scope:streaming.Number)
  })
_sym_db.RegisterMessage(Number)

Report = _reflection.GeneratedProtocolMessageType('Report', (_message.Message,), {
  'DESCRIPTOR' : _REPORT,
  '__module__' : 'streaming_pb2'
  # @@protoc_insertion_point(class_scope:streaming.Report)
  })
_sym_db.RegisterMessage(Report)


DESCRIPTOR._options = None

_STREAMTESTER = _descriptor.ServiceDescriptor(
  name='StreamTester',
  full_name='streaming.StreamTester',
  file=DESCRIPTOR,
  index=0,
  serialized_options=None,
  create_key=_descriptor._internal_create_key,
  serialized_start=125,
  serialized_end=266,
  methods=[
  _descriptor.MethodDescriptor(
    name='GeneratePrimeNumbers',
    full_name='streaming.StreamTester.GeneratePrimeNumbers',
    index=0,
    containing_service=None,
    input_type=_TASK,
    output_type=_NUMBER,
    serialized_options=None,
    create_key=_descriptor._internal_create_key,
  ),
  _descriptor.MethodDescriptor(
    name='CountPrimeNumbers',
    full_name='streaming.StreamTester.CountPrimeNumbers',
    index=1,
    containing_service=None,
    input_type=_NUMBER,
    output_type=_REPORT,
    serialized_options=None,
    create_key=_descriptor._internal_create_key,
  ),
])
_sym_db.RegisterServiceDescriptor(_STREAMTESTER)

DESCRIPTOR.services_by_name['StreamTester'] = _STREAMTESTER

# @@protoc_insertion_point(module_scope)