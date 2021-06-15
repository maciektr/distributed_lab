# export PATH="/home/maciektr/.asdf/installs/elixir/1.11.3-otp-23/.mix/escripts:$PATH"
# python -m grpc_tools.protoc -I. --proto_path=../proto --elixir_out=./lib ../proto/interface.proto

protoc --elixir_out=plugins=grpc:./lib ../proto/interface.proto -I../proto
