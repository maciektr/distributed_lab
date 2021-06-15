defmodule Telemetry.Endpoint do
  use GRPC.Endpoint

  intercept GRPC.Logger.Server
  run Telemetry.Telemetry.Server
end

defmodule Telemetry.Telemetry.Server do
  use GRPC.Server, service: Telemetry.Telemetry.Service

  def send_reading(request, _stream) do
    IO.inspect(request)
    Telemetry.Response.new()
  end

  def send_readings(request, _stream) do
    IO.inspect(request)
    Telemetry.Response.new()
  end
end
