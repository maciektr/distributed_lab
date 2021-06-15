defmodule Telemetry.Application do
  use Application

  def start(_type, _args) do

    children = [
      {GRPC.Server.Supervisor, {Telemetry.Endpoint, 50051}}
    ]

    # See https://hexdocs.pm/elixir/Supervisor.html
    # for other strategies and supported options
    opts = [strategy: :one_for_one, name: Telemetry.Supervisor]
    Supervisor.start_link(children, opts)
  end
end
